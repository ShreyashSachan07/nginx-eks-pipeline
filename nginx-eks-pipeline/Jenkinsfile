
// pipeline {
//   agent any

//   environment {
//     IMAGE_NAME = "nginx-demo"
//     REGISTRY = "ghcr.io/yourusername"
//     TAG = "latest"
//   }

//   options {
//     timestamps()
//   }

//   stages {
//     stage('Clone Repo') {
//       steps {
//         checkout scm
//       }
//     }

//     stage('Build Docker Image') {
//       steps {
//         sh 'docker build -t $IMAGE_NAME:$TAG .'
//       }
//     }

//     stage('Tag for GHCR') {
//       steps {
//         sh 'docker tag $IMAGE_NAME:$TAG $REGISTRY/$IMAGE_NAME:$TAG'
//       }
//     }

//     stage('Sysdig Inline Scan') {
//       steps {
//         withCredentials([string(credentialsId: 'sysdig-api-token', variable: 'SYSDIG_API_TOKEN')]) {
//           sh '''
//           docker run --rm             -v /var/run/docker.sock:/var/run/docker.sock             -v $(pwd):/app             sysdiglabs/inline-scan:latest             --sysdig-url https://secure.sysdig.com             --api-token $SYSDIG_API_TOKEN             --input-image $REGISTRY/$IMAGE_NAME:$TAG             --fail-on-critical-vulns
//           '''
//         }
//       }
//     }

//     stage('Push to GHCR') {
//       steps {
//         withCredentials([usernamePassword(credentialsId: 'ghcr-creds', usernameVariable: 'GHCR_USER', passwordVariable: 'GHCR_PAT')]) {
//           sh '''
//           echo "$GHCR_PAT" | docker login ghcr.io -u "$GHCR_USER" --password-stdin
//           docker push $REGISTRY/$IMAGE_NAME:$TAG
//           '''
//         }
//       }
//     }

//     stage('Deploy to EKS') {
//       steps {
//         sh 'kubectl apply -f k8s/nginx-deployment.yaml'
//       }
//     }
//   }

//   post {
//     failure {
//       echo "🔴 Build or scan failed. Investigate and fix before redeploying."
//     }
//     success {
//       echo "✅ Successfully deployed nginx to EKS with security scan passed!"
//     }
//   }
// }
