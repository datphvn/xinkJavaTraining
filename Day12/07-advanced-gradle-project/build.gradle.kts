plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("com.github.spotbugs") version "5.2.1" apply false
    id("jacoco")
    id("jacoco-report-aggregation")
}

group = "com.company.advanced"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
        testImplementation("org.mockito:mockito-core:5.7.0")
        testImplementation("org.assertj:assertj-core:3.24.2")
    }
    
    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
        
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
    
    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
    
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf(
            "-Xlint:all",
            "-parameters",
            "-Werror"
        ))
    }
}

// Project-specific configurations
configure(listOf(project(":core"))) {
    description = "Core utilities và shared functionality"
    
    dependencies {
        api("org.apache.commons:commons-lang3:3.14.0")
        api("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    }
}

configure(listOf(project(":api"))) {
    description = "API layer definitions"
    
    dependencies {
        api(project(":core"))
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
    }
}

configure(listOf(project(":web"))) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    
    description = "Web application"
    
    dependencies {
        implementation(project(":api"))
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.rest-assured:rest-assured:5.4.0")
    }
    
    tasks.bootJar {
        enabled = true
        archiveClassifier.set("")
    }
    
    tasks.jar {
        enabled = true
        archiveClassifier.set("plain")
    }
}

configure(listOf(project(":integration-tests"))) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    
    description = "Integration tests"
    
    dependencies {
        testImplementation(project(":web"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.testcontainers:junit-jupiter:1.19.3")
        testImplementation("org.testcontainers:postgresql:1.19.3")
        testImplementation("io.rest-assured:rest-assured:5.4.0")
    }
    
    tasks.jar { enabled = false }
    tasks.bootJar { enabled = false }
}

// Custom tasks
tasks.register("testAll") {
    dependsOn(subprojects.map { "${it.path}:test" })
    description = "Run all tests in all modules"
    group = "verification"
}

tasks.register<JacocoReport>("jacocoAggregateReport") {
    dependsOn(subprojects.map { "${it.path}:test" })
    
    additionalSourceDirs.from(subprojects.map { project ->
        project.the<SourceSetContainer>()["main"].allSource.srcDirs
    })
    sourceDirectories.from(subprojects.map { project ->
        project.the<SourceSetContainer>()["main"].allSource.srcDirs
    })
    classDirectories.from(subprojects.map { project ->
        project.the<SourceSetContainer>()["main"].output
    })
    executionData.from(project.fileTree(".") {
        include("**/build/jacoco/test.exec")
    })
    
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}

