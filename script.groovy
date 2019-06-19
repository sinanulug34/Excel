pipeline{
    agent{
        label 'LAB1-LIN1'
    }
}
stages{
    stage('Update Test Resources') {
        steps {
            script {
                cleanWs()
                git branch: 'master', credentialsId: 'GIT User', url: 'http://10.254.51.193:7990/scm/bo/bip-mobil.git'
            }
        }
    }
}