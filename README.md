# Gradle plugin - layer3gen
Generates the standard SPRING layered CRUD architecture starting from JPA entities.

1) repositories
2) services
3) controller

## How to:
##### 1- clone this repository and run
```shell script
gradlew install
```
##### 2- configure build.gradle as follow:
```groovy

buildscript {
	dependencies {
		classpath 'gae.piaz:layer3gen:0.0.6-SNAPSHOT'
	}
	repositories {
		mavenCentral()
		mavenLocal()
	}
}
  // ...
  // ...
apply plugin: 'gae.piaz.layer3gen'
layer3gen {
	configPath = 'src/main/resources/3layer-settings.yml'
}
```
##### 3- Create 3layer-settings.yml in your resource folder.
Example of 3layer-setting.yml :

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
##### 4- run 
```shell script
gradlew layer3gen
```

## Example:
To know what kind of structure will be generated check the example project which uses this repository in the /demo folder.
