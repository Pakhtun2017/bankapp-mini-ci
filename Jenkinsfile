pipeline {
  agent any

  tools {
    maven 'maven3'
  }

  environment {
    // Image names
    ACCOUNT_IMAGE = 'oolumee/account'
    TRANSACTION_IMAGE = 'oolumee/transaction'

    // GitOps repo
    GITOPS_REPO = 'https://github.com/Pakhtun2017/bankapp-mini-gitops.git'
    GITOPS_BRANCH = 'main'
    DEV_PATH = 'dev'

    IMAGE_TAG = "${GIT_COMMIT}"
  }

  stages {

    stage('Build account-service') {
      steps {
        dir('account-service') {
          sh 'mvn -q clean package'
        }
      }
    }

    stage('Build transaction-service') {
      steps {
        dir('transaction-service') {
          sh 'mvn -q clean package'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh '''
          docker build -t ${ACCOUNT_IMAGE}:${IMAGE_TAG} -f Dockerfile.account .
          docker build -t ${TRANSACTION_IMAGE}:${IMAGE_TAG} -f Dockerfile.transaction .
        '''
      }
    }

    stage('Docker Push') {
      steps {
        withDockerRegistry(credentialsId: 'docker-cred', url: 'https://index.docker.io/v1/') {
          sh '''
            docker push ${ACCOUNT_IMAGE}:${IMAGE_TAG}
            docker push ${TRANSACTION_IMAGE}:${IMAGE_TAG}
          '''
        }
      }
    }

    stage('Extract Image Digests') {
      steps {
        sh '''
          ACCOUNT_DIGEST=$(docker inspect --format='{{index .RepoDigests 0}}' ${ACCOUNT_IMAGE}:${IMAGE_TAG})
          TRANSACTION_DIGEST=$(docker inspect --format='{{index .RepoDigests 0}}' ${TRANSACTION_IMAGE}:${IMAGE_TAG})

          echo "ACCOUNT_DIGEST=${ACCOUNT_DIGEST}" > digests.env
          echo "TRANSACTION_DIGEST=${TRANSACTION_DIGEST}" >> digests.env
        '''
        archiveArtifacts artifacts: 'digests.env'
      }
    }

    stage('Update GitOps DEV') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'git-cred',
          usernameVariable: 'GIT_USER',
          passwordVariable: 'GIT_TOKEN'
        )]) {
          sh '''
            set -e

            source digests.env

            git clone https://${GIT_USER}:${GIT_TOKEN}@github.com/Pakhtun2017/bankapp-mini-gitops.git
            cd bankapp-mini-gitops
            git checkout ${GITOPS_BRANCH}

            sed -i "s|image: .*account.*|image: ${ACCOUNT_DIGEST}|" ${DEV_PATH}/account-deployment.yaml
            sed -i "s|image: .*transaction.*|image: ${TRANSACTION_DIGEST}|" ${DEV_PATH}/transaction-deployment.yaml

            git config user.email "jenkins@ci.local"
            git config user.name "jenkins-ci"

            git add ${DEV_PATH}
            git commit -m "dev: deploy images for ${IMAGE_TAG}"
            git push origin ${GITOPS_BRANCH}
          '''
        }
      }
    }
  }

  post {
    success {
      echo "CI complete — GitOps DEV updated. ArgoCD will deploy."
    }
    failure {
      echo "CI failed — GitOps not touched."
    }
  }
}

