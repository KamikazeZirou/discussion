plugins {
    kotlin("jvm")
}

group = "com.simple.discussion.exposed"
version = "0.0.1"

sourceSets {
    getByName("main").java.srcDirs("src")
    getByName("test").java.srcDirs("test")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":repository"))

    implementation(Deps.Database.H2)
    implementation(Deps.Exposed.core)
    implementation(Deps.Exposed.dao)
    implementation(Deps.Exposed.jdbc)
}