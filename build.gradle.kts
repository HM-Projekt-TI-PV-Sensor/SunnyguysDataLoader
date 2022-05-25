import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
    java
}

group = "com.sunnyguys"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    implementation("org.postgresql:postgresql:42.3.5")
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks {
    shadowJar {
        archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
        relocate("org.postgresql", "com.sunnyguys.postgres")
        relocate("com.google", "com.sunnyguys.google")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

application {
    mainClass.set("MainKt")
}