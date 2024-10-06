pipeline {
    agent none
    environment{
        DOCKER_REPOSITORY = 'malcomer/workeo'
    }
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
                                        $class: "GitSCM",
                                        branches: [[name: "*/main"]],
                                        userRemoteConfigs: [[url: "https://github.com/achrafhammi/Let-s-Work.git"]],
                                        extensions: [[$class: "SparseCheckoutPaths", sparseCheckoutPaths: [[path: "auth-service/"]]]]
                                    ])
                                    sh 'ls'
                                }
                            }
                        }
                        /*stage('Clean up and remove unnecessary dependences'){
                            steps{
                                dir('auth-service'){
                                    sh 'GOCACHE=/tmp/go-cache go mod tidy'
                                }
                            }
                        }
                        stage('Test') {
                            steps {
                                dir('auth-service') {
                                    sh 'GOCACHE=/tmp/go-cache go test ./...' 
                                }
                            }
                        }*/
                        stage('Building Docker Image') {
                            steps {
                                dir('auth-service') {
                                    sh 'whoami'
                                    //sh "docker build -t ${env.DOCKER_REPOSITORY}/auth-service:0.1 ." 
                                }
                            }
                        }
                        stage('Push Docker image to docker hub'){
                            steps{
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                    sh "docker push  ${env.DOCKER_REPOSITORY}/auth-service:0.1"
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
