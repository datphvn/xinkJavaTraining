# PowerShell version of Maven performance test script

Write-Host "🚀 Maven Build Performance Testing" -ForegroundColor Green
Write-Host "================================="

# Clean builds performance
Write-Host "Testing clean build performance..."
Measure-Command { mvn clean package -DskipTests }

# With tests
Write-Host "Testing build with tests..."
Measure-Command { mvn clean package }

# Parallel builds
Write-Host "Testing parallel builds..."
Measure-Command { mvn clean package -T 4 }

# Incremental builds
Write-Host "Testing incremental builds..."
(Get-Item "core/src/main/java/com/company/advanced/core/Calculator.java").LastWriteTime = Get-Date
Measure-Command { mvn package }

# Profile-specific builds
Write-Host "Testing profile builds..."
Measure-Command { mvn clean package -Pproduction -DskipTests }

