pipeline {
  agent any

  tools {
    maven 'maven3'
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
                script {
                    withDockerRegistry(credentialsId: 'docker-cred') {
                                sh '''
						  docker build -t bankapp/account:${GIT_COMMIT} -f Dockerfile.account .
						  docker build -t bankapp/transaction:${GIT_COMMIT} -f Dockerfile.transaction .
						  '''
                    }
      		}
      }
    }

    stage('Push Image') {
      steps {
             script {
                    withDockerRegistry(credentialsId: 'docker-cred') {
                                sh '''
						     docker tag bankapp/account:${GIT_COMMIT} bankapp/account:latest
						     docker tag bankapp/transaction:${GIT_COMMIT} bankapp/transaction:latest
						    '''
                    }
      }
    }
  }
}

