# Module used to configure the databases/rds


# resource "aws_rds_cluster_instance" "cluster_instances" {
#   identifier         = "${var.cluster_name}-instance"
#   cluster_identifier = "${aws_rds_cluster.cluster.id}"
#   instance_class     = "${var.instance_class_aurora}"
# }

# resource "aws_rds_cluster" "cluster" {
#   cluster_identifier     = "${var.cluster_name}"
#   database_name          = "baseConnectAPI"
#   master_username        = "${var.username}"
#   master_password        = "${var.password}"
#   # vpc_security_group_ids = ["${aws_security_group.aurorasg.id}"]
#   skip_final_snapshot    = true
# }

resource "aws_db_instance" "baseConnectMysqlRDS" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "${var.basic_db_engine_type}"
#   engine_version       = "${var.basic_db_engine_version}"
  instance_class       = "${var.instance_class_mysql}"
  name                 = "mydb"
  username             = "baseConnect"
  password             = "baseConnect!"
  parameter_group_name = "default.mysql5.7"
  final_snapshot_identifier = "db-1-backup"
  skip_final_snapshot       = true
}

# Scalability Instance
resource "aws_db_instance" "baseConnectDBInstance" {
  instance_class       = "${var.basic_db_instance_class_scalability}"
  engine               = "${var.basic_db_engine_type}"
  username             = "baseConnect"
  password             = "baseConnect!"
  final_snapshot_identifier = "db-2-backup"
  allocated_storage     = 50
  max_allocated_storage = 100
}