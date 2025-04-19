# Dockerfile

# Use an official Python runtime (e.g., 3.9)
FROM python:3.9

# Set the working directory in the container
WORKDIR /app

# Copy only the requirements file first (for Docker cache)
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of your application code into the container
COPY . .

# Expose the port Spaces expects
EXPOSE 7860

# Command to run the Flask application using Gunicorn
# Looks for the 'app' object within the 'app.py' file
CMD ["gunicorn", "--workers", "1", "--bind", "0.0.0.0:7860", "app:app"]