pipeline {
    agent none
    stages {
        stage('Workeo CI/CD Pipeline') {
            parallel {
                stage('Auth-Microservice') {
                    agent {
                        docker {
                            image 'alpine:latest'
                        }
                    }
                    steps {
                        dir('auth-service') {   // This needs to be inside steps block
                            sh 'cat /etc/os-release'
                        }
                    }
                }
/*                stage('Subscription-Service') {
                    agent {
                        docker {
                            image 'maven:3.6.3-jdk-21'
                        }
                    }
                    steps {
                        script{
                            sh 'whoami'
                        }
                        dir('subscription-service') {  // This needs to be inside steps block
                            sh 'java --version'
                            sh 'mvn --version'
                        }
                    }
                }*/
            }
        }
    }
}
