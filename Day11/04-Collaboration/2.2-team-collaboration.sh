#!/bin/bash
# Team Collaboration Patterns
# Forked and shared repository workflows

echo "=== Forked Repository Workflow ==="

echo "Fork-based contribution (for open source):"
echo ""
echo "1. Fork repository on GitHub"
echo "2. Clone your fork:"
echo "   git clone https://github.com/yourusername/project.git"
echo "   cd project"
echo ""
echo "3. Add upstream remote:"
echo "   git remote add upstream https://github.com/originaluser/project.git"
echo ""
echo "4. Create feature branch:"
echo "   git checkout -b feature/new-functionality"
echo ""
echo "5. Make changes and commit"
echo ""
echo "6. Push to your fork:"
echo "   git push origin feature/new-functionality"
echo ""
echo "7. Sync with upstream before PR:"
echo "   git fetch upstream"
echo "   git checkout main"
echo "   git merge upstream/main"
echo "   git push origin main"
echo ""
echo "8. Rebase feature branch on latest main:"
echo "   git checkout feature/new-functionality"
echo "   git rebase main"
echo "   git push --force-with-lease origin feature/new-functionality"

echo ""
echo "=== Shared Repository Workflow ==="

# Direct collaboration workflow
echo "Creating shared development branch..."

git checkout main 2>/dev/null || git checkout -b main
git checkout -b develop
echo "Created develop branch"

# Feature branches from develop
FEATURE_BRANCH="feature/shared-component"
git checkout -b $FEATURE_BRANCH

cat > SharedComponent.java << 'EOF'
public class SharedComponent {
    // Shared component implementation
}
EOF

git add SharedComponent.java
git commit -m "feat: add shared component"

echo ""
echo "=== Merge back to develop ==="
git checkout develop
git merge --no-ff $FEATURE_BRANCH -m "Merge feature: shared-component"
git branch -d $FEATURE_BRANCH

echo ""
echo "=== Periodic merge to main ==="
echo "When ready for release:"
echo "  git checkout main"
echo "  git merge develop"
echo "  git push origin main"

# Simulate merge to main
git checkout main
git merge --no-ff develop -m "Merge develop to main for release"

echo ""
echo "=== Release Branch Workflow ==="

# Create release branch for stabilization
git checkout develop
RELEASE_VERSION="v2.1.0"
RELEASE_BRANCH="release/$RELEASE_VERSION"
git checkout -b $RELEASE_BRANCH

# Version bumps and final fixes only
echo "2.1.0" > VERSION
git add VERSION
git commit -m "chore: bump version to $RELEASE_VERSION"

# Bug fixes on release branch
cat > bugfix.txt << 'EOF'
Critical bug fix for release candidate
EOF
git add bugfix.txt
git commit -m "fix: critical bug in release candidate"

echo ""
echo "=== Merge to main and tag ==="
git checkout main
git merge --no-ff $RELEASE_BRANCH -m "Release $RELEASE_VERSION"
git tag -a $RELEASE_VERSION -m "Release version $RELEASE_VERSION"

echo ""
echo "=== Merge fixes back to develop ==="
git checkout develop
git merge --no-ff $RELEASE_BRANCH -m "Merge release $RELEASE_VERSION back to develop"

echo ""
echo "=== Clean up release branch ==="
git branch -d $RELEASE_BRANCH

echo ""
echo "=== Team Collaboration Workflows Complete ==="
echo ""
echo "Summary:"
echo "- Forked workflow: For open source contributions"
echo "- Shared repository: For team collaboration"
echo "- Release branches: For stabilization before release"

