# Nisum - API registro de usuarios

## Ejercicio

### Requests
####  GET /
      curl -XGET 'http://localhost:8080/user-registry/'

####  POST /
      curl -XPOST -H "Content-type: application/json" -d '{ "name":"Bruce Wayne", "email":"batman@jl.com","password":"baticueva" }' 'localhost:8080/user-registry/'

