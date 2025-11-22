#!/bin/bash
# Commit Message Validation Hook
# Validates commit message format

echo "=== Creating Commit Message Hook ==="

HOOKS_DIR=".git/hooks"
mkdir -p $HOOKS_DIR

cat > $HOOKS_DIR/commit-msg << 'HOOK_EOF'
#!/bin/bash
commit_msg_file=$1
commit_msg=$(cat $commit_msg_file)

# Check commit message format: type(scope): description
if ! echo "$commit_msg" | grep -qE '^(feat|fix|docs|style|refactor|test|chore)(\(.+\))?: .{1,50}$'; then
    echo "Invalid commit message format!"
    echo "Format: type(scope): description"
    echo "Types: feat, fix, docs, style, refactor, test, chore"
    echo "Example: feat(auth): add login functionality"
    exit 1
fi

# Check for imperative mood
if echo "$commit_msg" | grep -qE '(added|fixed|changed)'; then
    echo "Use imperative mood in commit messages!"
    echo "Good: 'Add feature' Bad: 'Added feature'"
    exit 1
fi

echo "Commit message format valid!"
HOOK_EOF

chmod +x $HOOKS_DIR/commit-msg
echo "✅ Commit message hook created at $HOOKS_DIR/commit-msg"

