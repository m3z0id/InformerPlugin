plugins {
    `java-library`
}

group = "com.m3z0id.gunGame"
version = "1.0"
description = "A plugin that hhows you info about a player, like alts or client."

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks {
    compileJava {
        options.release = 17
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props =
            mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
            )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") { expand(props) }
    }
}