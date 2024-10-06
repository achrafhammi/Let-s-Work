pipeline {
    agent none
    environment {
        DOCKER_REPOSITORY_AUTH = 'workeo/auth-service'
        DOCKER_REPOSITORY_SUBSCRIPTION = 'workeo/subscription-service'
        DOCKER_REPOSITORY_BILLING = 'workeo/billing-service'
    }
    stages {
        stage('Checkout Repository') {
            agent {
                docker {
                    image 'docker:latest'
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                // Clone the entire repository to a temporary location
                dir('workspace') {
                    checkout([
                        $class: "GitSCM",
                        branches: [[name: "*/main"]],
                        userRemoteConfigs: [[url: "https://github.com/achrafhammi/Let-s-Work.git"]],
                    ])
                }
            }
        }
        stage('Distribute Services') {
            steps {
                // Move the relevant directories to their respective locations
                sh 'mv workspace/auth-service ./'
                sh 'mv workspace/subscription-service ./'
                sh 'mv workspace/billing_service ./'
            }
        }
        stage('Workeo CI/CD Pipeline') {
            parallel {
                stage('Auth-Microservice') {
                    agent {
                        docker {
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages {
                        stage('Clean up and remove unnecessary dependencies') {
                            agent {
                                docker {
                                    image 'golang:latest'
                                }
                            }
                            steps {
                                dir('auth-service') {
                                    sh 'GOCACHE=/tmp/go-cache go mod tidy'
                                }
                            }
                        }
                        stage('Test') {
                            agent {
                                docker {
                                    image 'golang:latest'
                                }
                            }
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
                        stage('Push Docker image to docker hub') {
                            steps {
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                    sh "docker push ${env.DOCKER_REPOSITORY_AUTH}:0.1"
                                }
                            }
                        }
                    }
                }
                stage('Subscription-Service') {
                    agent {
                        docker {
                            image 'maven:3.9.9-amazoncorretto-21'
                            args '-v /var/jenkins_home/.m2:/root/.m2'
                        }
                    }
                    stages {
                        stage('Test & Compile') {
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
                        stage('Building Docker image') {
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
                                    sh "docker push ${env.DOCKER_REPOSITORY_SUBSCRIPTION}:0.1"
                                }
                            }
                        }
                    }
                }
                stage("Billing-Service") {
                    agent {
                        docker {
                            image 'python:3.10-slim'
                        }
                    }
                    stages {
                        stage('Test') {
                            steps {
                                dir('billing_service') {
                                    sh 'pip install -r requirements.txt'
                                    sh 'python manage.py test'
                                }
                            }
                        }
                        stage("Build Docker image") {
                            steps {
                                dir('billing_service') {
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_BILLING}:0.1 ."
                                }
                            }
                        }
                        stage("Push Docker image") {
                            steps {
                                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                    sh "docker push ${env.DOCKER_REPOSITORY_BILLING}:0.1"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
