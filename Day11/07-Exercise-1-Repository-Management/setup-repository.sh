#!/bin/bash
# Exercise 1: Advanced Repository Management
# Build a complete Git workflow for a team project

echo "=== Exercise 1: Advanced Repository Management ==="

REPO_DIR="advanced-git-project"
if [ -d "$REPO_DIR" ]; then
    echo "Repository already exists. Removing..."
    rm -rf "$REPO_DIR"
fi

mkdir "$REPO_DIR"
cd "$REPO_DIR"

echo ""
echo "=== Task 1.1: Set up advanced repository structure ==="

git init

# Configure repository with all best practices
git config user.name "Team Developer"
git config user.email "developer@example.com"
git config core.autocrlf false
git config pull.rebase true
git config fetch.prune true

echo "✅ Git repository initialized and configured"

# Create comprehensive .gitignore
cat > .gitignore << 'EOF'
# IDE files
.vscode/
.idea/
*.swp
*.swo
*~

# Build outputs  
target/
build/
dist/
*.class
*.jar
*.war

# Logs
*.log
logs/

# OS files
.DS_Store
Thumbs.db

# Dependencies
node_modules/
.npm
.pnpm-store/

# Environment
.env
.env.local
.env.*.local

# Temporary files
*.tmp
*.temp
*.cache
EOF

git add .gitignore
git commit -m "chore: add comprehensive .gitignore"
echo "✅ Created .gitignore"

echo ""
echo "=== Task 1.2: Implement GitFlow with all branches ==="

# Create main branch
git checkout -b main
echo "Initial commit" > README.md
git add README.md
git commit -m "chore: initial commit"

# Create develop branch
git checkout -b develop
echo "✅ Created develop branch"

# Create feature branch with proper naming
FEATURE_BRANCH="feature/PROJ-123-user-authentication"
git checkout -b $FEATURE_BRANCH

cat > auth.md << 'EOF'
# Authentication System

## Overview
Basic authentication structure with security configurations and user model definitions.

## Features
- User authentication
- Security configurations
- User model definitions
EOF

git add auth.md
git commit -m "feat(auth): add authentication framework

- Add basic authentication structure  
- Include security configurations
- Add user model definitions

Implements: PROJ-123"

echo "✅ Created feature branch: $FEATURE_BRANCH"

echo ""
echo "=== Task 1.3: Advanced merge strategies practice ==="

# Merge feature back to develop
git checkout develop
git merge --no-ff $FEATURE_BRANCH -m "Merge feature: PROJ-123-user-authentication"
git branch -d $FEATURE_BRANCH
echo "✅ Merged feature to develop"

# Create release branch
RELEASE_VERSION="v1.0.0"
RELEASE_BRANCH="release/$RELEASE_VERSION"
git checkout -b $RELEASE_BRANCH

echo "$RELEASE_VERSION" > VERSION
git add VERSION  
git commit -m "chore: bump version to $RELEASE_VERSION"
echo "✅ Created release branch: $RELEASE_BRANCH"

# Merge release to main
git checkout main
git merge --no-ff $RELEASE_BRANCH -m "Release $RELEASE_VERSION"
git tag -a $RELEASE_VERSION -m "Release version $RELEASE_VERSION

Features:
- User authentication system
- Basic security framework  

Breaking Changes:
- None

Migration:
- No migration required"
echo "✅ Merged release to main and created tag"

# Merge release changes back to develop
git checkout develop
git merge --no-ff $RELEASE_BRANCH -m "Merge release $RELEASE_VERSION back to develop"
git branch -d $RELEASE_BRANCH
echo "✅ Merged release back to develop"

echo ""
echo "=== Repository Structure ==="
echo "Branches:"
git branch -a

echo ""
echo "Tags:"
git tag

echo ""
echo "=== Commit History ==="
git log --oneline --graph --decorate --all

echo ""
echo "=== Exercise 1 Complete ==="
echo "Repository created in: $REPO_DIR"
echo ""
echo "Next steps:"
echo "1. Review the branch structure"
echo "2. Practice merging strategies"
echo "3. Create additional features using the same workflow"

