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
                    stages {
                        dir('auth-service') { 
                            stage('Checkout'){
                                steps{
                                    checkout([
                                            $class: 'GitSCM', 
                                            branches: [[name: '*/main']], 
                                            userRemoteConfigs: [[url: 'https://github.com/achrafhammi/Let-s-Work.git']],
                                            extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: 'auth-service/']]]]
                                        ])
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
