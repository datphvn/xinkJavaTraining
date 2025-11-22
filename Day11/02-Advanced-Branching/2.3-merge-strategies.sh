#!/bin/bash
# Advanced Merge Strategies
# Demonstrating different merge strategies

echo "=== Different Merge Strategies Comparison ==="

# Create test repository structure
WORK_DIR="merge-strategies-demo"
if [ -d "$WORK_DIR" ]; then
    rm -rf "$WORK_DIR"
fi

mkdir "$WORK_DIR"
cd "$WORK_DIR"

git init
git config user.name "Test User"
git config user.email "test@example.com"

# Set up main branch
echo "Main branch content" > main.txt
git add main.txt
git commit -m "Initial main commit"

# Create feature branch
git checkout -b feature-branch
echo "Feature addition" > feature.txt
git add feature.txt
git commit -m "Add feature"

# Make changes to main
git checkout main
echo "Main branch update" >> main.txt
git add main.txt
git commit -m "Update main branch"

echo ""
echo "=== Strategy 1: Merge Commit (Default) ==="
echo "Creates merge commit preserving branch history"

git merge feature-branch --no-edit
echo "Merge commit created"
git log --oneline --graph --max-count=5

# Reset for next strategy
echo ""
echo "=== Resetting for next strategy ==="
git reset --hard HEAD~2

echo ""
echo "=== Strategy 2: Squash Merge ==="
echo "Combines all feature commits into single commit"

git merge --squash feature-branch
git commit -m "Squashed feature-branch changes"
echo "Squash merge completed"
git log --oneline --graph --max-count=5

# Reset for next strategy
echo ""
echo "=== Resetting for next strategy ==="
git reset --hard HEAD~1

echo ""
echo "=== Strategy 3: Rebase and Merge (Linear History) ==="
echo "Creates linear history without merge commit"

git checkout feature-branch
git rebase main
git checkout main
git merge feature-branch  # Fast-forward merge
echo "Rebase and merge completed"
git log --oneline --graph --max-count=5

echo ""
echo "=== Conflict Resolution Strategies ==="

# Create conflicting changes
git checkout -b conflict-demo
echo "Conflicting change from branch" > conflict.txt
git add conflict.txt
git commit -m "Branch change"

git checkout main
echo "Conflicting change from main" > conflict.txt
git add conflict.txt
git commit -m "Main change"

# Attempt merge (will create conflict)
echo ""
echo "=== Attempting Merge (Will Create Conflict) ==="
git merge conflict-demo || echo "Merge conflict detected as expected"

# View conflict
echo ""
echo "=== Conflict Content ==="
if [ -f "conflict.txt" ]; then
    cat conflict.txt
fi

echo ""
echo "=== Resolution Options ==="
echo "1. Manual resolution: Edit file and resolve conflicts"
echo "2. Use merge tools: git mergetool"
echo "3. Choose specific version:"
echo "   - git checkout --ours conflict.txt (keep our version)"
echo "   - git checkout --theirs conflict.txt (keep their version)"

# Resolve conflict for demonstration
echo "Resolved: Combined both changes" > conflict.txt
git add conflict.txt
git commit -m "Resolve merge conflict"

echo ""
echo "=== Merge Strategies Demo Complete ==="
echo ""
echo "Summary:"
echo "- Merge commit: Preserves history, shows branch structure"
echo "- Squash merge: Clean history, loses individual commits"
echo "- Rebase merge: Linear history, rewrites commit history"

cd ..
echo "Demo directory: $WORK_DIR"

