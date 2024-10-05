pipeline{
    agent none
    stages{
        stage('Workeo CI/CD Pipeline'){
            parallel{
                stage("Auth-Microservice"){
                    agent {
                        docker {
                            image 'alpine:latest'
                        }
                        dir('auth-service'){
                            steps{
                                sh 'cat /etc/os-release'
                            }
                        }
                    }
                }
                stage("Subscription-Service"){
                    agent{
                        docker{
                            image 'maven:3.6.3-jdk-21'
                        }
                        dir('subscription-service'){
                            steps{
                                sh 'java --version'
                                sh 'mvn --version'
                            }
                        }
                    }
                }
            }
        }
    }
}