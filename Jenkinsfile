pipeline {
    agent {
        docker{
            image 'docker:latest'
            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    environment {
        DOCKER_REPOSITORY_AUTH = 'workeo/auth-service'
        DOCKER_REPOSITORY_SUBSCRIPTION = 'workeo/subscription-service'
        DOCKER_REPOSITORY_BILLING = 'workeo/billing-service'
    }
    stages {
        stage('Workeo CI/CD Pipeline') {
            parallel {
                stage('Auth-Microservice') {
                    agent {
                        docker{
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages {
                        stage('Setup') {
                            docker {
                                image 'golang:latest'
                            }
                            steps {
                                // Implicitly clones the repo when the first step runs
                                script {
                                    dir('auth-service') {
                                        // Optional: confirm that you're in the right directory
                                        sh 'ls'
                                    }
                                }
                            }
                        }
                        stage('Clean up and remove unnecessary dependencies') {
                            docker {
                                image 'golang:latest'
                            }
                            steps {
                                dir('auth-service') {
                                    sh 'GOCACHE=/tmp/go-cache go mod tidy'
                                }
                            }
                        }
                        stage('Test') {
                            docker {
                                image 'golang:latest'
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
                        stage('Push Docker image to Docker Hub') {
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
                        docker{
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                        
                    }
                    stages {
                        stage('Setup') {
                            docker {
                                image 'maven:3.9.9-amazoncorretto-21'
                                args '-v /var/jenkins_home/.m2:/root/.m2'
                            }
                            steps {
                                script {
                                    dir('subscription-service') {
                                        // Optional: confirm that you're in the right directory
                                        sh 'ls'
                                    }
                                }
                            }
                        }
                        stage('Test & Compile') {
                            docker {
                                image 'maven:3.9.9-amazoncorretto-21'
                                args '-v /var/jenkins_home/.m2:/root/.m2'
                            }
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn test compile'
                                }
                            }
                        }
                        stage('Clean up & Packaging .jar') {
                            docker {
                                image 'maven:3.9.9-amazoncorretto-21'
                                args '-v /var/jenkins_home/.m2:/root/.m2'
                            }
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn clean package'
                                }
                            }
                        }
                        stage('Building Docker Image') {
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
                        }
                    }
                }
                stage('Billing-Service') {
                    agent {
                        docker{
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                        
                    }
                    stages {
                        stage('Setup') {
                            agent{
                                docker {
                                    image 'python:3.10-slim'
                                }
                            }
                            steps {
                                script {
                                    dir('billing_service') {
                                        // Optional: confirm that you're in the right directory
                                        sh 'ls'
                                    }
                                }
                            }
                        }
                        stage('Test') {
                            agent{
                                docker {
                                    image 'python:3.10-slim'
                                }
                            }
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
                }
            }
        }
    }
}
