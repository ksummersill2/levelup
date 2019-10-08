# Module used to configure the security groups

resource "aws_security_group" "aurorasg" {
  name   = "aurora-security-group"
  vpc_id = "${aws_vpc.baseconnectvpc.id}"
  # vpc_id = "${aws_default_vpc.default.id}"

  ingress {
    protocol    = "tcp"
    from_port   = 3306
    to_port     = 3306
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    protocol    = -1
    from_port   = 0 
    to_port     = 0 
    cidr_blocks = ["0.0.0.0/0"]
  }
}