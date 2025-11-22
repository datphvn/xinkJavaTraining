# History Rewriting and Manipulation

This section covers interactive rebase, cherry-picking, patches, and Git bisect.

## Contents

### 1.1 Interactive Rebase Mastery
- Cleaning up messy commit history
- Squashing, rewording, and dropping commits
- Splitting commits
- Rebase with conflict resolution
- Automated testing during rebase

**Script**: `1.1-interactive-rebase.sh`

### 1.2 Cherry-picking and Patch Management
- Strategic cherry-picking
- Creating and applying patches
- Git bisect for bug hunting
- Automated bisect with test scripts

**Script**: `1.2-cherry-pick-bisect.sh`

## Usage

Run each script in a Git repository:

```bash
cd 03-History-Rewriting
git init
bash 1.1-interactive-rebase.sh
bash 1.2-cherry-pick-bisect.sh
```

## Learning Objectives

After completing this section, you will:
- Clean up Git history with interactive rebase
- Use cherry-pick strategically
- Create and apply patches
- Find bugs efficiently with Git bisect

## Important Notes

⚠️ **Warning**: Rewriting history changes commit hashes. Never rewrite history on shared branches that others are using!

