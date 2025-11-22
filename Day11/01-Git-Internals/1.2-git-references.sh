#!/bin/bash
# Git References and Branch Implementation
# Understanding how Git references work

echo "=== Git References Exploration ==="

# All references stored in .git/refs/
echo "=== Local Branches ==="
ls -la .git/refs/heads/ 2>/dev/null || echo "No local branches yet"

echo ""
echo "=== Remote Tracking Branches ==="
ls -la .git/refs/remotes/ 2>/dev/null || echo "No remote branches yet"

echo ""
echo "=== Tags ==="
ls -la .git/refs/tags/ 2>/dev/null || echo "No tags yet"

# Branch is just a pointer to a commit
if [ -f ".git/refs/heads/main" ] || [ -f ".git/refs/heads/master" ]; then
    echo ""
    echo "=== Current Branch Reference ==="
    CURRENT_BRANCH=$(git branch --show-current)
    if [ -f ".git/refs/heads/$CURRENT_BRANCH" ]; then
        cat .git/refs/heads/$CURRENT_BRANCH
        echo "Same as: $(git rev-parse $CURRENT_BRANCH)"
    fi
fi

# HEAD is a reference to current branch
echo ""
echo "=== HEAD Reference ==="
cat .git/HEAD
echo ""
echo "Current branch reference:"
git symbolic-ref HEAD 2>/dev/null || echo "Detached HEAD state"

echo ""
echo "=== Creating References Manually ==="

# Create new branch reference manually
CURRENT_COMMIT=$(git rev-parse HEAD)
echo $CURRENT_COMMIT > .git/refs/heads/manual-branch
echo "Created manual-branch pointing to: $CURRENT_COMMIT"

# List branches - should show manual-branch
echo ""
echo "Available branches:"
git branch

# Create lightweight tag manually
echo $CURRENT_COMMIT > .git/refs/tags/manual-tag-v1.0
echo "Created manual-tag-v1.0 pointing to: $CURRENT_COMMIT"

# List tags
echo ""
echo "Available tags:"
git tag

echo ""
echo "=== Packed References ==="
# When many references exist, Git packs them for efficiency
echo "Running garbage collection to pack references..."
git gc --quiet

if [ -f ".git/packed-refs" ]; then
    echo "Packed references found:"
    head -20 .git/packed-refs
fi

# Demonstrate reference resolution
echo ""
echo "=== Reference Resolution ==="
echo "main resolves to: $(git rev-parse main 2>/dev/null || echo 'N/A')"
echo "HEAD resolves to: $(git rev-parse HEAD)"
echo "HEAD~1 resolves to: $(git rev-parse HEAD~1 2>/dev/null || echo 'N/A')"

echo ""
echo "=== References Exploration Complete ==="

