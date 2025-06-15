plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}

group = "com.m3z0id.informer"
version = "1.0"
description = "A plugin that shows you info about a player, like alts or client."

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
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
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks {
    compileJava {
        options.release = 21
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

tasks.assemble {
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}