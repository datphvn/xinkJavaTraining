# Exercise 3: Git Automation Suite

Create comprehensive Git automation with hooks and scripts.

## Objectives

- Create advanced Git hooks suite
- Build automated branching scripts
- Implement release automation
- Set up comprehensive pre-commit checks

## Tasks

### Task 3.1: Advanced Git hooks suite
- Comprehensive pre-commit hook
- Code quality checks
- Security validation
- File size limits
- Automated testing

### Task 3.2: Automated branching script
- Smart branch creation
- Automatic base branch selection
- Branch naming conventions
- Initial branch setup

### Task 3.3: Release automation
- Automated version bumping
- Release branch creation
- Tag management
- Changelog generation

## Usage

```bash
cd 09-Exercise-3-Automation-Suite
bash setup-automation.sh
```

## Scripts

### Smart Branch Script

Create branches with proper naming and setup:

```bash
bash scripts/smart_branch.sh feature PROJ-123 "user authentication"
bash scripts/smart_branch.sh bugfix PROJ-124 "fix login bug"
bash scripts/smart_branch.sh hotfix PROJ-125 "security patch"
```

### Release Script

Automate release process:

```bash
bash scripts/release.sh 1.2.0
```

This will:
1. Create release branch
2. Update version files
3. Generate changelog
4. Merge to main and tag
5. Merge back to develop

## Hooks

### Pre-commit Hook

Automatically runs:
- Commit message validation
- Code quality checks
- Security scans
- File size validation
- Test execution

## Learning Outcomes

- Create custom Git hooks
- Automate common Git workflows
- Implement release automation
- Set up comprehensive validation

## Customization

All scripts can be customized for your project:
- Modify hook checks in `.git/hooks/pre-commit`
- Adjust branch naming in `scripts/smart_branch.sh`
- Customize release process in `scripts/release.sh`

