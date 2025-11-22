#!/bin/bash
# Generate Changelog from Git Commits
# Creates CHANGELOG.md from commit history
# Can be used in any Git repository

LAST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "")
OUTPUT_FILE="CHANGELOG.md"

echo "# Changelog" > $OUTPUT_FILE
echo "" >> $OUTPUT_FILE

if [ -z "$LAST_TAG" ]; then
    # No previous tags, get all commits
    COMMITS=$(git log --pretty=format:"%h|%s|%an|%ad" --date=short)
else
    # Get commits since last tag
    COMMITS=$(git log $LAST_TAG..HEAD --pretty=format:"%h|%s|%an|%ad" --date=short)
fi

# Group commits by type
declare -A COMMIT_TYPES
COMMIT_TYPES[feat]="### ✨ Features"
COMMIT_TYPES[fix]="### 🐛 Bug Fixes"
COMMIT_TYPES[docs]="### 📚 Documentation"  
COMMIT_TYPES[style]="### 💎 Styles"
COMMIT_TYPES[refactor]="### 🔨 Code Refactoring"
COMMIT_TYPES[test]="### 🚨 Tests"
COMMIT_TYPES[chore]="### 🔧 Chores"

# Process commits
for type in feat fix docs style refactor test chore; do
    SECTION_COMMITS=$(echo "$COMMITS" | grep "^[a-f0-9]*|$type")
    
    if [ ! -z "$SECTION_COMMITS" ]; then
        echo "${COMMIT_TYPES[$type]}" >> $OUTPUT_FILE
        echo "" >> $OUTPUT_FILE
        
        echo "$SECTION_COMMITS" | while IFS='|' read -r hash message author date; do
            # Remove type prefix from message
            clean_message=$(echo "$message" | sed "s/^$type[(:][^:)]*[):] *//" | sed 's/^./\U&/')
            echo "- $clean_message ([$hash](../../commit/$hash)) - $author" >> $OUTPUT_FILE
        done
        
        echo "" >> $OUTPUT_FILE
    fi
done

echo "Generated changelog: $OUTPUT_FILE"

