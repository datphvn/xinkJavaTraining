# Build Optimization Project

This project demonstrates build optimization techniques for both Maven and Gradle.

## Gradle Optimization

### Configuration Files
- `gradle.properties` - Gradle build optimization settings
- `.gradle/init.d/build-cache.gradle` - Build cache configuration

### Performance Testing Scripts
- `scripts/performance_test.sh` - Linux/Mac performance testing
- `scripts/performance_test.ps1` - Windows PowerShell performance testing
- `scripts/cache_analysis.sh` - Cache effectiveness analysis

## Maven Optimization

### Configuration Files
- `.mvn/maven.config` - Maven JVM and build optimization settings

### Performance Testing Scripts
- `scripts/maven_performance_test.sh` - Linux/Mac Maven performance testing
- `scripts/maven_performance_test.ps1` - Windows PowerShell Maven performance testing

## Usage

### Gradle Performance Testing
```bash
# Linux/Mac
./scripts/performance_test.sh

# Windows PowerShell
.\scripts\performance_test.ps1
```

### Maven Performance Testing
```bash
# Linux/Mac
./scripts/maven_performance_test.sh

# Windows PowerShell
.\scripts\maven_performance_test.ps1
```

### Cache Analysis
```bash
# Linux/Mac
./scripts/cache_analysis.sh
```

## Key Optimizations

1. **JVM Settings**: Optimized heap size and garbage collection
2. **Parallel Builds**: Enable parallel execution for faster builds
3. **Build Cache**: Local and remote caching for faster incremental builds
4. **Daemon**: Keep Gradle daemon running for faster subsequent builds
5. **File System Watching**: Monitor file changes for faster builds

