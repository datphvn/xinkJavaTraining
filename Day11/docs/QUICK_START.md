# Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Step 1: Choose Your Learning Path

**Beginner to Advanced Git?**
Start with: `01-Git-Internals/` → `02-Advanced-Branching/` → `05-Git-Hooks/`

**Already know Git basics?**
Jump to: `02-Advanced-Branching/` → `03-History-Rewriting/` → Exercises

**Focus on automation?**
Go to: `05-Git-Hooks/` → `06-CI-CD/` → `09-Exercise-3-Automation-Suite/`

### Step 2: Run Your First Script

```bash
# Navigate to a section
cd 01-Git-Internals

# Initialize a Git repository (if needed)
git init

# Run the script
bash 1.1-git-object-model.sh
```

### Step 3: Practice with Exercises

```bash
# Exercise 1: Repository Management
cd 07-Exercise-1-Repository-Management
bash setup-repository.sh

# Exercise 2: Conflict Resolution
cd 08-Exercise-2-Conflict-Resolution
bash setup-conflicts.sh

# Exercise 3: Automation
cd 09-Exercise-3-Automation-Suite
bash setup-automation.sh
```

## 📋 Common Commands

### View Git History
```bash
git log --oneline --graph --all
```

### Check Branch Structure
```bash
git branch -a
```

### See Staged Changes
```bash
git status
git diff --cached
```

### Interactive Rebase
```bash
git rebase -i HEAD~5
```

### Cherry-pick a Commit
```bash
git cherry-pick <commit-hash>
```

## 🎯 Learning Tips

1. **Read the README** in each folder first
2. **Run scripts one at a time** to understand each step
3. **Experiment** - modify scripts and see what happens
4. **Practice** - apply concepts to your own projects
5. **Review** - go back and re-read sections you found difficult

## ❓ Troubleshooting

### Scripts won't run?
- Make sure you're using Bash (Git Bash on Windows)
- Check file permissions: `chmod +x script.sh`
- Ensure you're in a Git repository: `git init`

### Merge conflicts?
- Read `08-Exercise-2-Conflict-Resolution/README.md`
- Use `git mergetool` for visual conflict resolution
- Practice with the conflict resolution exercise

### Hooks not working?
- Verify hooks are executable: `ls -la .git/hooks/`
- Check hook syntax: `bash -n .git/hooks/pre-commit`
- Test manually: `bash .git/hooks/pre-commit`

## 🔗 Next Steps

After completing the basics:
1. Set up hooks in your real projects
2. Create your own automation scripts
3. Contribute to open source projects
4. Share knowledge with your team

Happy learning! 🎉

