pipeline {
    agent none // No global agent, we'll specify agent per stage
    environment {
        DOCKER_REPOSITORY_AUTH = 'workeo/auth-service'
        DOCKER_REPOSITORY_SUBSCRIPTION = 'workeo/subscription-service'
        DOCKER_REPOSITORY_BILLING = 'workeo/billing-service'
    }
    stages {
        stage('Workeo CI/CD Pipeline') {
            parallel {
                /*stage('Auth-Microservice') {
                    agent {
                        docker {
                            image 'golang:latest'
                        }
                    }
                    stages {
                        stage('Setup') {
                            steps {
                                dir('auth-service') {
                                    sh 'ls'
                                }
                            }
                        }
                        stage('Clean up and remove unnecessary dependencies') {
                            steps {
                                dir('auth-service') {
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
                        stage('Push Docker image to Docker Hub') {
                            steps {
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
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
                            args '-u root -v /var/jenkins_home/.m2:/root/.m2'
                        }
                    }
                    environment{
                        MAVEN_OPTS='-Dmaven.repo.local=/var/jenkins_home/.m2/repository'
                    }
                    stages {
                        stage('Setup') {
                            steps {
                                dir('subscription-service') {
                                    sh 'ls'
                                }
                            }
                        }
                        /*stage('Test & Compile') {
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn test compile'
                                }
                            }
                        }
                        stage('Clean up & Packaging .jar') {
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn clean package'
                                }
                            }
                        }
                        /*stage('Building Docker Image') {
                            steps {
                                dir('subscription-service') {
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_SUBSCRIPTION}:0.1 ."
                                }
                            }
                        }
                        stage('Pushing Docker image') {
                            steps {
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                    sh "docker push ${env.DOCKER_REPOSITORY_SUBSCRIPTION}:0.1"
                                }
                            }
                        }*/
                    }
                }
                /*stage('Billing-Service') {
                    agent {
                        docker {
                            image 'python:3.10-slim'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages {
                        stage('Setup') {
                            steps {
                                dir('billing_service') {
                                    sh 'ls'
                                }
                            }
                        }
                        stage('Test') {
                            steps {
                                dir('billing_service') {
                                    sh 'pip install -r requirements.txt'
                                    sh 'python manage.py test'
                                }
                            }
                        }
                        stage('Build Docker Image') {
                            steps {
                                dir('billing_service') {
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_BILLING}:0.1 ."
                                }
                            }
                        }
                        stage('Push Docker image') {
                            steps {
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                    sh "docker push ${env.DOCKER_REPOSITORY_BILLING}:0.1"
                                }
                            }
                        }
                    }
                }*/
            }
        }
        stage("tesssti"){
            agent{
                docker{
                    image 'docker:latest'
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps{
                sh 'ls'
            }
        }
    }
}
