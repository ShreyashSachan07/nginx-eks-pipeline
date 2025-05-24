# NGINX + EKS + Jenkins + Sysdig Secure

This repo contains a complete pipeline to:
- Build and scan a Dockerized NGINX image
- Push it to GitHub Container Registry (GHCR)
- Deploy to AWS EKS
- Secure the image and runtime with Sysdig Secure

## 🔧 Tech Stack
- Jenkins (CI/CD)
- GHCR (Docker registry)
- AWS EKS (Kubernetes)
- Sysdig Secure (Image scanning + Runtime)

## 🚀 Quick Start

1. Clone the repo
2. Add Jenkins credentials:
   - `sysdig-api-token`: Sysdig API token
   - `ghcr-creds`: GitHub username + PAT
3. Push changes to trigger pipeline

## 🛡️ Security
Image is scanned inline using Sysdig before pushing. Build fails on high/critical vulnerabilities with fixes.

## 📦 Registry
Target: `ghcr.io/<yourusername>/nginx-demo:latest`
