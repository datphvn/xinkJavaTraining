# Dependency Resolution Examples

## Maven Dependency Analysis

### Analyze dependency tree
```bash
mvn dependency:tree -Dverbose
```

### Find dependency conflicts
```bash
mvn dependency:analyze
mvn dependency:analyze-duplicate
```

### Show effective POM
```bash
mvn help:effective-pom
```

### Resolve specific dependency conflicts
```bash
mvn dependency:tree -Dincludes=org.slf4j:*
```

### Example of exclusion in Maven POM
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>6.1.1</version>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## Gradle Dependency Analysis

### Show dependency tree
```bash
./gradlew dependencies
```

### Show dependency insights
```bash
./gradlew dependencyInsight --dependency slf4j-api
```

### Show configuration dependencies
```bash
./gradlew dependencies --configuration compileClasspath
```

### Find dependency conflicts
```bash
./gradlew buildEnvironment
```

### Resolution strategy in Gradle
```groovy
configurations.all {
    resolutionStrategy {
        // Force specific version
        force 'org.slf4j:slf4j-api:2.0.9'
        
        // Fail on version conflict
        failOnVersionConflict()
        
        // Prefer project modules
        preferProjectModules()
        
        // Cache dependency resolution
        cacheDynamicVersionsFor 10, 'minutes'
        cacheChangingModulesFor 4, 'hours'
        
        // Exclude transitive dependencies
        eachDependency { DependencyResolveDetails details ->
            if (details.requested.group == 'commons-logging') {
                details.useTarget 'org.slf4j:jcl-over-slf4j:2.0.9'
            }
        }
    }
}
```

