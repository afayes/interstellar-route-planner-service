#!/bin/bash
# Script to build and deploy the application to AWS ECS from local machine
# Prerequisites: AWS CLI configured, Docker running, Terraform applied

# aws configured - e.g.: export AWS_PROFILE=admin 

set -e

AWS_REGION="eu-west-2"
APP_NAME="interstellar-route-planner"

# Get ECR repository URL from Terraform output
ECR_REPO_URL=$(cd infra && terraform output -raw ecr_repository_url)
AWS_ACCOUNT_ID=$(echo "$ECR_REPO_URL" | cut -d'.' -f1)

echo "Building JAR..."
./mvnw clean package -DskipTests

echo "Building Docker image for linux/amd64..."
docker build --platform linux/amd64 -t "$APP_NAME" .

echo "Logging in to ECR..."
aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"

echo "Tagging image..."
docker tag "$APP_NAME:latest" "$ECR_REPO_URL:latest"

echo "Pushing image to ECR..."
docker push "$ECR_REPO_URL:latest"

echo "Forcing new ECS deployment..."
aws ecs update-service --cluster "$APP_NAME-cluster" --service "$APP_NAME-service" --force-new-deployment --region "$AWS_REGION" > /dev/null

echo "Deployment triggered. Wait 2-3 minutes for the service to stabilise."
echo "Health check: curl $(cd infra && terraform output -raw alb_url)/actuator/health"