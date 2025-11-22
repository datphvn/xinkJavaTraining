#!/bin/bash
# Exercise 3: Git Automation Suite
# Create comprehensive Git automation

echo "=== Exercise 3: Git Automation Suite ==="

WORK_DIR="git-automation-suite"
if [ -d "$WORK_DIR" ]; then
    echo "Directory already exists. Removing..."
    rm -rf "$WORK_DIR"
fi

mkdir "$WORK_DIR"
cd "$WORK_DIR"

echo ""
echo "=== Task 3.1: Advanced Git hooks suite ==="

git init
git config user.name "Automation User"
git config user.email "automation@example.com"

HOOKS_DIR=".git/hooks"
mkdir -p $HOOKS_DIR

# Create sophisticated pre-commit hook
cat > $HOOKS_DIR/pre-commit << 'HOOK_EOF'
#!/bin/bash
set -e

echo "🔍 Running comprehensive pre-commit checks..."

# Function to print colored output
print_status() {
    if [ "$1" = "SUCCESS" ]; then
        echo "✅ $2"
    elif [ "$1" = "WARNING" ]; then  
        echo "⚠️  $2"
    elif [ "$1" = "ERROR" ]; then
        echo "❌ $2"
    else
        echo "ℹ️  $2"
    fi
}

# Check 1: Validate commit message format
COMMIT_MSG_FILE=".git/COMMIT_EDITMSG"
if [ -f "$COMMIT_MSG_FILE" ]; then
    COMMIT_MSG=$(cat "$COMMIT_MSG_FILE")
    if ! echo "$COMMIT_MSG" | grep -qE '^(feat|fix|docs|style|refactor|test|chore|ci)(\(.+\))?: .{1,50}'; then
        print_status "ERROR" "Invalid commit message format"
        echo "Expected format: type(scope): description"
        echo "Types: feat, fix, docs, style, refactor, test, chore, ci"
        exit 1
    fi
fi

# Check 2: Code quality checks
print_status "INFO" "Running code quality checks..."

# Check for debug statements
if git diff --cached --name-only | grep -E '\.(java|js|py|rb)$' | xargs grep -l "console\.log\|System\.out\.println\|debugger\|pdb\.set_trace" 2>/dev/null; then
    print_status "WARNING" "Debug statements found in staged files"
    read -p "Continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Check 3: Security checks  
print_status "INFO" "Running security checks..."

# Check for potential secrets
SECRET_PATTERNS="password|secret|key|token|api_key|private_key"
if git diff --cached --name-only | xargs grep -iE "$SECRET_PATTERNS" 2>/dev/null | grep -v "test\|spec\|README"; then
    print_status "ERROR" "Potential secrets found in staged files"
    git diff --cached --name-only | xargs grep -iE "$SECRET_PATTERNS" 2>/dev/null | grep -v "test\|spec\|README"
    exit 1
fi

# Check 4: File size limits
print_status "INFO" "Checking file sizes..."

large_files=$(git diff --cached --name-only | xargs ls -la 2>/dev/null | awk '$5 > 1048576 {print $9 " (" $5 " bytes)"}')
if [ ! -z "$large_files" ]; then
    print_status "WARNING" "Large files detected (>1MB):"
    echo "$large_files"
    read -p "Continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1  
    fi
fi

# Check 5: Run tests if available
if [ -f "package.json" ] && [ -f "node_modules/.bin/jest" ]; then
    print_status "INFO" "Running JavaScript tests..."
    npm test -- --watchAll=false --silent
elif [ -f "pom.xml" ]; then
    print_status "INFO" "Running Maven tests..."
    mvn test -q
elif [ -f "build.gradle" ]; then
    print_status "INFO" "Running Gradle tests..."  
    ./gradlew test --quiet
fi

print_status "SUCCESS" "All pre-commit checks passed!"
HOOK_EOF

chmod +x $HOOKS_DIR/pre-commit
echo "✅ Created comprehensive pre-commit hook"

echo ""
echo "=== Task 3.2: Automated branching script ==="

mkdir -p scripts

cat > scripts/smart_branch.sh << 'SCRIPT_EOF'
#!/bin/bash
# Smart branch creation script
create_branch() {
    BRANCH_TYPE=$1
    TICKET_ID=$2
    DESCRIPTION=$3
    
    if [ -z "$BRANCH_TYPE" ] || [ -z "$TICKET_ID" ] || [ -z "$DESCRIPTION" ]; then
        echo "Usage: $0 <type> <ticket-id> <description>"
        echo "Types: feature, bugfix, hotfix, release"
        echo "Example: $0 feature PROJ-123 'user authentication'"
        exit 1
    fi
    
    # Generate branch name
    BRANCH_NAME="${BRANCH_TYPE}/${TICKET_ID}-${DESCRIPTION// /-}"
    BRANCH_NAME=$(echo "$BRANCH_NAME" | tr '[:upper:]' '[:lower:]')
    
    # Ensure we're on the right base branch
    case $BRANCH_TYPE in
        "feature"|"bugfix")
            BASE_BRANCH="develop"
            ;;
        "hotfix")
            BASE_BRANCH="main"
            ;;
        "release")
            BASE_BRANCH="develop"
            ;;
        *)
            echo "Unknown branch type: $BRANCH_TYPE"
            exit 1
            ;;
    esac
    
    # Switch to base branch and update
    echo "Switching to $BASE_BRANCH and updating..."
    git checkout $BASE_BRANCH 2>/dev/null || git checkout -b $BASE_BRANCH
    git pull origin $BASE_BRANCH 2>/dev/null || echo "No remote configured"
    
    # Create and switch to new branch
    echo "Creating branch: $BRANCH_NAME"
    git checkout -b "$BRANCH_NAME"
    
    # Create initial commit with branch info
    cat > BRANCH_INFO.md << EOL
