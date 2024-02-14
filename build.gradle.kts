import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer

plugins {
    id("java")
    id("checkstyle")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2" // to create a jar with the dependencies
}

group = "sd"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.typesafe.akka:akka-actor-typed_2.13:2.8.0") // principal actors implementation
    implementation("com.typesafe.akka:akka-cluster-typed_2.13:2.8.0") // cluster support
    implementation("com.typesafe.akka:akka-cluster-sharding-typed_2.13:2.8.0") // for scaling and find actors by id
    implementation("org.slf4j:slf4j-api:1.7.32") // main logger
    implementation("ch.qos.logback:logback-classic:1.4.12") // logger helper
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("sd.Main")
}

tasks.jar {
    archiveBaseName.set("The_Game_without_dependencies") // specify why the jar doesn't work
    manifest {
        attributes["Main-Class"] = "sd.Main"
    }
}

tasks.shadowJar {
    archiveBaseName.set("The_Game")
    archiveClassifier.set("") // to have only "The_Game" as name of the jar
    manifest {
        attributes["Main-Class"] = "sd.Main"
    }

    // essential to have a functioning jar with all the dependencies
    val newTransformer = AppendingTransformer()
    newTransformer.resource = "reference.conf"
    // used to concatenate reference.conf with application.conf and to have all the configurations in the jar
    transformers.add(newTransformer)
}

tasks.wrapper {
    gradleVersion = "7.3"
    distributionType = Wrapper.DistributionType.ALL
}

checkstyle {
    toolVersion = "8.37"
    configFile = file("${rootProject.projectDir}/config/checkstyle/checkstyle.xml")
    isIgnoreFailures = true
    isShowViolations = true
}