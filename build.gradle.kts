import com.google.protobuf.gradle.*
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("org.jetbrains.kotlin.kapt") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.micronaut.application") version "1.5.4"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.32"
    id("com.google.protobuf") version "0.8.15"
}

version = "0.1"
group = "br.com.ane"
allOpen {
    annotation("javax.inject.Singleton")
}
val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

micronaut {
    testRuntime("junit5")
    processing {
        annotations("br.com.ane.*")
    }
}

dependencies {
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.grpc:micronaut-grpc-runtime")
    implementation("javax.annotation:javax.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("io.micronaut:micronaut-http-client")
    implementation(kotlin("stdlib-jdk8"))

    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    implementation("io.micronaut:micronaut-http-client:2.5.12")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.mockito:mockito-core:3.8.0")
    //testImplementation("io.micronaut.test:micronaut-test-junit5:2.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    //testImplementation("com.h2database:h2")
    testImplementation("com.h2database:h2:1.4.200")
    //JPA
    annotationProcessor("io.micronaut.data:micronaut-data-processor:3.0.0")
    //implementation("io.micronaut.data:micronaut-data-hibernate-jpa:2.5.0")
    implementation("io.micronaut.xml:micronaut-jackson-xml")



}


application {
    mainClass.set("br.com.ane.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"    }

    }


}
sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.33.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}
