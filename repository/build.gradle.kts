plugins {
    kotlin("jvm")
}

group = "com.simple.discussion.repository"
version = "0.0.1"

sourceSets {
    getByName("main").java.srcDirs("src")
    getByName("test").java.srcDirs("test")
}

dependencies {
    implementation(project(":domain"))
    implementation(Deps.Kotlin.stdlibJvm)
}