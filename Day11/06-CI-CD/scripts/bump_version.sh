#!/bin/bash
# Automated Version Bumping
# Determines version bump type from commit messages

BUMP_TYPE="patch"

# Get commits since last tag
LAST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "v0.0.0")
COMMITS=$(git log $LAST_TAG..HEAD --pretty=format:"%s")

# Check for breaking changes
if echo "$COMMITS" | grep -q "BREAKING CHANGE\|feat!:\|fix!:"; then
    BUMP_TYPE="major"
elif echo "$COMMITS" | grep -q "^feat"; then
    BUMP_TYPE="minor"
fi

echo "Version bump type: $BUMP_TYPE"

# Get current version
CURRENT_VERSION=$(echo $LAST_TAG | sed 's/v//')
if [ "$CURRENT_VERSION" = "v0.0.0" ]; then
    CURRENT_VERSION="0.0.0"
fi

# Calculate new version
IFS='.' read -ra VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR=${VERSION_PARTS[0]}
MINOR=${VERSION_PARTS[1]}  
PATCH=${VERSION_PARTS[2]}

case $BUMP_TYPE in
    "major")
        MAJOR=$((MAJOR + 1))
        MINOR=0
        PATCH=0
        ;;
    "minor")
        MINOR=$((MINOR + 1))
        PATCH=0
        ;;
    "patch")
        PATCH=$((PATCH + 1))
        ;;
esac

NEW_VERSION="$MAJOR.$MINOR.$PATCH"

echo "New version: v$NEW_VERSION"

# Update version in files
if [ -f "pom.xml" ]; then
    sed -i "s/<version>.*<\/version>/<version>$NEW_VERSION<\/version>/" pom.xml
fi

if [ -f "package.json" ]; then
    sed -i "s/\"version\": \".*\"/\"version\": \"$NEW_VERSION\"/" package.json
fi

# Create git tag
git add .
git commit -m "chore: bump version to v$NEW_VERSION"
git tag -a "v$NEW_VERSION" -m "Release version $NEW_VERSION"

echo "Version bumped to v$NEW_VERSION"

