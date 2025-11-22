#!/bin/bash
# Pre-push Hook for Security
# Security checks before pushing

echo "=== Creating Pre-push Hook ==="

HOOKS_DIR=".git/hooks"
mkdir -p $HOOKS_DIR

cat > $HOOKS_DIR/pre-push << 'HOOK_EOF'
#!/bin/bash
echo "Running pre-push security checks..."

# Check for secrets in code
if git diff @{upstream}..HEAD --name-only | xargs grep -l "password\|secret\|key" 2>/dev/null; then
    echo "Warning: Potential secrets found in code!"
    echo "Files containing sensitive words:"
    git diff @{upstream}..HEAD --name-only | xargs grep -l "password\|secret\|key" 2>/dev/null
    
    read -p "Continue push? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Check for large files
large_files=$(git diff @{upstream}..HEAD --name-only | xargs ls -la 2>/dev/null | awk '$5 > 1048576 {print $9, $5}')
if [ ! -z "$large_files" ]; then
    echo "Warning: Large files detected (>1MB):"
    echo "$large_files"
    
    read -p "Continue push? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo "Pre-push checks completed!"
HOOK_EOF

chmod +x $HOOKS_DIR/pre-push
echo "✅ Pre-push hook created at $HOOKS_DIR/pre-push"

