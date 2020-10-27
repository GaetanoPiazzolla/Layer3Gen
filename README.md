# springboot-3layer-generator
Generates the standard SPRING layered architecture providing only JPA entities

* repositories
* services
* controller
* mapper ( entity - dto )
* controller-dto

The generated classes require those libraries in pom.xml or build.gradle: 

* 'org.springframework.boot:spring-boot-starter-data-jpa' // repositories
* 'org.springframework.boot:spring-boot-starter-web' // controller
* 'org.mapstruct:mapstruct' // mapper
* 'org.projectlombok:lombok' // autogen-utils

Run with:

**java -jar springboot-3layer-generator.jar settings.yml**

Example of settings.yml :

```yml
projectPath: /home/tano/workspace_autogenerate/springboot-3layer-generator
outputDirectory : /src/main/java
options:
    dtoLayer : true

inputPackages:
    jpaEntities : com.gae.piaz.autogen.model

outputPackages:
    repositories : com.gae.piaz.autogen.repositorygen
    services: com.gae.piaz.autogen.servicegen
    controllers: com.gae.piaz.autogen.controllergen
  
```
