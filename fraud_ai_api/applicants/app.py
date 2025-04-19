import traceback
import pickle
import numpy as np
import pandas as pd
import json
from flask import Flask, request, jsonify

from sklearn.preprocessing import OrdinalEncoder

# Function for preprocessing
def preprocessing(df):
    
    # List of documents to flag
    DOCUMENT_LIST = [
        "Salinan KP Pemohon", "Salinan KP Pasangan", "Salinan KP Tanggungan",
        "Kad Islam", "Penyata Bank", "Sijil Nikah/Cerai/Mati",
        "Slip Gaji", "Kad OKU", "Bil Utiliti", "Penyata KWSP",
        "Sokongan Tambahan"
    ]

    # Function to flag presence of documents
    def flag_documents(dokumen_string):
        flags = {doc: (doc in dokumen_string) for doc in DOCUMENT_LIST}
        return pd.Series(flags)

    # Apply the function and create new columns
    df_flags = df["Dokumen Lampiran Utama"].apply(flag_documents)

    # Concatenate the original DataFrame with the flags DataFrame
    df = pd.concat([df, df_flags], axis=1)

    # Dropping out the flagged column
    df = df.drop(columns=["Dokumen Lampiran Utama"])

    # Step 1: Convert JSON-like strings to dictionaries
    df["Sumber Pendapatan Bulanan"] = df["Sumber Pendapatan Bulanan"].apply(json.loads)

    # Step 2: Normalize the data into a DataFrame
    income_df = pd.json_normalize(df["Sumber Pendapatan Bulanan"])

    # Step 3: Prepend "Sumber Pendapatan " to each column name
    income_df.columns = [f"Sumber Pendapatan {col}" for col in income_df.columns]

    # Step 4: Concatenate with the original DataFrame
    df = pd.concat([df.drop(columns=["Sumber Pendapatan Bulanan"]), income_df], axis=1)

    # Step 1: Convert JSON-like strings to dictionaries
    df["Perbelanjaan Bulanan"] = df["Perbelanjaan Bulanan"].apply(json.loads)

    # Step 2: Normalize the data into a DataFrame
    expenses_df = pd.json_normalize(df["Perbelanjaan Bulanan"])

    # Step 3: Prepend "Perbelanjaan " to each column name
    expenses_df.columns = [f"Perbelanjaan {col}" for col in expenses_df.columns]

    # Step 4: Concatenate with the original DataFrame
    df = pd.concat([df.drop(columns=["Perbelanjaan Bulanan"]), expenses_df], axis=1)

    # Step 1: Convert the column to datetime
    df['Tarikh'] = pd.to_datetime(df['Tarikh'])
    df['Tarikh Lahir'] = pd.to_datetime(df['Tarikh Lahir'])

    # Step 2: Calculate age
    df['Umur'] = (df['Tarikh'] - df['Tarikh Lahir']).dt.days // 365

    # Step 1: Convert the column to datetime
    df['Tarikh'] = pd.to_datetime(df['Tarikh'])
    df['Tarikh Masuk Islam'] = pd.to_datetime(df['Tarikh Masuk Islam'])

    # Step 2: Calculate age
    df['Umur'] = (df['Tarikh'] - df['Tarikh Masuk Islam']).dt.days

    df = df.drop("Tarikh Lahir", axis=1)
    df = df.drop("Tarikh Masuk Islam", axis=1)

    # potential noisy feature to flag
    noisy_features = [
        'No. K/P (baru)/Polis/Tentera/No. Pasport',
        'Nama Pemohon/Institusi',
        'Alamat',
        'Emel',
        'No. Telefon Bimbit',
        'No. Telefon Rumah/Waris',
        'Nama waris',
        'Nama Pemegang Akaun',
        'No. Akaun Bank',
        'Nama Si Mati',
        'No. Kad Pengenalan Si Mati',
        'Nama Majikan',
        'No. Tel. Majikan',
        'Nama Kakitangan',
        'Jawatan (Kakitangan Berhubungan)',
        'Tarikh'
    ]

    for feature in noisy_features:
        flagname = f"{feature}_flag"
        df[flagname] = df[feature].notnull().astype(int)
        df = df.drop(feature, axis=1)
        
    # One hot encoding    
    df = pd.concat([df, pd.get_dummies(df['Sebab Memohon Bantuan'], prefix='Sebab_Memohon_Bantuan')], axis=1)
    df = pd.concat([df, pd.get_dummies(df['Kategori pemohon'], prefix='Kategori_pemohon')], axis=1)
    df = pd.concat([df, pd.get_dummies(df['Sebab Tidak Bekerja'], prefix='Sebab Tidak Bekerja')], axis=1)

    df = df.drop("Sebab Memohon Bantuan", axis=1)
    df = df.drop("Kategori pemohon", axis=1)
    df = df.drop("Sebab Tidak Bekerja", axis=1)

    # OrdinalEncoder cannot handle null
    df[categorical_data] = df[categorical_data].fillna("missing").astype(str)

    df[categorical_data] = ordinal_encoder.transform(df[categorical_data])

    # Replacing None with np.nan as lightgbm doesn't recognise None object type
    df = df.replace({None: np.nan})
    
    # seems like lgbm does not support JSON character for column name
    df.columns = df.columns.str.replace(r'[^\w\s]', '_', regex=True)
    
    return df

