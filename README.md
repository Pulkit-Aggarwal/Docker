# Build Single-Stage
docker build -f Dockerfile.OneStage -t java-single .

# Build Multi-Stage
docker build -f Dockerfile.MultiStage -t java-multi .

# Run Single-Stage
docker run --rm java-single

# Run Multi-Stage
docker run --rm java-multi

# (The --rm flag automatically cleans up and removes the container instance after it finishes executing).

# Checking the Image size difference 
docker images
or 
docker images --filter "reference=java-*" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"

# Checking runtime memory footprint
# Run the multi-stage container in the background, sleeping for 60 seconds
docker run -d --name java-mem-test java-multi sleep 60

# Check the live memory footprint
docker stats java-mem-test --no-stream