import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "me.hbogaeus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:4.3.0")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.jdbi:jdbi3-core:3.27.0")
    implementation("org.flywaydb:flyway-core:8.4.1")
    implementation("org.xerial:sqlite-jdbc:3.36.0.2")
    implementation("org.jdbi:jdbi3-kotlin:3.24.1")
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("ch.qos.logback:logback-core:1.2.10")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
    implementation("com.konghq:unirest-java:3.13.6")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation(kotlin("test"))
}

tasks.shadowJar {
    manifest {
        attributes(Pair("Main-Class", "me.hbogaeus.Main"))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}