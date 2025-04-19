plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("io.ktor.plugin") version "2.3.4"
    kotlin("plugin.serialization") version "1.9.22"
}

application {
    mainClass.set("com.muamalatgo.ApplicationKt") // or com.muamalatgo.ApplicationKt if you renamed package
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-netty:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}
