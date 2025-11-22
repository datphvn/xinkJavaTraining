# CI/CD Integration

This section covers GitHub Actions workflows and automated Git operations.

## Contents

### GitHub Actions CI/CD Pipeline
- Multi-JDK testing (Java 11, 17, 21)
- Code coverage reporting
- Security scanning
- Automated deployments
- Release creation

**File**: `.github/workflows/ci-cd.yml`

### Automated Version Bumping
- Semantic versioning based on commit messages
- Automatic version updates in pom.xml and package.json
- Tag creation

**Script**: `scripts/bump_version.sh`

### Changelog Generation
- Automatic changelog from commit history
- Grouped by commit type (feat, fix, docs, etc.)
- Links to commits

**Script**: `scripts/generate_changelog.sh`

## Usage

### GitHub Actions

Place the workflow file in your repository:

```bash
mkdir -p .github/workflows
cp 06-CI-CD/.github/workflows/ci-cd.yml .github/workflows/
```

### Version Bumping

```bash
cd 06-CI-CD
bash scripts/bump_version.sh
```

### Changelog Generation

```bash
cd 06-CI-CD
bash scripts/generate_changelog.sh
```

## Learning Objectives

After completing this section, you will:
- Set up CI/CD pipelines with GitHub Actions
- Automate version management
- Generate changelogs automatically
- Integrate Git with deployment workflows

## Notes

- GitHub Actions requires a GitHub repository
- Secrets must be configured in repository settings
- Version bumping follows semantic versioning
- Changelog generation requires conventional commits

