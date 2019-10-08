# MOdule used to configure the S3 bucket

resource "aws_s3_bucket" "backconnects3" {
  bucket = "baseconnectapi"
  acl    = "private"

  versioning {
    enabled = true
  }

  tags = {
    Name        = "baseconnectapi"
    Environment = "Prod"
  }
}
resource "aws_s3_bucket_object" "file_upload" {
  bucket = "${aws_s3_bucket.backconnects3.tags.Name}"
  key    = "nodejs-v1.zip"
  source = "nodejs-v1.zip"
}

output "s3_bucket_object_file_upload_id" {
  value = "${aws_s3_bucket_object.file_upload.id}"
}

output "s3_bucket_id" {
  value = "${aws_s3_bucket.backconnects3.id}"
}