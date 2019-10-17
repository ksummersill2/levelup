# About the Docker File

This docker file is used to configure an Image that will contain all the needed tools to do the following for a legacy applciation:
Pull the repository with DI2E BitBucket using Git
Scan the code utilizing SonarQube Static Code Analizer
Compile the code utilizing JDeveloper 11.0.2.4 
    OJDeploy apart of Jdeveloper is used to compile. OJDeploy requires a .jws (JDeveloper Workspace File) and a profile within the .jws file to start the compile process. 
Once compiled the ear file the file is SCP to the On Prem backroom server. Based on the variables. 
The weblogic server is then executed to removed older files and deploy the new ear file based upon the Web Logic Plan. 
The Web Logic Plan is initiated depending upon the environment such as: CI, Staging, and PreProd. 
After deployment is successful the binary is sent to Nexus 3 within DI2E for Vunerability Scanning. 

Legacy Application is:
Oracle ADF Application
Runs on JDK 1.6 (The docker container was configured to have Jdeveloper utilize the containers JDK so that i can be easily changed and configured to an updated JDK to help remove POA&Ms)
Oracle Web Logic Server 10.3.6 is installed to work with Oracle Forms and Reports (Legacy)
Oracle Forms and Reports (Installed to compile the forms application)
Oracle 12c DB (Installed as a requirement in order to comiple forms as to check database schema changes)

