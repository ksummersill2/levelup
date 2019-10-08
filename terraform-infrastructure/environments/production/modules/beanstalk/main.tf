# Module used to configure the elastic beanstalk application
module "s3" {
  source = "../s3"
}

resource "aws_elastic_beanstalk_application" "bcbackendapp" {
  name        = "bcbackendapi"
  description = "baseconnect backend api server"
}

resource "aws_elastic_beanstalk_application_version" "bcbackendappver" {
  name        = "bcbackendappverlbl"
  application = "bcbackendapi"
  description = "application version created by terraform"
  bucket      = "${module.s3.s3_bucket_id}"
  key         = "${module.s3.s3_bucket_object_file_upload_id}"
}

resource "aws_iam_role" "baseconnectelbrole" {
  name = "baseconnectelbrole"

  assume_role_policy = <<EOF
{
  "Version": "2008-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

data "aws_iam_policy" "AWSElasticBeanstalkWebTier" {
  arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier"
}

resource "aws_iam_role_policy_attachment" "baseconnectroleatt" {
  role       = "${aws_iam_role.baseconnectelbrole.name}"
  policy_arn = "${data.aws_iam_policy.AWSElasticBeanstalkWebTier.arn}"
}

resource "aws_iam_instance_profile" "baseconnectintpro" {
  name = "baseconnectintpro"
  role = "${aws_iam_role.baseconnectelbrole.name}"
}

resource "aws_elastic_beanstalk_environment" "bcbackendenv" {
  name                = "bcbackendenv"
  application         = "${aws_elastic_beanstalk_application.bcbackendapp.name}"
  solution_stack_name = "64bit Amazon Linux 2018.03 v4.10.2 running Node.js"

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = "${aws_iam_instance_profile.baseconnectintpro.name}"
  }
}
