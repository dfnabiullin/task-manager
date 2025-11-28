buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.15.0")
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "11.15.0"
}
val springCloudVersion by extra("2025.0.0")
group = "io.github.dfnabiullin.taskmanager"
version = "1.1.0"
description = "task-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs = listOf(
        "-javaagent:${classpath.find { it.name.contains("byte-buddy-agent") }?.absolutePath}"
    )
}

flyway {
    url = "jdbc:postgresql://localhost:5433/task_db"
    user = "postgres"
    password = "postgres"
}