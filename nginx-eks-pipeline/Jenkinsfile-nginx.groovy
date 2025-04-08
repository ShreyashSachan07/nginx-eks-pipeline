pipeline {
    agent any 
    tools {
        nodejs 'nodejs'
    }
    environment  {
        AWS_ACCOUNT_ID = credentials('ACCOUNT_ID')
        AWS_ECR_REPO_NAME = credentials('ECR_REPO1')
        AWS_DEFAULT_REGION = 'us-east-1'
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/"
    }
    stages {
        stage('Cleaning Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout from Git') {
            steps {
                git branch: 'main', credentialsId: 'GITHUB', url: 'https://github.com/ShreyashSachan07/nginx-eks-pipeline.git'
            }
        }
        
        stage("Docker Image Build") {
            steps {
                script {
                    dir('catalogue') {
                            sh 'docker system prune -f'
                            sh 'docker container prune -f'
                            sh 'docker build -t ${AWS_ECR_REPO_NAME} .'
                    }
                }
            }
        }

        stage('Sysdig Inline Scan') {
            steps {
                withCredentials([string(credentialsId: 'sysdig-api-token', variable: 'SYSDIG_API_TOKEN')]) {
                    sh '''
                    docker run --rm             -v /var/run/docker.sock:/var/run/docker.sock             -v $(pwd):/app             sysdiglabs/inline-scan:latest             --sysdig-url https://secure.sysdig.com             --api-token $SYSDIG_API_TOKEN             --input-image $REGISTRY/$IMAGE_NAME:$TAG             --fail-on-critical-vulns
                    '''
        }
      }
    }

        stage("ECR Image Pushing") {
            steps {
                script {
                        sh 'aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${REPOSITORY_URI}'
                        echo "docker tag ${AWS_ECR_REPO_NAME}${REPOSITORY_URI}${AWS_ECR_REPO_NAME}:${BUILD_NUMBER}"
                        sh 'docker tag ${AWS_ECR_REPO_NAME} ${REPOSITORY_URI}${AWS_ECR_REPO_NAME}:${BUILD_NUMBER}'
                        sh 'docker push ${REPOSITORY_URI}${AWS_ECR_REPO_NAME}:${BUILD_NUMBER}'
                }
            }
        }

    }}