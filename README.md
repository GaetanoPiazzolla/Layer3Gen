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

**java -jar springboot-3layer-generator.jar settings.json**

Example of settings.json :

```javascript
{
  "projectPath": "/home/tano/workspace_autogenerate/autogen",

  "options": {
    "dto-layer" : true
  },

  "input-packages": {
    "jpa-entities" : "com.gae.piaz.autogen.model"
  },

  "output-packages": {
    "repositories" : "com.gae.piaz.autogen.repository",
    "services": "com.gae.piaz.autogen.service",
    "controllers": "com.gae.piaz.autogen.controller"
  }
}
```
