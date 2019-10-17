variable "cluster_name" {
  default = "baseConnect-cluster"
} 

variable "instance_class_mysql" {
  default = "db.t2.medium"
}

variable "instance_class_aurora" {
  default = "db.t2.medium"
}

variable "username" {
  default = "baseConnect"
}

variable "password" {
  default = "baseConnect!"
}

variable "basic_db_instance_class" {
  default = "db.t2.medium"
}

variable "basic_db_instance_class_scalability" {
  default = "db.t2.medium"
}

variable "basic_db_engine_version" {
  default = "5.7.26"
}

variable "basic_db_engine_type" {
  default = "mysql"
}
