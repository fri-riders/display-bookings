# Display bookings service

`Last version: 0.5`

## IBM CLOUD

### Info
* Public IP: `http://169.51.17.191`

### Login instructions
0. 
- bx plugin install container-service -r Bluemix
- bx login -a https://api.eu-gb.bluemix.net 
2. bx cs cluster-config rso-cluster
3. export KUBECONFIG=/Users/ibm/.bluemix/plugins/container-service/clusters/rso-cluster/kube-config-par01-rso-cluster.yml
4. kubectl get nodes
5. kubectl proxy

## Docker compose
- Build app: mvn clean package
- Run: docker-compose up --build

## Endpoints
App is accessible on port 8080.

Example call: `{IP}:8080/v1/bookings`
### Bookings
* `GET: /v1/bookings` Returns list of all bookings
* `GET: /v1/bookings/{bookingId}` Returns booking with appropriate id
* `GET: /v1/bookings/{bookingId}/accommodation` Returns info about booking accommodation
* `GET: /v1/bookings/{bookingId}/user` Returns info about the user who has made booking with id `bookingId`
* `POST: /v1/bookings` Creates new booking
Booking example:
`{
    "id": 1,
    "idAccommodation": 1,
    "idUser": 1,
    "fromDate": 1508833089266,
    "toDate": 1508833089266
}`
* `DELETE: /v1/bookings/{bookingId}` Removes booking with `bookingId` from database

### Config
* `GET: /v1/config` Returns all configurable values
* `POST: /v1/config/bookingsInsert` Changes config value `bookings-insert`  (parameter is boolean)

### Health
* `GET: /health` Returns info about service health
* `POST: /v1/health-demo/healthy` Changes config value `healthy` (parameter is boolean)
* `POST: /v1/health-demo/load` Simulates heavy load with calculating n-th number of fibonacci sequence (parameter is integer) 

### Metrics
* `GET: /metrics` Returns metrics
   

   


  

     

   
