# Nisum - API registro de usuarios

## Ejercicio
Proyecto Java SpringBoot de registro de usuarios.

* implementación de los diferentes verbos HTTP.
* pruebas unitarias en la capas controller y service (coverage 75%).
* JWT para la seguridad.

## Token de autenticación
El proyecto está segurizado mediante JWT. Para generar este token es necesario apuntar al endpoint de /login con las credenciales descritas en el apartado siguiente.

Una vez obtenido dicho token, debe ser ingresado como parte del request en el apartado "Authorization" de Postman, como Bearer Token.

## Roles y credenciales
Al levantar el proyecto se generan automaticamente, vía CommandLineRunner, los siguientes perfiles administrativos:

### ROLE_ADMIN
* username: evans
* password: undercurrent
* grants: GET, POST (ver y crear usuarios)

### ROLE_SUPERADMIN
* username: coltrane
* password: supreme
* grants: GET, POST, PUT, DELETE (ver, crear, modificar y borrar usuarios)

## Requests

#### POST /user-registry/login/
     curl -L 'http://localhost:8080/user-registry/login/' \
     -F 'username="bwayne"' \
     -F 'password="batman"'

####  GET /user-registry/
      curl -L 'http://localhost:8080/user-registry/'

####  POST /user-registry/
      curl -XPOST -H "Content-type: application/json" -d '{ "name":"Bruce Wayne","email":"batman@jl.com","password":"baticueva", "phones": [{ "number": "1234567", "citycode": "1", "contrycode": "57" }]}' 'http://localhost:8080/user-registry/'
