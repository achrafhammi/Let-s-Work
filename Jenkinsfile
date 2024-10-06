pipeline {
    agent none
    stages {
        stage('Workeo CI/CD Pipeline') {
            parallel {
                stage('Auth-Microservice') {
                    agent {
                        docker {
                            image 'golang:latest'
                        }
                    }
                    stages {
                        stage('Checkout') {
                            steps {
                                dir('auth-service') {
                                    checkout([
                                        $class: 'GitSCM',
                                        branches: [[name: '*/main']],
                                        userRemoteConfigs: [[url: 'https://github.com/achrafhammi/Let-s-Work.git']],
                                        extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: 'auth-service/']]]]
                                    ])
                                    sh 'ls'
                                }
                            }
                        }
                        stage('Clean up and remove unnecessary dependences'){
                            steps{
                                sh 'pwd'
                            }
                        }
                        stage('Test') {
                            steps {
                                dir('auth-service') {
                                    sh 'GOCACHE=/tmp/go-cache go test ./...' 
                                }
                            }
                        }
                    }
                }
/*                stage('Subscription-Service') {
                    agent {
                        docker {
                            image 'maven:3.9.9-amazoncorretto-21'
                        }
                    }
                    steps {
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
