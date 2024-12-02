@Library('jenkins-private') _

pipeline {
    agent any

    environment {
        MEND_EMAIL = 'Your_Mend_Email'
        MEND_URL = 'https://saas.whitesourcesoftware.com/'     // Mend 平台 URL
        MEND_USER_KEY = 'Your_MEND_USER_KEY'
    }

    tools {
        maven 'Maven-3.9.6'
        jdk 'JDK17'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Git Clone') {
            steps {
                checkout scm: [
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'Your_GitHub_Url', 
                        credentialsId: 'Your_Credential_ID' // 這裡填入你的憑證 ID
                    ]]
                ]
            }
        }
        // Build the application with your required package manager.  The below example is for maven: ###
        stage('Install dependencies') {
            steps {
                dir('Your_File_Name') {
                    sh 'mvn install -DskipTests'
                }
            }
        }

        stage('Download Mend CLI') {
            steps {
               DownloadMendCLI()
            }
        }
        
        stage('Run Mend SCA') {
            steps {
                echo "Reachability is enabled"
                MendSCAScan(reachability: true)
                echo "Reachability is disabled"
                MendSCAScan(reachability: false)
            }
        }
        stage('Run SCA Reports') {
            steps {
               GenerateSCAReports()
            }
        }
    }
}
