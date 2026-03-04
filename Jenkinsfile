pipeline {
    agent any

    environment {
        // Nexus Config
        NEXUS_URL_INT = "192.168.1.101:8083"
        NEXUS_URL_PROD = "192.168.1.101:8084"
        NEXUS_CREDS = credentials('nexus-credentials-id')

        // VM Integration
        VM_INT_IP = "192.168.1.102"
        VM_INT_USER = "solucity_int"
        VM_INT_PASS = credentials('ssh-password-int-id')

        // VM Production
        VM_PROD_BACK_IP = "192.168.1.105"
        VM_PROD_FRONT_IP = "192.168.1.106"
        VM_PROD_USER_BACK = "solucity-back_prod"
        VM_PROD_USER_FRONT = "solucity-front_prod"
        VM_PROD_PASS = credentials('ssh-password-prod-id')
    }

    triggers {
        pollSCM('* * * * *') // // vérifie s'il y a un push toute les une minute et build si c'est oui

    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Push Docker') {
            steps {
                script {
                    def isProdTag = env.TAG_NAME?.startsWith("prod-")
                    def targetNexus = isProdTag ? NEXUS_URL_PROD : NEXUS_URL_INT
                    def imageTag = isProdTag ? env.TAG_NAME : "build-${BUILD_NUMBER}"

                    echo "Building images for ${targetNexus} with tag ${imageTag}"

                    sh "echo ${NEXUS_CREDS_PSW} | docker login ${targetNexus} -u ${NEXUS_CREDS_USR} --password-stdin"

                    // Build Backend
                    sh "docker build -t ${targetNexus}/backend:${imageTag} ./api"

                    // Build Frontend
                    sh "docker build -t ${targetNexus}/frontend:${imageTag} -f webapp/Dockerfile ."

                    // Push Docker images
                    sh "docker push ${targetNexus}/backend:${imageTag}"
                    sh "docker push ${targetNexus}/frontend:${imageTag}"
                }
            }
        }

        stage('Deploy to Integration') {
            when {
                allOf {
                    branch 'main'
                    expression { !env.TAG_NAME }
                }
            }
            steps {
                script {
                    deploySSH(VM_INT_IP, VM_INT_USER, VM_INT_PASS, NEXUS_URL_INT, "int", "all", "build-${BUILD_NUMBER}", true)
                }
            }
        }

        stage('Deploy to Production') {
            when {
                expression { env.TAG_NAME?.startsWith("prod-") }
            }
            steps {
                script {
                    // Backend Prod
                    deploySSH(VM_PROD_BACK_IP, VM_PROD_USER_BACK, VM_PROD_PASS, NEXUS_URL_PROD, "prod", "backend", env.TAG_NAME, false)
                    // Frontend Prod
                    deploySSH(VM_PROD_FRONT_IP, VM_PROD_USER_FRONT, VM_PROD_PASS, NEXUS_URL_PROD, "prod", "frontend", env.TAG_NAME, false)
                }
            }
        }
    }
}

// Fonction de déploiement SSH
// networkShared = true si en int
def deploySSH(ip, user, pass, nexusUrl, profile, type, version, networkShared) {
    sh """
        sshpass -p '${pass}' ssh -o StrictHostKeyChecking=no ${user}@${ip} '
            set -e

            echo "Login to Nexus"
            docker login ${nexusUrl} -u ${NEXUS_CREDS_USR} -p ${NEXUS_CREDS_PSW}

            if [ "${networkShared}" = "true" ]; then
                docker network create app-net || true
            fi

            if [ "${type}" == "backend" ] || [ "${type}" == "all" ]; then
                docker stop backend-${profile} || true
                docker rm backend-${profile} || true

                docker pull ${nexusUrl}/backend:${version}

                if [ "${networkShared}" = "true" ]; then
                    docker run -d --name backend-${profile} --hostname backend --network app-net -p 8080:8080 -e SPRING_PROFILES_ACTIVE=${profile} ${nexusUrl}/backend:${version}
                else
                    docker run -d --name backend-${profile} --hostname backend -p 8080:8080 -e SPRING_PROFILES_ACTIVE=${profile} ${nexusUrl}/backend:${version}
                fi
            fi

            if [ "${type}" == "frontend" ] || [ "${type}" == "all" ]; then
                docker stop frontend-${profile} || true
                docker rm frontend-${profile} || true

                docker pull ${nexusUrl}/frontend:${version}

                if [ "${networkShared}" = "true" ]; then
                    docker run -d --name frontend-${profile} --network app-net -p 80:80 ${nexusUrl}/frontend:${version}
                else
                    docker run -d --name frontend-${profile} --add-host=backend:${VM_PROD_BACK_IP} -p 80:80 ${nexusUrl}/frontend:${version}
                fi
            fi
        '
    """
}