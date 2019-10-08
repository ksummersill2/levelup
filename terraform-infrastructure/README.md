# BaseConnect Terraform AWS Gov

## Setting up IAM Policies
Currently the application is running using an Administrative IAM. Make sure to have the same created and utilize the IAM secret and access ids to setup the below variables. 

## Setup Variables

Run terraform script by first setting up environment variables. 

In linux setup the Access Key and Secret Key with export and with Windows set. 
Example:

Windows
```
set TF_VAR_AWS_ACCESS_KEY=key
set TF_VAR_AWS_SECRET_KEY=key
```
Linux
```
export TF_VAR_AWS_ACCESS_KEY=key
export TF_VAR_AWS_SECRET_KEY=key
```
#Note: In order to setup a variable in Terraform from machine environment; then utilize TF_VAR and add the variable to the script. 

## Prepare and Execute Script

After variables are setup then run the following:
terraform init -> Setsup all the Terraform plugins and resources
terraform plan -> Verifies the setup is correct before running
terraform apply -> Executes the infrastructure within AWS. 

To destroy the resources then use:
terraform destory
