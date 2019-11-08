
try {
   node {      
      stage('init'){       
           
      }
      stage('checkout'){              
         sh "mkdir source"
          dir("source"){
              git(
                  branch : "master",
                  credentialsId: "github",
                  url: "https://github.com/opando/aks-rbac-example.git"
              )
          }
      }
      stage('Build Container') {      
         
         
         withCredentials([string(credentialsId:"bleon-sp",variable:'azcre')]){
            //token : CLIENT_ID|CLIENT_SECRET|TENANT_ID|SUBSCRIPTION_ID

            spcre = azcre.split('\\|')
            clientId=spcredential[0]     
            clientse=spcredential[1] 
            tenantId=spcredential[2]
            subscrId=spcredential[3]

          sh = "docker build -t az-demo:1.0 --build-arg --build-arg CLIENT_ID=${clientId} --build-arg CLIENT_SECRET=${clientse} --build-arg TENANT_ID=${tenantId} --build-arg SUBSCRIPTION_ID=${subscrId}"


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