# Function to load pkl file
def load_pkl_file(path, name):
    try:
        with open(path, 'rb') as f:
            pkl_file = pickle.load(f)
        print(f"{name} loaded successfully from {path}")
    except FileNotFoundError:
        print(f"Error: {name} file not found at {path}")
        pkl_file = None
    except Exception as e:
        print(f"Error loading {name}: {e}")
        traceback.print_exc()
        pkl_file = None
    return pkl_file

# Initialize Flask App
app = Flask(__name__)

# Loading the pkl file
fraud_model = load_pkl_file('fraud_model.pkl', 'Fraud model')
columns = load_pkl_file('columns.pkl', 'Columns')
ordinal_encoder = load_pkl_file('ordinal_encoder.pkl', 'Ordinal encoder')
categorical_data = load_pkl_file('categorical_data_columns.pkl', 'Categorical data columns')
# asnaf_model = load_pkl_file('asnaf_model.pkl', 'Asnaf model')
# asnaf_class_map = load_pkl_file('class_map.pkl', 'Asnaf class map')

# Define a health check route
@app.route('/')
def home():
    # Simple check to see if the server is running and model loaded
    status = "Model loaded successfully." if fraud_model else "Model loading failed. Check logs."
    return f"Flask server is running! {status}"

# 4. Define the prediction route
@app.route('/predict', methods=['POST'])
def predict():
    if fraud_model is None:
        return jsonify({'error': 'Model is not loaded. Cannot make predictions.'}), 500

    if not request.is_json:
        return jsonify({'error': 'Request must be JSON'}), 400

    try:
        # Get data from the POST request
        data = request.get_json()

        # if 'features' not in data:
        #     return jsonify({'error': 'Missing "features" key in JSON data'}), 400

        data = pd.DataFrame([data])
        print(len(data.columns))
        try:
            test_data = preprocessing(data.copy())
            test_data = test_data.reindex(columns=columns, fill_value=0)
            print(len(test_data.columns))
            # print(f"Received features: {columns}")
            # print(f"Processed features shape: {test_data.shape}")
        except Exception as e:
             return jsonify({'error': f'Error processing features: {e}'}), 400
        # --- End Preprocessing ---


        # Make prediction
        fraud_prediction = fraud_model.predict(test_data)
        fraud_prediction_int = (fraud_prediction > 0.5).astype(int)
        # asnaf_prediction = asnaf_model.predict(test_data)
        # asnaf_prediction = asnaf_prediction.argmax(axis=1)
        # asnaf_class_names = [asnaf_class_map[i] for i in asnaf_prediction] # class_map is from the model training part

        # Convert prediction to standard Python list/type for JSON serialization
        # (e.g., NumPy arrays might not be directly serializable)
        # output = prediction.tolist() # Adjust if predict returns a single value
        fraud_output = int(fraud_prediction_int[0])
        print(f"Eligibility prediction: {fraud_output}")
        # asnaf_output = str(asnaf_class_names[0])
        # print(f"Asnaf predition: {asnaf_output}")

        # Return the prediction as JSON
        return jsonify({'fraud': fraud_output}) # Often predict returns an array, take the first element

    except Exception as e:
        print(f"Error during prediction: {e}")
        traceback.print_exc() # Log the full error for debugging
        return jsonify({'error': f'An error occurred during prediction: {str(e)}'}), 500

# 5. Run the Flask App
if __name__ == '__main__':
    # Set host='0.0.0.0' to make it accessible from outside the container/machine
    # debug=True is helpful for development (auto-reloads), but **remove it for production**
    app.run(host='0.0.0.0', port=5000, debug=True)