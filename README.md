# levelup
Level Up Code Submission

This repository contains the example of previous work(s) for technologies for: Docker, Jenkins, and Terraform. The Docker allows use to containerize the application which then allows use to move a sustainment application to the cloud without worrying about the environment/server that runs the application. The application can run on either the AWS Elastic Bean Stalk (ELB) or on an EC2 Virtual Machine from AWS. The Jenkins pipeline script is an example that Appddiction Studio has used to push code as part of a CI/CD pipeline. The application utilized a Jdeveloper IDE and oracle database (to check database objects) in order order to compile. The Pipeline utilizes a docker container with all the software required to compile code. The Terraform script is used to build an entire environment within AWS for an with ELB, EC2s (Bastion server), API Server, ELB (Workers), Security Groups, VPCs, Subnets, and etc. 

To deal with Docker scaleabiltiy the repository added Kubernetes, Knative (with Istio) as well as Tekton (Pipeline CI/CD scripting) to the repository. 
