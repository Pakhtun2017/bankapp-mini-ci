pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh 'mvn -q clean package'
      }
    }

    stage('Docker Build') {
      steps {
        sh '''
        docker build -t bankapp/account:${GIT_COMMIT} -f Dockerfile.account .
        docker build -t bankapp/transaction:${GIT_COMMIT} -f Dockerfile.transaction .
        '''
      }
    }

    stage('Push Image') {
      steps {
        sh '''
        docker tag bankapp/account:${GIT_COMMIT} bankapp/account:latest
        docker tag bankapp/transaction:${GIT_COMMIT} bankapp/transaction:latest
        '''
      }
    }
  }
}

