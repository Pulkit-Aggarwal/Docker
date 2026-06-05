FROM python:3.12-slim

WORKDIR /app

ARG DEBIAN_FRONTEND=noninteractive

# Install system dependencies required for building certain Python packages
# 2. Add --no-install-recommends to keep the download footprint tiny
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

# 3. IF YOU ARE RUNNING PIP NEXT, USE THIS ENVIRONMENT VARIABLE:
# This forces pip to compile sequentially rather than in parallel, saving massive amounts of RAM
ENV MAKEFLAGS="-j1"


# Copy and install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code
COPY . .

# Expose FastAPI's default port
EXPOSE 8000

# Start Uvicorn server
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]