plugins {
    java
    id("org.springframework.boot") version "2.1.0.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

group = "de.htwsaar.vs"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    implementation("org.apache.commons:commons-math3:3.6.1")
}
