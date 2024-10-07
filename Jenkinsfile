pipeline {
    agent none 
    environment {
        DOCKER_REPOSITORY_AUTH = 'workeo/auth-service'
        DOCKER_REPOSITORY_SUBSCRIPTION = 'workeo/subscription-service'
        DOCKER_REPOSITORY_BILLING = 'workeo/billing-service'
    }
    stages {
        stage('Continuous Integration') {
            parallel {
                stage('Auth-Microservice') {
                    agent {
                        docker {
                            image 'golang:latest'
                        }
                    }
                    stages {
                        stage('Setup Project'){
                            steps{
                                dir('auth-service'){
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
                        stage("Install necessary dependencies"){
                            steps{
                                dir('aut-service'){
                                    sh 'GOCACHE=/tmp/go-cache go mod download'
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
                    }
                }
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
                        stage('Setup Project'){
                            steps{
                                dir('subscription-service'){
                                    sh 'ls'
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
                        stage('Clean up & Packaging .jar') {
                            steps {
                                dir('subscription-service') {
                                    sh 'mvn clean package'
                                }
                            }
                        }
                    }
                }
                stage('Billing-Service') {
                    agent {
                        docker {
                            image 'python:3.10-slim'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages {
                        stage('Setup Project'){
                            steps{
                                dir('billing_service'){
                                    sh 'ls'
                                }
                            }
                        }
                        stage('Install pip-tools'){
                            steps{
                                dir('billing_service'){
                                    sh 'pip install pip-tools'
                                }
                            }
                        }
                        stage('Clean up and Sync dependencies'){
                            steps{
                                dir('billing_service'){
                                    sh 'pip-compile --output-file=requirements.txt requirements.in' 
                                    sh 'pip-sync'
                                }
                            }
                        }
                        stage("install necessary dependencies"){
                            steps {
                                dir('billing_service') {
                                    sh 'pip install -r requirements.txt'
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
                    }
                }
            }
        }
        stage("Containerization"){
            parallel{
                stage('Auth-Microservice'){
                    agent{
                        docker{
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages{
                        stage('Building docker image'){
                            steps{
                                dir('auth-service'){
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_AUTH}:0.1 ."
                                }
                            }
                        }
                        stage('Pushing docker image to repo'){
                            steps{
                                dir('auth-service'){
                                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                            sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                            sh "docker push ${env.DOCKER_REPOSITORY_AUTH}:0.1"
                                        }
                                }
                            }
                    
                        }
                    }
                }
                stage('Subscription-Microservice'){
                    agent{
                        docker{
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages{
                        stage("Building Docker Image"){
                            steps{
                                dir('subscription-service'){
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_SUBSCRIPTION}:0.1 ."
                                }
                            }
                        }
                        stage("Pushing Docker Image"){
                            steps{
                                dir('subscription-service'){
                                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                            sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                            sh "docker push ${env.DOCKER_REPOSITORY_SUBSCRIPTION}:0.1"
                                        }
                                }
                            }
                        }
                    }
                }
                stage('Billing-Service'){
                    agent{
                        docker{
                            image 'docker:latest'
                            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    stages{
                        stage("Building docker image"){
                            steps{
                                dir('billing_service'){
                                    sh "docker build -t ${env.DOCKER_REPOSITORY_BILLING}:0.1 ."
                                }
                            }
                        }
                        stage("Pushing docker image"){
                            steps{
                                dir('billing_service'){
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
}
