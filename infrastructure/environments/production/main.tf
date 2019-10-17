# Main Module to startup infrastructure

provider "aws" {
 access_key = "${var.AWS_ACCESS_KEY_ID}"
 secret_key = "${var.AWS_SECRET_KEY_ID}"
 region = "us-gov-east-1"
}

module "bastion" {
  source = "./modules/bastion"
}

# module "securitygroups" {
#   source = "./modules/securitygroups"
# }terr

module "rds" {
  source = "./modules/rds"
}

module "beanstalk" {
  source = "./modules/beanstalk"
}

module "subnets" {
  source = "./modules/subnets"
}
module "vpc" {
  source = "./modules/vpc"
}
