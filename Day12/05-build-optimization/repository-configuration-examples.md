# Repository Configuration Examples

## Maven Repository Configuration

### Example POM with repositories
```xml
<project>
    <!-- Repositories for dependencies -->
    <repositories>
        <repository>
            <id>central</id>
            <name>Maven Central</name>
            <url>https://repo1.maven.org/maven2</url>
            <layout>default</layout>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
        
        <repository>
            <id>company-releases</id>
            <name>Company Release Repository</name>
            <url>https://nexus.company.com/repository/maven-public/</url>
            <releases>
                <enabled>true</enabled>
                <updatePolicy>never</updatePolicy>
                <checksumPolicy>warn</checksumPolicy>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
        
        <repository>
            <id>company-snapshots</id>
            <name>Company Snapshot Repository</name>
            <url>https://nexus.company.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>daily</updatePolicy>
                <checksumPolicy>warn</checksumPolicy>
            </snapshots>
        </repository>
    </repositories>
    
    <!-- Plugin repositories -->
    <pluginRepositories>
        <pluginRepository>
            <id>central</id>
            <name>Maven Central</name>
            <url>https://repo1.maven.org/maven2</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>
</project>
```

## Gradle Repository Configuration

### Example build.gradle with repositories
```groovy
repositories {
    // Maven Central with content filtering
    mavenCentral {
        content {
            // Only use Maven Central for Apache libraries
            includeGroupByRegex "org\\.apache\\..*"
            // Exclude Spring libraries from Maven Central
            excludeGroupByRegex "org\\.springframework\\..*"
        }
    }
    
    // Company Nexus repository
    maven {
        name = "CompanyNexus"
        url = "https://nexus.company.com/repository/maven-public/"
        
        credentials {
            username = findProperty("nexusUsername") ?: System.getenv("NEXUS_USERNAME")
            password = findProperty("nexusPassword") ?: System.getenv("NEXUS_PASSWORD")
        }
        
        authentication {
            basic(BasicAuthentication)
        }
        
        content {
            // Only fetch company artifacts
            includeGroupByRegex "com\\.company\\..*"
        }
    }
    
    // Snapshot repository with conditional access
    if (version.endsWith('-SNAPSHOT')) {
        maven {
            name = "SnapshotRepo"
            url = "https://nexus.company.com/repository/maven-snapshots/"
            credentials {
                username = findProperty("nexusUsername") ?: System.getenv("NEXUS_USERNAME")
                password = findProperty("nexusPassword") ?: System.getenv("NEXUS_PASSWORD")
            }
        }
    }
    
    // Gradle Plugin Portal
    gradlePluginPortal {
        content {
            // Only use for plugins
            includeGroupByRegex ".*\\.gradle\\.plugin"
        }
    }
    
    // JitPack for GitHub dependencies
    maven {
        name = "JitPack"
        url = "https://jitpack.io"
        content {
            includeGroupByRegex "com\\.github\\..*"
        }
    }
}

// Repository order matters - first match wins
repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
```

