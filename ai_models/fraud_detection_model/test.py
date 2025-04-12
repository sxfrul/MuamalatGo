import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler, OrdinalEncoder
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
import shap
import matplotlib.pyplot as plt
import xgboost as xgb
from sklearn.metrics import classification_report, confusion_matrix

# Load the labeled dataset
df = pd.read_csv('labeled_data.csv')
df = df.drop(columns=["Maklumat Isi Rumah"])

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

df = df.drop(columns=["Dokumen Lampiran Utama"])

import json

# Step 1: Convert JSON-like strings to dictionaries
df["Sumber Pendapatan Bulanan"] = df["Sumber Pendapatan Bulanan"].apply(json.loads)

# Step 2: Normalize the data into a DataFrame
income_df = pd.json_normalize(df["Sumber Pendapatan Bulanan"])

# Step 3: Concatenate with the original DataFrame (if necessary)
df = pd.concat([df, income_df], axis=1)