pipeline {
  agent any

  tools {
    maven 'maven3'
    jdk 'jdk21'
  }

  environment {
    REGISTRY = 'docker.io'
    ACCOUNT_IMAGE = 'bankapp/account'
    TRANSACTION_IMAGE = 'bankapp/transaction'
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

    stage('Docker Push (commit tag only)') {
      steps {
        withDockerRegistry(credentialsId: 'docker-cred', url: "https://${REGISTRY}") {
          sh '''
            docker push ${ACCOUNT_IMAGE}:${IMAGE_TAG}
            docker push ${TRANSACTION_IMAGE}:${IMAGE_TAG}
          '''
        }
      }
    }
  }

  post {
    success {
      echo "CI successful for commit ${GIT_COMMIT}"
    }
    failure {
      echo "CI failed — merge blocked"
    }
  }
}

