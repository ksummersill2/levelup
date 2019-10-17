pipeline {
    agent { docker {
                        image 'docker.io/summersillk/acesdevtest:nodejsv1'
                        registryUrl 'https://docker.io'
                        alwaysPull true
                        registryCredentialsId 'd0faee3e-65b0-4ab7-83a1-b1709af66014'
                        args '-u root --privileged'
                    }
            }
    environment {
        location = '<ip address>'
        gitBranch = '<repository-branch>'
        repository = '<repository>'
        mainFolder = '<workspace-main-drectory>'
        planNm = '<wls_plan>'
        clusterNm = '<wls_cluster>'
        appNm = '<app_name>'
        earNm = '<ear_name>'
        jDeveloperWorkSpace = '<workspacefile>.jws'
        jDeveloperProfile = 'eodims'
        binaryBranch = '<branch>'
        binaryRepositoryBranch = '<branch>'
        logServer = '<servername>'
        }
        stages{
                stage('Prepare Docker Environment') {
                    steps {
                        // cleanWs()
                        echo '************CHECKING DOCKER JAVA VERSION********************'
                        sh 'java -version'
                        echo '************CHECKING DOCKER GIT VERSION*********************'
                        sh 'git --version'
                        echo '************CHECKING JDEVELOPER INSTALLATION****************'
                        sh 'cat /u01/app/oracle/middleware/logs/log.txt'
                    }
                }
                stage('Pulling Repository') {
                    steps {
                        echo '**************CONFIGURING GIT*********************************'
                        sh 'git config --global user.email <email address of repository>'
                        sh 'git config --global user.name "<username>"'
                        echo '**************REMOVING EXISTING REPOSITORY********************'
                        sh 'rm -Rf ${repository}'
                        echo '**************PULLING CODE FROM REPOSITORY********************'
                        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768b', 
                        usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                            sh 'git clone -b ${gitBranch} https://$USERNAME:$PASSWORD@bitbucket.di2e.net/scm/aces/${repository}.git'
                        }
                        sh 'ls $WORKSPACE/${repository}/Model/src/META-INF'
                        echo '*************CHECKING REPOSITORY LOCATION*********************'
                        sh 'readlink -f ${jDeveloperWorkSpace}'
                    }
                }
                stage('Compiling Node JS JST Application'){
                    steps {
                        sh 'npm config set registry http://registry.npmjs.org/'
                        sh 'npm install ./eodims-sustainment-app-nonjst/OperationsManagementVC/trainingModule --prefix ./eodims-sustainment-app-nonjst/OperationsManagementVC/trainingModule'
                        sh './eodims-sustainment-app-nonjst/OperationsManagementVC/trainingModule/node_modules/.bin/ng version'
                        sh 'npm run demo ./eodims-sustainment-app-nonjst/OperationsManagementVC/trainingModule --prefix ./eodims-sustainment-app-nonjst/OperationsManagementVC/trainingModule'
                    }
                }
                stage('Compiling Code'){
                    steps {
                        echo '*****************REVIEWING CONTENTS OF THE REPOSITORY******************'
                        sh 'ls $WORKSPACE/${repository}'
                        echo '*****************COMPILING APPLICATION CODE FROM REPOSITORY************'
                        sh 'ojdeploy -workspace $WORKSPACE/${repository}/${jDeveloperWorkSpace} -profile ${jDeveloperProfile}'
                        echo '*****************DEPLOYMENT FOLDER CONTENTS****************************'
                        sh 'ls $WORKSPACE/${repository}/deploy'
                    }
                }
                stage('Send Binary for Version Control'){
                    steps {
                        echo '***************SENDING BINARY TO BIT BUCKET REPOSTIROY**********************'
                        sh 'rm -Rf $WORKSPACE/${binaryRepositoryBranch}'
                        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768b', 
                        usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'git clone -b ${binaryBranch} https://$USERNAME:$PASSWORD@bitbucket.di2e.net/scm/aces/${binaryRepositoryBranch}.git'
                        sh 'cp  $WORKSPACE/${repository}/deploy/${earNm} $WORKSPACE/${binaryRepositoryBranch}/${earNm}'
                        sh 'ls  $WORKSPACE/${binaryRepositoryBranch}'
                        sh 'git --git-dir=$WORKSPACE/${binaryRepositoryBranch}/.git --work-tree=$WORKSPACE/${binaryRepositoryBranch}/ add .'
                        sh 'git --git-dir=$WORKSPACE/${binaryRepositoryBranch}/.git commit -m "CI Binary for EODIMS"'
                        sh 'git --git-dir=$WORKSPACE/${binaryRepositoryBranch}/.git push https://$USERNAME:$PASSWORD@bitbucket.di2e.net/scm/aces/${binaryRepositoryBranch}.git --force'
                        }
                    }
                }
                stage('Send EAR to Dev') {
                    steps {
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768a', 
                            usernameVariable: 'WLSUSERNAME', passwordVariable: 'WLSPASSWORD']]) {
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768f', 
                            usernameVariable: 'JKUSERNAME', passwordVariable: 'JKPASSWORD']]) {
                            echo '**************REVIEW EAR FILES ON DEVELOPMENT SERVER**********************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} ls /aces/install/ears'
                            echo '**************STOPING DEPLOYMENT******************************************'
                            // sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo java -cp /aces/app/oracle/product/Middleware/wlserver_10.3/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://localhost:7001 -username "${WLSUSERNAME}" -password "${WLSPASSWORD}" -name ${appNm} -stop /aces/install/ears/${earNm} -targets ${clusterNm}'
                            echo '**************UNDEPLOYING BY NAME******************************************'
                            // sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo java -cp /aces/app/oracle/product/Middleware/wlserver_10.3/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://localhost:7001 -username "${WLSUSERNAME}" -password "${WLSPASSWORD}" -name ${appNm} -undeploy -timeout 300 /aces/install/ears/${earNm} -targets ${clusterNm}'
                            echo '**************REMOVE EXISTING EAR*****************************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} rm /aces/install/ears/${earNm}'
                            echo '**************COPY EAR FILE OVER TO ACES/INSTALL/EARS LOCATION************'
                            sh 'sshpass -p "${JKPASSWORD}" scp $WORKSPACE/${repository}/deploy/${earNm} jenkins@${location}:/aces/install/ears'
                            echo '**************REMOVE EXISTING PLAN*****************************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} rm /aces/install/plan/${planNm}'
                            echo '**************COPY PLAN FROM REPOSITORY **********************************'
                            sh 'sshpass -p "${JKPASSWORD}" scp $WORKSPACE/${repository}/${planNm} jenkins@${location}:/aces/install/plan/${planNm}'
                            echo '**************CHANGE OWNERSHIP OF EAR*************************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo chown oracle:oinstall /aces/install/ears/${earNm}'
                            echo '**************CHANGE OWNERSHIP OF PLAN*************************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo chown oracle:oinstall /aces/install/plan/${planNm}'
                            echo '**************SET ENVIRONMENT**********************************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo /aces/app/oracle/product/Middleware/wlserver_10.3/server/bin/setWLSEnv.sh'
                            echo '**************STOP THE MANAGE SERVER*************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo /aces/app/oracle/product/Middleware/user_projects/domains/APPDomain/bin/stopManagedWebLogic.sh ${logServer}'
                            echo '**************STARTING THE MANAGE SERVER*********************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo nohup /aces/app/oracle/product/Middleware/user_projects/domains/APPDomain/bin/startManagedWebLogic.sh ${logServer} &'
                            echo '**************DEPLOY EAR FILE*********************************************'
                            sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} sudo java -cp /aces/app/oracle/product/Middleware/wlserver_10.3/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://localhost:7001 -username "${WLSUSERNAME}" -password "${WLSPASSWORD}" -name ${appNm} -deploy /aces/install/ears/${earNm} -plan /aces/install/plan/${planNm} -targets ${clusterNm}' 
                            }
                            }
                        }
                }
                stage('Send Out Log Files') {
                    steps {
                        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768a', 
                        usernameVariable: 'WLSUSERNAME', passwordVariable: 'WLSPASSWORD']]) {
                        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768f', 
                        usernameVariable: 'JKUSERNAME', passwordVariable: 'JKPASSWORD']]) {
                         sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} ls /aces/app/oracle/product/Middleware/user_projects/domains/APPDomain/servers/'
                         sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} ls /aces/app/oracle/product/Middleware/user_projects/domains/APPDomain/servers/${logServer}/logs'
                         sh 'rm -f  $WORKSPACE/${binaryRepositoryBranch}/${logServer}.out'
                         sh 'sshpass -p "${JKPASSWORD}" scp ${JKUSERNAME}@${location}:/aces/app/oracle/product/Middleware/user_projects/domains/APPDomain/servers/${logServer}/logs/${logServer}.out $WORKSPACE/${binaryRepositoryBranch}/'
                         sh 'git --git-dir=$WORKSPACE/${binaryRepositoryBranch}/.git --work-tree=$WORKSPACE/${binaryRepositoryBranch}/ add .'
                         sh 'git --git-dir=$WORKSPACE/${binaryRepositoryBranch}/.git commit -m "CI Binary Out logs for FESIMS"'
                         withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '2b27e14f-c065-49e6-ae1a-d8bf0962768b', 
                         usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                         sh 'git --git-dir=$WORKSPACE/${binaryRepositoryBranch}/.git push https://$USERNAME:$PASSWORD@bitbucket.di2e.net/scm/aces/${binaryRepositoryBranch}.git'
                         }
                         sh 'sshpass -p "${JKPASSWORD}" ssh -o StrictHostKeyChecking=no ${JKUSERNAME}@${location} tail -n 200 /aces/app/oracle/product/Middleware/user_projects/domains/APPDomain/servers/${logServer}/logs/${logServer}.out'
                        }
                            }
                        }
                }
        }
        post {
            always {
                echo 'Started Post Section'
                cleanWs()
            }
            success {
                echo 'Successfully Compiled and Deployed EODIMS EAR to Developement'
            }
            failure {
                mail bcc: '', body: "<b><Application Name> Failed Deployment to Dev Server</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "ksummersill@appddictionstudio.com";
            }
            unstable {
                echo 'The job ran unstable, please check!'
            }
            changed {
                echo 'The pipeline has changes status'
            }
        }
}