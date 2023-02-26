
buildscript {
    dependencies {
        classpath("gae.piaz:layer3gen:1.9")
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.0.0"
}
apply(plugin = "io.spring.dependency-management")
apply(plugin = "gae.piaz.layer3gen")

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.spring.io/milestone")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation( "org.springframework.boot:spring-boot-starter-web")
    implementation ("org.mapstruct:mapstruct:1.5.3.Final")
    compileOnly( "org.projectlombok:lombok:1.18.26")
    runtimeOnly 'com.h2database:h2'
    annotationProcessor( "org.projectlombok:lombok:1.18.26")
    annotationProcessor( "org.mapstruct:mapstruct-processor:1.5.3.Final")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_19
