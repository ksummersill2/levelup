#!/bin/bash  
environment=$1
task=$2
if [ $environment == "prod" ] && [ $task == "start" ]
then
    echo "Argument = $1"
    echo "Running Infrastructure for Production"
    echo "Initializing and Starting the Plan for Development"
    terraform init ./environments/production/
    terraform plan ./environments/production/
elif [ $environment == "dev" ] && [ $task == "start" ]
then
    echo "Argument = $1"
    echo "Running Infrastructure for Development"
    terraform init ./environments/development/
    terraform plan ./environments/development/
elif [ $environment == "qa" ] && [ $task == "start" ]
then
    echo "Argument = $1"
    echo "Running Infrastructure for Quality Assurance/Staging"
    terraform init ./environments/staging/
    terraform plan ./environments/staging/
fi
if [ $environment == "prod" ] && [ $task == "destroy" ]
then
    echo "Argument = $1"
    echo "Running Infrastructure for Production"
    echo "Initializing and Starting the Plan for Development"
    terraform destroy ./environments/production/
elif [ $environment == "dev" ] && [ $task == "destroy" ]
then
    echo "Argument = $1"
    echo "Running Infrastructure for Development"
    terraform destroy ./environments/development/
elif [ $environment == "qa" ] && [ $task == "destroy" ]
then
    echo "Argument = $1"
    echo "Running Infrastructure for Quality Assurance/Staging"
    terraform destroy ./environments/staging/
fi