# Branch Information

**Type**: $BRANCH_TYPE  
**Ticket**: $TICKET_ID
**Description**: $DESCRIPTION
**Base Branch**: $BASE_BRANCH  
**Created**: $(date)

## Checklist

- [ ] Implementation complete
- [ ] Tests written  
- [ ] Documentation updated
- [ ] Code reviewed
- [ ] Ready for merge
EOL
    git add BRANCH_INFO.md
    git commit -m "chore: initialize $BRANCH_TYPE branch for $TICKET_ID"
    
    echo "Branch $BRANCH_NAME created successfully!"
    echo "Don't forget to push: git push -u origin $BRANCH_NAME"
}

create_branch "$@"
SCRIPT_EOF

chmod +x scripts/smart_branch.sh
echo "✅ Created smart branch script"

echo ""
echo "=== Task 3.3: Release automation ==="

cat > scripts/release.sh << 'RELEASE_EOF'  
#!/bin/bash
# Automated release script
perform_release() {
    VERSION=$1
    
    if [ -z "$VERSION" ]; then
        echo "Usage: $0 <version>"
        echo "Example: $0 1.2.0"
        exit 1
    fi
    
    # Validate version format
    if ! echo "$VERSION" | grep -qE '^[0-9]+\.[0-9]+\.[0-9]+$'; then
        echo "Invalid version format. Use semantic versioning (e.g., 1.2.0)"
        exit 1
    fi
    
    echo "🚀 Starting release process for version $VERSION"
    
    # Step 1: Ensure we're on develop branch
    CURRENT_BRANCH=$(git branch --show-current)
    if [ "$CURRENT_BRANCH" != "develop" ]; then
        echo "❌ Must be on develop branch to create release"
        exit 1
    fi
    
    # Step 2: Ensure working directory is clean
    if ! git diff-index --quiet HEAD --; then
        echo "❌ Working directory must be clean"
        exit 1
    fi
    
    # Step 3: Update from remote
    echo "📡 Updating from remote..."
    git pull origin develop 2>/dev/null || echo "No remote configured"
    
    # Step 4: Create release branch
    RELEASE_BRANCH="release/v$VERSION"
    echo "🌿 Creating release branch: $RELEASE_BRANCH"
    git checkout -b "$RELEASE_BRANCH"
    
    # Step 5: Update version in files
    echo "📝 Updating version files..."
    if [ -f "pom.xml" ]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            sed -i '' "s/<version>.*<\/version>/<version>$VERSION<\/version>/" pom.xml
        else
            sed -i "s/<version>.*<\/version>/<version>$VERSION<\/version>/" pom.xml
        fi
    fi
    
    if [ -f "package.json" ]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            sed -i '' "s/\"version\": \".*\"/\"version\": \"$VERSION\"/" package.json
        else
            sed -i "s/\"version\": \".*\"/\"version\": \"$VERSION\"/" package.json
        fi
    fi
    
    if [ -f "gradle.properties" ]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            sed -i '' "s/version=.*/version=$VERSION/" gradle.properties
        else
            sed -i "s/version=.*/version=$VERSION/" gradle.properties
        fi
    fi
    
    # Step 6: Generate changelog (if script exists)
    if [ -f "scripts/generate_changelog.sh" ]; then
        echo "📜 Generating changelog..."
        bash scripts/generate_changelog.sh
    fi
    
    # Step 7: Commit version changes
    git add .
    git commit -m "chore: prepare release v$VERSION"
    
    # Step 8: Merge to main
    echo "🔀 Merging to main branch..."
    git checkout main 2>/dev/null || git checkout -b main
    git pull origin main 2>/dev/null || echo "No remote configured"
    git merge --no-ff "$RELEASE_BRANCH" -m "Release v$VERSION"
    
    # Step 9: Create tag
    echo "🏷️  Creating release tag..."
    git tag -a "v$VERSION" -m "Release version $VERSION"
    
    # Step 10: Merge back to develop
    echo "🔄 Merging back to develop..."
    git checkout develop
    git merge --no-ff "$RELEASE_BRANCH" -m "Merge release v$VERSION back to develop"
    
    # Step 11: Clean up release branch
    git branch -d "$RELEASE_BRANCH"
    
    # Step 12: Push everything
    echo "📤 Pushing to remote..."
    git push origin main 2>/dev/null || echo "No remote configured"
    git push origin develop 2>/dev/null || echo "No remote configured"
    git push origin "v$VERSION" 2>/dev/null || echo "No remote configured"
    
    echo "✅ Release v$VERSION completed successfully!"
    echo "🎉 Don't forget to create a GitHub release and deploy to production"
}

perform_release "$@"
RELEASE_EOF

chmod +x scripts/release.sh
echo "✅ Created release automation script"

# Create initial develop branch
git checkout -b develop
echo "Initial setup" > README.md
git add README.md
git commit -m "chore: initial setup"

echo ""
echo "=== Git Automation Suite Created ==="
echo ""
echo "Available scripts:"
echo "  - scripts/smart_branch.sh - Intelligent branch creation"
echo "  - scripts/release.sh - Automated release process"  
echo "  - .git/hooks/pre-commit - Comprehensive pre-commit checks"
echo ""
echo "Example usage:"
echo "  bash scripts/smart_branch.sh feature PROJ-123 'user authentication'"
echo "  bash scripts/release.sh 1.2.0"

cd ..
echo ""
echo "=== Exercise 3 Complete ==="
echo "Automation suite created in: $WORK_DIR"

