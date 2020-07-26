import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", Versions.kotlin))
    }
}

plugins {
    application
    kotlin("jvm") version Versions.kotlin
    // これを指定しておかないと、kotlinx.serializationは実行時にエラーになる
    kotlin("plugin.serialization") version Versions.kotlin
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
    getByName("test").resources.srcDirs("test/resources")
}

repositories {
    mavenLocal()
    jcenter()
}


dependencies {
    implementation(Deps.Kotlin.stdlibJvm)
    implementation(Deps.Ktor.serverNetty)
    implementation(Deps.Ktor.logback)

    implementation(Deps.Database.H2)

    implementation(Deps.Exposed.core)
    implementation(Deps.Exposed.dao)
    implementation(Deps.Exposed.jdbc)

    implementation(Deps.Ktor.serialization)

    implementation(Deps.Ktor.koin)

    testImplementation(TestDeps.Ktor.serverTests)
    testImplementation(TestDeps.Ktor.serverTestHost)
    testImplementation(TestDeps.truth)
}
