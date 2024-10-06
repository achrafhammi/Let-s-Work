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
                                    sh 'git clone https://github.com/achrafhammi/Let-s-Work.git /tmp/let-s-work'
                                    sh 'mv /tmp/let-s-work/auth-service .'
                                    sh 'rm -rf /tmp/let-s-work'
                                }
                            }
                        }
                        stage('Clean up and remove unnecessary dependences'){
                            steps{
                                sh 'ls'
                                sh 'go mod tidy'
                            }
                        }
                        stage('Test') {
                            steps {
                                sh 'GOCACHE=/tmp/go-cache go test ./...' 
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
