# Module used to configured the Bastion Server

module "subnets" {
  source = "../subnets"
}

resource "aws_instance" "bastion" {
  ami           = "ami-28ed0d59"
  instance_type = "t3.large"
  subnet_id = "${module.subnets.public_facing_subnet}"

  tags = {
    Name = "bcgovbastion"
  }
}
