#!/bin/bash
# Git Object Model Complete Understanding
# This script demonstrates how Git stores everything as objects

echo "=== Git Object Model Exploration ==="

# Create a sample file and track how Git stores it
echo "Hello Git Internals" > sample.txt
git add sample.txt
git commit -m "Add sample file"

# Find the commit hash
COMMIT_HASH=$(git rev-parse HEAD)
echo "Latest commit: $COMMIT_HASH"

# Examine the commit object
echo ""
echo "=== Commit Object ==="
git cat-file -t $COMMIT_HASH  # Shows object type: commit
echo ""
echo "Commit content:"
git cat-file -p $COMMIT_HASH  # Shows object content

# Get the tree hash from commit
TREE_HASH=$(git cat-file -p $COMMIT_HASH | grep "tree" | cut -d' ' -f2)
echo ""
echo "=== Tree Object ==="
echo "Tree object: $TREE_HASH"
git cat-file -p $TREE_HASH

# Get the blob hash
BLOB_HASH=$(git cat-file -p $TREE_HASH | grep "sample.txt" | cut -f1)
echo ""
echo "=== Blob Object ==="
echo "Blob object: $BLOB_HASH"
git cat-file -p $BLOB_HASH

# Object relationships visualization
echo ""
echo "=== Git Object Hierarchy ==="
echo "Commit ($COMMIT_HASH)"
echo "├── Tree ($TREE_HASH)" 
echo "    └── Blob ($BLOB_HASH) -> sample.txt content"

echo ""
echo "=== Manual Object Creation ==="

# Create blob manually
MANUAL_BLOB=$(echo "Manual content" | git hash-object -w --stdin)
echo "Manual blob hash: $MANUAL_BLOB"

# Create tree manually with multiple files
echo "File 1 content" > file1.txt
echo "File 2 content" > file2.txt
git add file1.txt file2.txt

# Write tree object
TREE_HASH_NEW=$(git write-tree)
echo "New tree hash: $TREE_HASH_NEW"

# Create commit manually
PARENT_COMMIT=$(git rev-parse HEAD)
COMMIT_HASH_NEW=$(echo "Manual commit message" | git commit-tree $TREE_HASH_NEW -p $PARENT_COMMIT)
echo "New commit hash: $COMMIT_HASH_NEW"

# Update branch reference
git reset --hard $COMMIT_HASH_NEW

echo ""
echo "=== Object Model Exploration Complete ==="

