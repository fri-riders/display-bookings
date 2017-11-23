#Display bookings service

IBM CLOUD
-
0. 
- bx plugin install container-service -r Bluemix
- bx login -a https://api.eu-gb.bluemix.net 
2. bx cs cluster-config rso-cluster
3. export KUBECONFIG=/Users/ibm/.bluemix/plugins/container-service/clusters/rso-cluster/kube-config-par01-rso-cluster.yml
4. kubectl get nodes
5. kubectl proxy

Docker compose
-
- Build app: mvn clean package
- Run: docker-compose up --build

App is accessible on port 8080.

(Example call: [IP]:8080/v1/bookings)
   

   


  

     

   
