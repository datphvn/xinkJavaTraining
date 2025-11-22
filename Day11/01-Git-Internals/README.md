# Git Internals Deep Dive

This section covers the internal workings of Git, including object model, references, and the index.

## Contents

### 1.1 Git Object Model
- Understanding blob, tree, commit, and tag objects
- Manual object creation
- Object relationships

**Script**: `1.1-git-object-model.sh`

### 1.2 Git References
- Branch implementation
- Tag references
- HEAD and symbolic references
- Packed references

**Script**: `1.2-git-references.sh`

### 1.3 Git Index
- Staging area structure
- File modes (regular, executable, symbolic link)
- Direct index manipulation
- Partial staging operations

**Script**: `1.3-git-index.sh`

## Usage

Each script can be run independently. Make sure you're in a Git repository:

```bash
cd 01-Git-Internals
git init
bash 1.1-git-object-model.sh
bash 1.2-git-references.sh
bash 1.3-git-index.sh
```

## Learning Objectives

After completing this section, you will understand:
- How Git stores data internally
- The relationship between objects
- How branches and tags are implemented
- The structure and purpose of the Git index

