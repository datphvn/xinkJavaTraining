#!/bin/bash
# Git Index (Staging Area) Advanced
# Understanding the Git index structure

echo "=== Git Index Structure Analysis ==="

# The index is a binary file that tracks staged changes
echo "=== Index Contents ==="
echo "Staged files with modes:"
git ls-files --stage

echo ""
echo "=== Cached (Staged) Files ==="
git ls-files --cached

echo ""
echo "=== Modified Files ==="
git ls-files --modified

echo ""
echo "=== File Modes in Git ==="
echo "100644 - Regular file"
echo "100755 - Executable file"
echo "120000 - Symbolic link"
echo "040000 - Directory (tree)"

# Create files with different modes
echo ""
echo "=== Creating Files with Different Modes ==="
echo "regular file" > regular.txt
echo "#!/bin/bash" > executable.sh
echo "echo 'executable'" >> executable.sh
chmod +x executable.sh

# Note: Symbolic links on Windows may not work the same way
# On Unix systems: ln -s regular.txt symbolic_link
# On Windows, we'll skip symbolic link creation

git add regular.txt executable.sh
echo ""
echo "=== Files in Index After Adding ==="
git ls-files --stage

echo ""
echo "=== Manipulating the Index Directly ==="

# Add file to index without working directory
echo "Index-only content" | git hash-object -w --stdin > temp_hash.txt
BLOB_HASH=$(cat temp_hash.txt)
git update-index --add --cacheinfo 100644 $BLOB_HASH index-only.txt

# Show index content
echo ""
echo "Index content including index-only.txt:"
git ls-files --stage | grep index-only.txt

# The file exists in index but not in working directory
echo ""
echo "=== Checking Working Directory ==="
if [ -f "index-only.txt" ]; then
    echo "File exists in working directory"
else
    echo "File exists in index but NOT in working directory"
fi

git status

echo ""
echo "=== Advanced Index Operations ==="

# Partial staging (patch mode)
echo -e "Line 1\nLine 2\nLine 3\nLine 4\nLine 5" > partial.txt
git add partial.txt
git commit -m "Initial partial file"

# Modify multiple lines
sed -i 's/Line 2/Modified Line 2/' partial.txt 2>/dev/null || \
    (echo -e "Line 1\nModified Line 2\nLine 3\nLine 4\nLine 5" > partial.txt)
sed -i 's/Line 4/Modified Line 4/' partial.txt 2>/dev/null || \
    (echo -e "Line 1\nModified Line 2\nLine 3\nModified Line 4\nLine 5" > partial.txt)

echo ""
echo "=== Index Operations Complete ==="
echo "Use 'git add -p partial.txt' for interactive patch mode"
echo "Use 'git reset HEAD~1 -- partial.txt' to unstage specific file"
echo "Use 'git reset --mixed HEAD~1' to reset index but keep working dir"
echo "Use 'git reset --soft HEAD~1' to reset only HEAD pointer"
echo "Use 'git reset --hard HEAD~1' to reset everything"

# Cleanup
rm -f temp_hash.txt

