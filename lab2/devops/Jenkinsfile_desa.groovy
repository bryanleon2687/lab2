
try {
   node {      
      stage('init'){       
           sh "printenv"
         cleanWs()
      }
      stage('checkout'){  
         checkout scm 


         sh "mkdir source"
          dir("source"){
              git(
                  branch : "master",
                  credentialsId: "github",
                  url: "https://github.com/opando/aks-rbac-example.git"
              )
          }
          sh "pwd && ls -lart"

      }
      stage('Build Container') {      
         
         
         withCredentials([string(credentialsId:"bleon-sp",variable:'azcre')]){
            //token : CLIENT_ID|CLIENT_SECRET|TENANT_ID|SUBSCRIPTION_ID

            spcredential = azcre.split('\\|')
            clientId=spcredential[0]     
            clientse=spcredential[1] 
            tenantId=spcredential[2]
            subscrId=spcredential[3]
            sh "cp lab2/docker/Dockerfile ."
            sh "ls -lta"

          sh = "docker build -t az-demo:1.0 --build-arg --build-arg CLIENT_ID=${clientId} --build-arg CLIENT_SECRET=${clientse} --build-arg TENANT_ID=${tenantId} --build-arg SUBSCRIPTION_ID=${subscrId} ."
         sh "docker images"

   }
   
} 
stage("Terraform init"){

   sh "docker run -v ${WORKSPACE}/source:/aks-rbac-example:/iac --name az-runarq az-demo:1.0 terraform init"
}
   }
}
catch(e) {
   node{   
      echo "-- exception --"
      throw e
   }
}
finally {
  node{ 
    echo "-- remove img --"

  }  
}

