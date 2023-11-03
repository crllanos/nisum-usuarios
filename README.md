# Nisum - API registro de usuarios

## Ejercicio

### Requests

#### POST /user-registry/login/
     curl -L 'http://localhost:8080/user-registry/login/' \
     -F 'username="bwayne"' \
     -F 'password="batman"'

####  GET /user-registry/
      curl -L 'http://localhost:8080/user-registry/'

####  POST /user-registry/
      curl -XPOST -H "Content-type: application/json" -d '{ "name":"Bruce Wayne","email":"batman@jl.com","password":"baticueva", "phones": [{ "number": "1234567", "citycode": "1", "contrycode": "57" }]}' 'http://localhost:8080/user-registry/'
