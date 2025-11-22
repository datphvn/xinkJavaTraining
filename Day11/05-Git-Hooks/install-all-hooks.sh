#!/bin/bash
# Install All Git Hooks
# Runs all hook installation scripts

echo "=== Installing All Git Hooks ==="

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Check if we're in a Git repository
if [ ! -d ".git" ]; then
    echo "Error: Not in a Git repository. Please run 'git init' first."
    exit 1
fi

# Install client-side hooks
echo ""
echo "Installing client-side hooks..."
bash "$SCRIPT_DIR/pre-commit.sh"
bash "$SCRIPT_DIR/commit-msg.sh"
bash "$SCRIPT_DIR/pre-push.sh"

# Create server-side hook simulations
echo ""
echo "Creating server-side hook simulations..."
bash "$SCRIPT_DIR/server-side-hooks.sh"

echo ""
echo "=== All Hooks Installed Successfully ==="
echo ""
echo "Client-side hooks installed in .git/hooks/:"
echo "  - pre-commit   (code quality checks)"
echo "  - commit-msg   (commit message validation)"
echo "  - pre-push     (security checks)"
echo ""
echo "Server-side hook simulations in hooks-simulation/:"
echo "  - pre-receive.sh  (access control)"
echo "  - post-receive.sh (deployment trigger)"

