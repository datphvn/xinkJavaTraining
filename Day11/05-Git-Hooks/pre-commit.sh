#!/bin/bash
# Pre-commit Hook for Code Quality
# Comprehensive pre-commit checks

echo "=== Creating Pre-commit Hook ==="

HOOKS_DIR=".git/hooks"
mkdir -p $HOOKS_DIR

cat > $HOOKS_DIR/pre-commit << 'HOOK_EOF'
#!/bin/bash
echo "Running pre-commit checks..."

# Check for Java code style
if [ -d "src" ]; then
    echo "Checking Java code style..."
    find src -name "*.java" -exec grep -l "System.out.println" {} \; 2>/dev/null
    if [ $? -eq 0 ]; then
        echo "Error: Found System.out.println in code. Use logging instead."
        exit 1
    fi
fi

# Check for TODO comments in production code
if git diff --cached --name-only | grep -E '\.(java|js|py)$' | xargs grep -l "TODO" 2>/dev/null; then
    echo "Warning: Found TODO comments in staged files"
    read -p "Continue commit? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Run tests if they exist
if [ -f "pom.xml" ]; then
    echo "Running Maven tests..."
    mvn test -q
elif [ -f "build.gradle" ]; then
    echo "Running Gradle tests..."
    ./gradlew test --quiet
elif [ -f "package.json" ]; then
    echo "Running npm tests..."
    npm test
fi

if [ $? -ne 0 ]; then
    echo "Tests failed! Commit aborted."
    exit 1
fi

echo "Pre-commit checks passed!"
HOOK_EOF

chmod +x $HOOKS_DIR/pre-commit
echo "✅ Pre-commit hook created at $HOOKS_DIR/pre-commit"

