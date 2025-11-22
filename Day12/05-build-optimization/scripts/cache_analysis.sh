#!/bin/bash

echo "📊 Build Cache Analysis"
echo "====================="

# Cache statistics
echo "Local cache directory size:"
du -sh ~/.gradle/caches/build-cache/

echo "Cache entries:"
find ~/.gradle/caches/build-cache/ -name "*.tgz" | wc -l

# Build with cache from clean
echo "Build with clean cache:"
rm -rf ~/.gradle/caches/build-cache/
time ./gradlew clean build --build-cache

echo "Build with warm cache:"
time ./gradlew clean build --build-cache

# Cache effectiveness
echo "Cache hit rate analysis:"
./gradlew clean build --build-cache --info | grep -i cache | grep -E "(FROM-CACHE|UP-TO-DATE)"

