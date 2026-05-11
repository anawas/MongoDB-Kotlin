plugins {
    kotlin("jvm") version "2.3.21"
    application
}

group = "abbts.ndsswe.dbn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Synchroner MongoDB-Kotlin-Treiber
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.4.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("MainKt")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}