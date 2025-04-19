# Use an official Python runtime
FROM python:3.9

# Tell Hugging Face Spaces your app runs on port 5000 (✨ this is the key)
ENV PORT 7860s

# Set the working directory
WORKDIR /app

# Copy only the requirements file first (for Docker cache)
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of your application code into the container
COPY . .

# Expose the internal port (this is for documentation only in Docker)
EXPOSE 7860

# Run the Flask app via Gunicorn, binding to the correct port
CMD ["gunicorn", "--workers", "1", "--bind", "0.0.0.0:7860", "app:app"]