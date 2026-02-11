# ==============================================================================
# Input variables – customize via terraform.tfvars or -var
# ==============================================================================
#
# Used across main.tf: app_name prefixes most resources; app_port aligns container,
# target group, and security groups; cpu/memory/desired_count control ECS sizing.
# ------------------------------------------------------------------------------

# AWS region for all resources. Also passed to ECS task definition for CloudWatch
# logs (awslogs-region). Affects data.aws_availability_zones.available.
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "eu-west-2"
}

# Application name: used in resource names (ECR, VPC, ALB, ECS cluster/service,
# security groups, IAM roles, log group) and as the ECS container name.
variable "app_name" {
  description = "Application name"
  type        = string
  default     = "interstellar-route-planner"
}

# Port the app listens on inside the container. Must match Spring Boot server.port.
# Used by: aws_ecs_task_definition.app portMappings, aws_lb_target_group.app,
# aws_ecs_service.app load_balancer.container_port, aws_security_group.ecs ingress.
variable "app_port" {
  description = "Port the application listens on"
  type        = number
  default     = 8080
}

# Fargate CPU units per task (256 = 0.25 vCPU). Sets aws_ecs_task_definition.app
# cpu. Valid combinations with memory are documented in AWS Fargate docs.
variable "cpu" {
  description = "Fargate task CPU units (256 = 0.25 vCPU)"
  type        = number
  default     = 256
}

# Fargate memory per task (MiB). Sets aws_ecs_task_definition.app memory. Must
# be a valid pair with cpu for Fargate (e.g. 256 CPU allows 512, 1024, 2048).
variable "memory" {
  description = "Fargate task memory (MiB)"
  type        = number
  default     = 512
}

# Desired number of running ECS tasks. Set on aws_ecs_service.app desired_count.
# Increase for higher availability or load; ALB distributes across tasks.
variable "desired_count" {
  description = "Number of ECS tasks to run"
  type        = number
  default     = 1
}