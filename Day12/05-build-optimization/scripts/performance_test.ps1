# PowerShell version of performance test script

Write-Host "🚀 Build Performance Testing" -ForegroundColor Green
Write-Host "=========================="

# Clean builds performance
Write-Host "Testing clean build performance..."
Measure-Command { ./gradlew clean build --no-build-cache }

# Cached builds performance  
Write-Host "Testing cached build performance..."
Measure-Command { ./gradlew clean build }

# Incremental builds performance
Write-Host "Testing incremental build performance..."
(Get-Item "modules/core/src/main/java/com/company/advanced/core/MathUtils.java").LastWriteTime = Get-Date
Measure-Command { ./gradlew build }

# Parallel vs sequential comparison
Write-Host "Testing parallel vs sequential builds..."
Write-Host "Parallel build:"
Measure-Command { ./gradlew clean build --parallel --max-workers=4 }

Write-Host "Sequential build:"
Measure-Command { ./gradlew clean build --no-parallel --max-workers=1 }

# Build scan analysis
Write-Host "Generating build scan for analysis..."
./gradlew build --scan

