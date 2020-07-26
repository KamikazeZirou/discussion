plugins {
    kotlin("jvm")
}

sourceSets {
    getByName("main").java.srcDirs("src")
}

dependencies {
    implementation(project(":domain"))
    implementation(Deps.Kotlin.stdlibJvm)
}