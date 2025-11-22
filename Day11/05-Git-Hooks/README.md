# Git Hooks Implementation

This section covers client-side and server-side Git hooks for automation.

## Contents

### Client-Side Hooks

#### Pre-commit Hook
- Code quality checks
- Java code style validation
- TODO comment warnings
- Automated test execution

**Script**: `pre-commit.sh`

#### Commit Message Hook
- Validates commit message format
- Enforces conventional commits
- Checks imperative mood

**Script**: `commit-msg.sh`

#### Pre-push Hook
- Security checks for secrets
- Large file detection
- Pre-push validation

**Script**: `pre-push.sh`

### Server-Side Hooks (Simulation)

#### Pre-receive Hook
- Repository access control
- Block direct pushes to main
- Commit message validation

**Script**: `server-side-hooks.sh`

#### Post-receive Hook
- Deployment triggers
- Issue tracker integration
- Branch-based deployments

**Script**: `server-side-hooks.sh`

## Usage

### Install All Hooks

```bash
cd 05-Git-Hooks
git init  # If not already a Git repository
bash install-all-hooks.sh
```

### Install Individual Hooks

```bash
bash pre-commit.sh
bash commit-msg.sh
bash pre-push.sh
```

## Learning Objectives

After completing this section, you will:
- Create custom Git hooks for validation
- Automate code quality checks
- Implement security checks
- Understand server-side hook workflows

## Notes

- Client-side hooks are stored in `.git/hooks/`
- Server-side hooks would be placed on the Git server
- Hooks must be executable (`chmod +x`)
- Hooks can be bypassed with `--no-verify` flag (use with caution)

