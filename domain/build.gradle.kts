plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.kotlin
}

group = "com.simple.discussion.domain"
version = "0.0.1"

sourceSets {
    getByName("main").java.srcDirs("src")
}

dependencies {
    implementation(Deps.Ktor.serialization)
}