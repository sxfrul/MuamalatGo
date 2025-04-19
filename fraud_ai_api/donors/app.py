import traceback
import pickle
import numpy as np
import pandas as pd
import json
from flask import Flask, request, jsonify

from sklearn.preprocessing import OrdinalEncoder

# Function for preprocessing
def preprocessing(df):
    df['Income_per_Donation'] = df['Declared Income'] / (df['Donation Frequency'] + 1e-6) # Add small constant to avoid division by zero
    df['Donation_Pct_Income'] = (df['Average Donation Amount'] * df['Donation Frequency']) / (df['Declared Income'] + 1e-6)
    df['Savings_to_Income'] = df['Savings'] / (df['Declared Income'] + 1e-6)
    df['Investment_to_Income'] = df['Investment (Stocks/Bonds)'] / (df['Declared Income'] + 1e-6)
    
    df["Fraud Label (Rule-Based)"] = df["Fraud Label (Rule-Based)"].replace({"Not Fraud": 0, "Fraud": 1})
    df["Fraud Label (Rule-Based)"].value_counts()

    # Fill missing values
    df[cat_cols] = df[cat_cols].fillna('Unknown')
    df[num_cols] = df[num_cols].fillna(df[num_cols].mean())
    
    # Encode categorical features
    df[cat_cols] = encoder.fit_transform(df[cat_cols])

    df = df.drop(columns=['Fraud Label (Rule-Based)'])  # Drop the target variable

    # Scale numerical data (only features, not labels)
    df[num_cols] = scaler.fit_transform(df[num_cols])  # Scale only numerical features
    
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
fraud_model = load_pkl_file('fraud_donor_model.pkl', 'Fraud model')
columns = load_pkl_file('columns.pkl', 'Columns')
encoder = load_pkl_file('ordinal_encoder.pkl', 'Ordinal encoder')
cat_cols = load_pkl_file('categorical_data_columns.pkl', 'Categorical data columns')
num_cols = load_pkl_file('numerical_data_columns.pkl', 'Numerical data columns')
scaler = load_pkl_file('standard_scaler.pkl', 'Standard scaler')

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