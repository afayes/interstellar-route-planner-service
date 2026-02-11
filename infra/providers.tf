# ==============================================================================
# Terraform and provider configuration
# We define the Terraform version and the AWS provider
# ==============================================================================

# Terraform version and provider requirements. AWS provider ~> 5.0 is used for
# all resources in main.tf (VPC, ECS, ECR, ALB, IAM, CloudWatch, etc.).
terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# AWS provider: region is set from var.aws_region (default eu-west-2). All
# resources are created in this region unless overridden per-resource.
provider "aws" {
  region = var.aws_region
}

# Data source: list of available AZs in the provider region. Used by
# aws_subnet.public and aws_subnet.private to place subnets in two AZs for
# ALB and ECS high availability.
data "aws_availability_zones" "available" {
  state = "available"
}