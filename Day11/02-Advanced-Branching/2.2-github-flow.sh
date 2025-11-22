#!/bin/bash
# GitHub Flow for Continuous Deployment
# Simpler workflow for continuous deployment

echo "=== GitHub Flow Implementation ==="

# Only main branch + feature branches + pull requests

# Ensure we're on main branch
git checkout main 2>/dev/null || git checkout -b main
git pull origin main 2>/dev/null || echo "No remote configured yet"

echo ""
echo "=== Creating Feature Branch ==="

FEATURE_NAME="payment-integration"
FEATURE_BRANCH="feature/$FEATURE_NAME"

git checkout -b $FEATURE_BRANCH
echo "Created feature branch: $FEATURE_BRANCH"

# Develop feature with frequent commits
echo "Payment gateway setup" > payment.java
git add payment.java
git commit -m "Set up payment gateway"

echo "Credit card processing" >> payment.java
git add payment.java
git commit -m "Add credit card processing"

echo "PayPal integration" >> payment.java
git add payment.java
git commit -m "Add PayPal integration"

echo ""
echo "=== Pushing Feature Branch ==="
echo "To push feature branch:"
echo "  git push -u origin $FEATURE_BRANCH"
echo ""
echo "Then create a Pull Request on GitHub"

echo ""
echo "=== After Code Review and Approval ==="
echo "Merge to main:"
echo "  git checkout main"
echo "  git merge $FEATURE_BRANCH"
echo "  git push origin main"
echo ""
echo "Clean up feature branch:"
echo "  git branch -d $FEATURE_BRANCH"
echo "  git push origin --delete $FEATURE_BRANCH"

# Simulate merge
git checkout main
git merge --no-ff $FEATURE_BRANCH -m "Merge feature: $FEATURE_NAME"
git branch -d $FEATURE_BRANCH

echo ""
echo "=== Automated GitHub Flow with Hooks ==="

# Create pre-push hook for automated testing
HOOKS_DIR=".git/hooks"
mkdir -p $HOOKS_DIR

cat > $HOOKS_DIR/pre-push << 'HOOK_EOF'
#!/bin/bash
echo "Running tests before push..."

# Run unit tests
if [ -f "pom.xml" ]; then
    mvn test
elif [ -f "build.gradle" ]; then
    ./gradlew test
else
    echo "No build file found, skipping tests"
fi

# Check test results
if [ $? -ne 0 ]; then
    echo "Tests failed! Push aborted."
    exit 1
fi

echo "Tests passed! Proceeding with push."
HOOK_EOF

chmod +x $HOOKS_DIR/pre-push
echo "Created pre-push hook for automated testing"

echo ""
echo "=== GitHub Flow Setup Complete ==="
echo "This workflow is ideal for:"
echo "- Continuous deployment"
echo "- Small to medium teams"
echo "- Projects with frequent releases"

