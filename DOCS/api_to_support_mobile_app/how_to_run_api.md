## How to run all system in docker? 

### Instructions to run project (Backend + Database)

* 1.Install docker compose desktop: https://docs.docker.com/desktop/install/windows-install/
* 2.Build .jar to docker, so, go to BackEnd folder at ./docs/api_to_support_mobile_app/daw_code/jvm/battleship and run:
```
 ./gradlew bootJar   
```
* 3.Next go to code base folder at ./docs/api_to_support_mobile_app/daw_code
* 4.Start docker image with development time services and wait...
```
docker compose up --build --force-recreate 
```
* 6.Check in docker desktop if these containers are created:
```
be-batleship
db-batteship
```
* 6.Backend server is available at http://localhost:8080