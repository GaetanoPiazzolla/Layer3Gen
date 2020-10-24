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
