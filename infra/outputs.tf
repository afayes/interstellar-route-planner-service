# ==============================================================================
# Terraform outputs – consumed by deploy scripts and for debugging
# ==============================================================================

# ECR repository URL: used to tag and push the built Docker image (e.g. in
# scripts/local-deploy.sh). ECS task definition pulls from this repository.
output "ecr_repository_url" {
  description = "ECR repository URL for pushing Docker images"
  value       = aws_ecr_repository.app.repository_url
}

# ALB URL: public endpoint for the app. Traffic hits the ALB, which forwards to
# ECS tasks in the target group. Use this after deploy to call the service.
output "alb_url" {
  description = "ALB URL to access the application"
  value       = "http://${aws_lb.main.dns_name}"
}

# ECS cluster name: useful for CLI/console (e.g. aws ecs list-tasks --cluster).
# Contains the ECS service and its tasks.
output "ecs_cluster_name" {
  description = "ECS cluster name"
  value       = aws_ecs_cluster.main.name
}

# ECS service name: identifies the service in the cluster for updates and
# scaling (e.g. aws ecs update-service to force new deployment or change count).
output "ecs_service_name" {
  description = "ECS service name"
  value       = aws_ecs_service.app.name
}