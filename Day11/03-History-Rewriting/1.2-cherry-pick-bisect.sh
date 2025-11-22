#!/bin/bash
# Cherry-picking and Patch Management
# Strategic cherry-picking and Git bisect

echo "=== Strategic Cherry-picking ==="

# Set up branches for cherry-pick demo
git checkout main 2>/dev/null || git checkout -b main
git checkout -b hotfix-branch

# Create hotfix commits
echo "Critical fix 1" > fix1.txt
git add fix1.txt
git commit -m "Critical security fix"

echo "Critical fix 2" > fix2.txt
git add fix2.txt
git commit -m "Performance optimization"

echo "Debug code" > debug.txt
git add debug.txt
git commit -m "Add debug code (not for production)"

echo ""
echo "=== Cherry-picking Production-Ready Commits ==="

# Cherry-pick only the production-ready commits to main
git checkout main

# Get commit hashes
COMMIT1=$(git log hotfix-branch --oneline | grep "Critical security fix" | cut -d' ' -f1)
COMMIT2=$(git log hotfix-branch --oneline | grep "Performance optimization" | cut -d' ' -f1)

echo "Cherry-picking: $COMMIT1 (Critical security fix)"
git cherry-pick $COMMIT1

echo "Cherry-picking: $COMMIT2 (Performance optimization)"
git cherry-pick $COMMIT2

echo "Skipped debug commit (not for production)"

echo ""
echo "=== Cherry-pick with Conflict Resolution ==="
echo "If conflicts occur during cherry-pick:"
echo "  1. Resolve conflicts in files"
echo "  2. git add resolved-file.txt"
echo "  3. git cherry-pick --continue"
echo ""
echo "To abort: git cherry-pick --abort"

echo ""
echo "=== Creating and Applying Patches ==="

# Create patch files from commits
echo ""
echo "=== Creating Patches ==="
echo "Creating patch for last commit..."
git format-patch -1 HEAD --stdout > last-commit.patch
echo "Patch saved to: last-commit.patch"

echo ""
echo "Creating patches for last 3 commits..."
git format-patch -3 HEAD -o patches/
echo "Patches saved to patches/ directory"

echo ""
echo "Creating patches for feature branch..."
git checkout hotfix-branch
git format-patch main..hotfix-branch -o patches/feature/
echo "Feature patches saved to patches/feature/"

echo ""
echo "=== Applying Patches ==="
echo "To apply patches:"
echo "  git apply patch-file.patch         # Apply without commit"
echo "  git am patch-file.patch            # Apply and create commit"
echo "  git apply --check patch-file.patch # Check if patch applies cleanly"
echo "  git am -3 patch-file.patch        # Apply with three-way merge"

echo ""
echo "=== Advanced Git Bisect for Bug Hunting ==="

# Create a scenario for bisect
echo ""
echo "=== Setting Up Bisect Scenario ==="
echo "Finding the commit that introduced a bug:"

# Create test script for bisect
cat > test_script.sh << 'EOF'
#!/bin/bash
# Test script for bisect
# Return 0 if good, 1 if bad

# Example: Check if a specific file exists or has certain content
if [ -f "buggy-file.txt" ]; then
    if grep -q "BUG" buggy-file.txt; then
        exit 1  # Bad commit
    fi
fi
exit 0  # Good commit
EOF

chmod +x test_script.sh

echo ""
echo "=== Bisect Workflow ==="
echo "1. Start bisect:"
echo "   git bisect start"
echo ""
echo "2. Mark current commit as bad:"
echo "   git bisect bad HEAD"
echo ""
echo "3. Mark a known good commit:"
echo "   git bisect good v1.0"
echo ""
echo "4. Git checks out middle commit"
echo "   Test the commit manually or with script"
echo ""
echo "5. Mark result:"
echo "   git bisect good  # If bug doesn't exist"
echo "   git bisect bad   # If bug exists"
echo ""
echo "6. Repeat until Git finds the bad commit"
echo ""
echo "7. Automate with test script:"
echo "   git bisect run ./test_script.sh"
echo ""
echo "8. End bisect session:"
echo "   git bisect reset"

echo ""
echo "=== Creating Bisect Demo ==="

# Create commits with and without bug
git checkout main
for i in {1..5}; do
    echo "Commit $i content" > "file$i.txt"
    git add "file$i.txt"
    git commit -m "Add file $i"
done

# Introduce bug in commit 3
git checkout HEAD~2
echo "BUG: This file has a bug" > buggy-file.txt
git add buggy-file.txt
git commit -m "Add buggy file (introduces bug)"
BUG_COMMIT=$(git rev-parse HEAD)

# Continue with more commits
for i in {6..8}; do
    echo "Commit $i content" > "file$i.txt"
    git add "file$i.txt"
    git commit -m "Add file $i"
done

echo ""
echo "Bug introduced in commit: $BUG_COMMIT"
echo "To find it with bisect:"
echo "  git bisect start"
echo "  git bisect bad HEAD"
echo "  git bisect good HEAD~8"
echo "  git bisect run ./test_script.sh"

echo ""
echo "=== Cherry-pick and Bisect Demo Complete ==="

