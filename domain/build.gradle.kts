plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.kotlin
}

sourceSets {
    getByName("main").java.srcDirs("src")
}

dependencies {
    implementation(Deps.Ktor.serialization)
}