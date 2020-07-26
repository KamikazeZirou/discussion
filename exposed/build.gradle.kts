plugins {
    kotlin("jvm")
}

sourceSets {
    getByName("main").java.srcDirs("src")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":repository"))

    implementation(Deps.Database.H2)
    implementation(Deps.Exposed.core)
    implementation(Deps.Exposed.dao)
    implementation(Deps.Exposed.jdbc)
}