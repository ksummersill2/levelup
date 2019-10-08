
# Module used to configure the subnets for infrastructure

module "vpc" {
  source = "../vpc"
}
resource "aws_subnet" "bcpublicsubnet" {
  vpc_id     = "${module.vpc.vpc_id}"
  cidr_block = "10.0.2.0/24"

  tags = {
    Name = "bcpublicsubnet"
  }
}
resource "aws_subnet" "bcprivatesubnet" {
  vpc_id     = "${module.vpc.vpc_id}"
  cidr_block = "10.0.1.0/24"

  tags = {
    Name = "bcprivatesubnet"
  }
}

output "public_facing_subnet" {
  value = "${aws_subnet.bcpublicsubnet.id}"
}