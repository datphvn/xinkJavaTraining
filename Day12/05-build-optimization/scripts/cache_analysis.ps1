# PowerShell version of cache analysis script

Write-Host "📊 Build Cache Analysis" -ForegroundColor Cyan
Write-Host "====================="

# Cache statistics
$cacheDir = "$env:USERPROFILE\.gradle\caches\build-cache"
if (Test-Path $cacheDir) {
    $cacheSize = (Get-ChildItem -Path $cacheDir -Recurse | Measure-Object -Property Length -Sum).Sum
    Write-Host "Local cache directory size: $([math]::Round($cacheSize / 1MB, 2)) MB"
    
    $cacheEntries = (Get-ChildItem -Path $cacheDir -Recurse -Filter "*.tgz").Count
    Write-Host "Cache entries: $cacheEntries"
} else {
    Write-Host "Cache directory does not exist yet"
}

# Build with cache from clean
Write-Host "`nBuild with clean cache:"
Remove-Item -Path $cacheDir -Recurse -Force -ErrorAction SilentlyContinue
Measure-Command { ./gradlew clean build --build-cache }

Write-Host "`nBuild with warm cache:"
Measure-Command { ./gradlew clean build --build-cache }

# Cache effectiveness
Write-Host "`nCache hit rate analysis:"
./gradlew clean build --build-cache --info | Select-String -Pattern "FROM-CACHE|UP-TO-DATE"

