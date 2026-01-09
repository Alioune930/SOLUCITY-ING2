pipeline {
    agent any

    environment {
        NEXUS_URL = "192.168.1.101:8083"
        NEXUS_CREDS = credentials('nexus-credentials-id')
        VM_INT_IP = "192.168.1.102"
        VM_INT_USER = "solucity_int"
        VM_INT_PASS = credentials('ssh-password-int-id') // SSH password
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Push Nexus') {
            steps {
                script {
                    sh "echo ${NEXUS_CREDS_PSW} | docker login ${NEXUS_URL} -u ${NEXUS_CREDS_USR} --password-stdin"

                    def tag = "main-${BUILD_NUMBER}"

                    sh "docker build -t ${NEXUS_URL}/backend:${tag} ./api"
                    sh "docker build -t ${NEXUS_URL}/frontend:${tag} -f webapp/Dockerfile ."

                    sh "docker push ${NEXUS_URL}/backend:${tag}"
                    sh "docker push ${NEXUS_URL}/frontend:${tag}"

                    sh "docker tag ${NEXUS_URL}/backend:${tag} ${NEXUS_URL}/backend:main-latest"
                    sh "docker tag ${NEXUS_URL}/frontend:${tag} ${NEXUS_URL}/frontend:main-latest"
                    sh "docker push ${NEXUS_URL}/backend:main-latest"
                    sh "docker push ${NEXUS_URL}/frontend:main-latest"
                }
            }
        }

        stage('Deploy to Integration') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh """
                        sshpass -p '${VM_INT_PASS}' ssh -o StrictHostKeyChecking=no ${VM_INT_USER}@${VM_INT_IP} '
                            docker login ${NEXUS_URL} -u ${NEXUS_CREDS_USR} -p ${NEXUS_CREDS_PSW}

                            docker stop api-int web-int || true
                            docker rm api-int web-int || true

                            docker pull ${NEXUS_URL}/backend:main-latest
                            docker pull ${NEXUS_URL}/frontend:main-latest

                            docker network create app-net || true

                            docker run -d --name api-int --network app-net -p 8080:8080 ${NEXUS_URL}/backend:main-latest
                            docker run -d --name web-int --network app-net -p 80:80 ${NEXUS_URL}/frontend:main-latest
                        '
                    """
                }
            }
        }
    }
}
