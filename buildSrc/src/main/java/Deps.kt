object Versions {
    const val kotlin = "1.3.72"
    const val ktor = "1.3.2"
    const val exposed = "0.24.1"
}

object Deps {
    object Kotlin {
        const val stdlibJvm: String = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72"
    }

    object Ktor {
        const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
        const val logback = "ch.qos.logback:logback-classic:1.2.1"
        const val serialization = "io.ktor:ktor-serialization:${Versions.ktor}"
        const val koin = "org.koin:koin-ktor:2.1.6"
    }

    object Database {
        const val H2 = "com.h2database:h2:1.4.200"
    }

    object Exposed {
        const val core = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
        const val dao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
        const val jdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
    }

}
object TestDeps {
    object Ktor {
        const val serverTests = "io.ktor:ktor-server-tests:${Versions.ktor}"
        const val serverTestHost = "io.ktor:ktor-server-test-host:${Versions.ktor}"
    }

    const val truth = "com.google.truth:truth:1.0.1"
}
