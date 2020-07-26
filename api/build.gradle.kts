import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
}

group = "com.simple.discussion"
version = "0.0.1"

application {
    @Suppress("UnstableApiUsage")
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

sourceSets {
    getByName("main").java.srcDirs("src")
    getByName("main").resources.srcDirs("resources")
    getByName("test").java.srcDirs("test")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":repository"))
    implementation(project(":exposed"))

    implementation(Deps.Kotlin.stdlibJvm)
    implementation(Deps.Ktor.serverNetty)
    implementation(Deps.Ktor.logback)

    implementation(Deps.Ktor.serialization)

    implementation(Deps.Ktor.koin)

    testImplementation(TestDeps.Ktor.serverTests)
    testImplementation(TestDeps.Ktor.serverTestHost)
    testImplementation(TestDeps.truth)
}