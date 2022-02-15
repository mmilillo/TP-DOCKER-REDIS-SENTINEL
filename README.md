
 
# Contenedores y escalabilidad
 
## no-escalapp
 
Este repositorio inicia con una API desarrollada en Java y Spring Boot 2, con una base de datos acoplada, en memoria. La cual puede encontrarse en [`este repositorio`](https://github.com/distribuida2/no-escalapp). Esta API lo único que hace es demorar en responder y ocupar recursos del contenedor en donde vive.
 
 
### No escala?
 
Cada nuevo request generará una nueva carga en el CPU por un random de segundos. Al llegar a 100% de CPU, la aplicación empezará a responder con un error HTTP 500 de Internal Server Error.
 
### Y como la rompemos?
 
Dentro del repositorio de la misma tenemos las instrucciones para probar levantar la aplicación y probarla utilizando un script de Gatling.
 
 
## Solución
 
El objetivo de este trabajo es migrar la API no-escalapp a una solución escalable. Para ello se implementó la siguiente arquitectura.
 
![Diagrama](/documentacion/Diagrama.png "Diagrama de solución")
 
### Y como la probamos?
 
Al igual que la solución no escalable primero, levantamos el contenedor utilizando docker compose.
 
   docker-compose
 
Después, ejecutamos el script de Gatling que ya está configurado en el pom.xml de la siguiente manera
 
    ./mvnw gatling:test
 
Al terminar ese comando, nos indica el archivo donde se generó un reporte HTML con la información detallada de toda la corrida.
 
    Notar que si bien la prueba parece ser la misma que la ejecutada en la aplicación no-escalaApp en este caso se modificó el script de Gatling para que este "apunte" a la IP del load balancer, para que este distribuya la carga entre las 3 instancias de la API.
 
A continuación los resultados de las pruebas
 
![Resultado-pruebas](/documentacion/resultado-prueba-estres.png "resultado pruebas de estrés")
 
 
## Es tolerante a fallas?
 
Si, lo es, para probarlo basta con detener el contenedor en el cual se ejecuta la instancia master de la base de datos.
 
    docker stop redisDB-master
 
Al detener el contenedor los sentinels detectarán que la instancia master de la base de datos ya no está disponible y seleccionarán una de las réplicas como nueva instancia de master. Una vez realizada la selección informarán a las APIs la nueva IP con la cual deben comunicarse.
