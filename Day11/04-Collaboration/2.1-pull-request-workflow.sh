#!/bin/bash
# Advanced Pull Request Workflows
# Complete PR workflow setup

echo "=== Feature Branch Pull Request Workflow ==="

# Create well-structured feature branch
git checkout main 2>/dev/null || git checkout -b main
git pull origin main 2>/dev/null || echo "No remote configured"

FEATURE_BRANCH="feature/user-profile-enhancement"
git checkout -b $FEATURE_BRANCH

echo ""
echo "=== Making Atomic Commits with Good Messages ==="

# Make atomic commits with good messages
cat > UserProfile.java << 'EOF'
public class UserProfile {
    private String username;
    private String email;
    
    // Constructor and getters/setters
}
EOF

git add UserProfile.java
git commit -m "feat: add user profile model

- Add UserProfile entity with basic fields
- Include validation annotations
- Add database constraints

Closes #123"

cat > UserProfileService.java << 'EOF'
public class UserProfileService {
    // CRUD operations for user profiles
    // Business logic validation
    // Error handling
}
EOF

git add UserProfileService.java
git commit -m "feat: implement user profile service

- Add CRUD operations for user profiles
- Include business logic validation
- Add error handling

Related to #123"

echo ""
echo "=== Pushing Feature Branch ==="
echo "To push feature branch:"
echo "  git push -u origin $FEATURE_BRANCH"
echo ""
echo "Then create a Pull Request on GitHub/GitLab"

echo ""
echo "=== Code Review Best Practices Setup ==="

# Configure Git for better code review
echo "Configuring Git for code review..."

# Set up commit message template
cat > ~/.gitmessage << 'TEMPLATE_EOF'
# Type: Subject (50 chars max)
#
# Body: Explain what and why (72 chars per line)
#
# Footer: Reference issues/PRs
# 
# Types: feat, fix, docs, style, refactor, test, chore
TEMPLATE_EOF

echo "Created commit message template at ~/.gitmessage"
echo ""
echo "To use template, configure:"
echo "  git config --global commit.template ~/.gitmessage"

echo ""
echo "=== Draft Pull Requests and WIP Management ==="

# Mark commits as WIP during development
echo "WIP: implement user validation" > validation.txt
git add validation.txt
git commit -m "WIP: implement user validation

This is work in progress - not ready for review yet"

echo ""
echo "=== Cleaning Up WIP Commits ==="
echo "Before final PR, clean up WIP commits:"
echo "  git rebase -i HEAD~3  # Squash WIP commits together"

echo ""
echo "=== Advanced Review Techniques ==="

echo ""
echo "Review changes before creating PR:"
echo "  git diff main..$FEATURE_BRANCH"
echo "  git log main..$FEATURE_BRANCH --oneline"

# Create PR template
cat > PR_TEMPLATE.md << 'PR_EOF'
## Description

Brief description of changes

## Type of Change  

- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing

- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Screenshots/Videos

(If applicable)

## Related Issues

Closes #123
PR_EOF

echo "Created PR template: PR_TEMPLATE.md"

echo ""
echo "=== Pull Request Workflow Complete ==="

