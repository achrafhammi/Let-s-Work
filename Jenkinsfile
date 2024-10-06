pipeline {
    agent none
    environment{
        DOCKER_REPOSITORY_AUTH = 'workeo/auth-service'
        DOCKER_REPOSITORY_SUBSCRIPTION = 'workeo/subscription-service'
    }
    stages {
        stage('Workeo CI/CD Pipeline') {
            parallel {
                /*stage('Auth-Microservice') {
                    agent {
                        docker {
                            image 'golang:latest'
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'

                        }
                    }
                    stages {
                        stage('Checkout') {
                            steps {
                                dir('auth-service') {
                                    checkout([
                                        $class: "GitSCM",
                                        branches: [[name: "-----main"]],
                                        userRemoteConfigs: [[url: "https://github.com/achrafhammi/Let-s-Work.git"]],
                                        extensions: [[$class: "SparseCheckoutPaths", sparseCheckoutPaths: [[path: "auth-service/"]]]]
                                    ])
                                }
                            }
                        }
                        stage('Clean up and remove unnecessary dependences'){
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
                        }
                        stage('Building Docker Image') {
                            steps {
                                dir('auth-service') {
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_AUTH}:0.1 ." 
                                }
                            }
                        }
                        stage('Push Docker image to docker hub'){
                            steps{
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD docker.io "
                                    sh "docker push ${env.DOCKER_REPOSITORY_AUTH}:0.1"
                                }

                            }
                        }
                        
                    }
                }*/
                stage('Subscription-Service') {
                    agent {
                        docker {
                            image 'maven:3.9.9-amazoncorretto-21'
                            args '-v /var/jenkins_home/.m2:/root/.m2'
                        }
                    }
                    environment{
                        MAVEN_OPTS='-Dmaven.repo.local=/var/jenkins_home/.m2/repository'
                    }
                    stages{
                        stage('Checkout') {
                            steps {
                                dir('subscription-service') {
                                    checkout([
                                        $class: "GitSCM",
                                        branches: [[name: "*/main"]],
                                        userRemoteConfigs: [[url: "https://github.com/achrafhammi/Let-s-Work.git"]],
                                        extensions: [[$class: "SparseCheckoutPaths", sparseCheckoutPaths: [[path: "subscription-service/"]]]]
                                    ])
                                }
                            }
                        }
                        stage('Clean up and remove unnecessary dependences'){
                            steps{
                                dir('subscription-service'){
                                    sh 'mvn clean'
                                }
                            }
                        }
                        stage('Test & Compile') {
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn test compile'     
                                }
                            }
                        }
                        stage('Packaging .jar') {
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn clean package'
                                }
                            }
                        }
                        stage('Building docker image') {
                            steps {
                                dir('subscription-service') {
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_SUBSCRIPTION}:0.1 ."
                                }
                            }
                        }
                        stage('Pushing docker image') {
                            steps {    
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                        sh "docker push ${env.DOCKER_REPOSITORY_AUTH}:0.1"
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}

