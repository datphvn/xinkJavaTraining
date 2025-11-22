#!/bin/bash
# Interactive Rebase Mastery
# Complete workflow for cleaning up Git history

echo "=== Interactive Rebase Complete Workflow ==="

# Create repository with messy history
WORK_DIR="rebase-demo"
if [ -d "$WORK_DIR" ]; then
    rm -rf "$WORK_DIR"
fi

mkdir "$WORK_DIR"
cd "$WORK_DIR"

git init
git config user.name "Test User"
git config user.email "test@example.com"

# Create commits with various issues
echo "Feature start" > feature.txt
git add feature.txt
git commit -m "WIP: start feature"

echo "More work" >> feature.txt
git add feature.txt
git commit -m "fix typo"

echo "Final feature" >> feature.txt
git add feature.txt
git commit -m "Add awesome feature"

echo "Oops forgot something" >> feature.txt
git add feature.txt
git commit -m "forgot to add validation"

echo "Debug prints" >> feature.txt
git add feature.txt
git commit -m "add debug prints (TODO: remove)"

echo ""
echo "=== Messy History Before Rebase ==="
git log --oneline

echo ""
echo "=== Interactive Rebase Actions ==="
echo "To start interactive rebase for last 5 commits:"
echo "  git rebase -i HEAD~5"
echo ""
echo "Available actions:"
echo "  pick (p) - use commit as-is"
echo "  reword (r) - use commit but edit message"
echo "  edit (e) - use commit but stop for amending"
echo "  squash (s) - combine with previous commit"
echo "  fixup (f) - like squash but discard commit message"
echo "  exec (x) - run shell command"
echo "  break (b) - stop rebase (continue with git rebase --continue)"
echo "  drop (d) - remove commit"
echo "  label (l) - label current HEAD with name"
echo "  reset (t) - reset HEAD to label"
echo "  merge (m) - create merge commit"

echo ""
echo "=== Example Rebase Plan ==="
cat > rebase_plan.txt << 'EOF'
pick abc1234 WIP: start feature          -> reword to "feat: add feature framework"
pick def5678 fix typo                    -> squash into next commit
pick ghi9012 Add awesome feature         -> keep as is
pick jkl3456 forgot to add validation    -> squash into previous
pick mno7890 add debug prints            -> drop (remove entirely)
EOF

echo "Saved rebase plan to rebase_plan.txt"

echo ""
echo "=== Advanced Rebase Techniques ==="

# Create complex rebase scenario
git checkout -b complex-rebase
echo "Change 1" > file1.txt
git add file1.txt
git commit -m "Change 1"

echo "Change 2" > file2.txt
git add file2.txt
git commit -m "Change 2"

# Go back to main and make conflicting changes
git checkout main 2>/dev/null || git checkout -b main
echo "Conflicting change" > file1.txt
git add file1.txt
git commit -m "Main change"

# Rebase complex-rebase onto main
git checkout complex-rebase
echo ""
echo "=== Rebase with Conflict Resolution ==="
echo "To rebase with conflicts:"
echo "  1. git rebase main (will create conflicts)"
echo "  2. Resolve conflicts in files"
echo "  3. git add resolved-file.txt"
echo "  4. git rebase --continue"
echo ""
echo "Other rebase commands:"
echo "  git rebase --skip    # Skip current commit"
echo "  git rebase --abort   # Return to pre-rebase state"

echo ""
echo "=== Splitting Commits ==="
echo "To split a commit:"
echo "  1. git rebase -i HEAD~1"
echo "  2. Change 'pick' to 'edit' for the commit"
echo "  3. When rebase stops: git reset HEAD~1"
echo "  4. Stage and commit parts separately"
echo "  5. git rebase --continue"

echo ""
echo "=== Rebase with Exec for Testing ==="
echo "To run tests after each commit during rebase:"
echo "  git rebase -i HEAD~3 --exec 'mvn test'"
echo ""
echo "This runs 'mvn test' after each commit"
echo "Stops if any test fails, allowing fixes"

cd ..
echo ""
echo "=== Interactive Rebase Demo Complete ==="
echo "Demo directory: $WORK_DIR"

