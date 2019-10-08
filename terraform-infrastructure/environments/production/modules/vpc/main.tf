# Module used to configure the VPC

resource "aws_vpc" "baseconnectvpc" {
  cidr_block = "10.0.0.0/16"
}

output "vpc_id" {
  value = "${aws_vpc.baseconnectvpc.id}"
}