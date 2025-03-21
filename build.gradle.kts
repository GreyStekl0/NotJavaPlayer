plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.beatunes.com/repo/maven2")
    }
}

application {
    mainClass.set("MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

tasks.shadowJar {
    archiveBaseName.set("NotJavaPlayer")
    archiveVersion.set("1.0")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org:jaudiotagger:2.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("com.googlecode.soundlibs:basicplayer:3.0.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation(platform("org.junit:junit-bom:5.12.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.mockk:mockk:1.13.17")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}
