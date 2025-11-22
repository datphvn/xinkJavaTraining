#!/bin/bash
# Exercise 2: Conflict Resolution Mastery
# Master complex merge conflict scenarios

echo "=== Exercise 2: Conflict Resolution Mastery ==="

WORK_DIR="conflict-mastery"
if [ -d "$WORK_DIR" ]; then
    echo "Directory already exists. Removing..."
    rm -rf "$WORK_DIR"
fi

mkdir "$WORK_DIR"
cd "$WORK_DIR"

echo ""
echo "=== Task 2.1: Create complex conflict scenario ==="

git init
git config user.name "Conflict Resolver"
git config user.email "resolver@example.com"

# Set up initial file
cat > application.properties << 'EOF'
# Application Configuration
server.port=8080
database.url=jdbc:mysql://localhost:3306/app
database.username=root
database.password=secret
logging.level=INFO
cache.enabled=true
EOF

git add application.properties
git commit -m "Initial configuration"
echo "✅ Created initial configuration"

echo ""
echo "=== Task 2.2: Create multiple conflicting branches ==="

# Branch 1: Database changes
git checkout -b feature/database-upgrade

# Use sed with backup for cross-platform compatibility
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    sed -i '' 's/mysql:\/\/localhost:3306\/app/postgresql:\/\/localhost:5432\/appdb/' application.properties
    sed -i '' 's/database.username=root/database.username=postgres/' application.properties
else
    # Linux
    sed -i 's/mysql:\/\/localhost:3306\/app/postgresql:\/\/localhost:5432\/appdb/' application.properties
    sed -i 's/database.username=root/database.username=postgres/' application.properties
fi

echo "database.schema=public" >> application.properties
git add application.properties
git commit -m "feat: upgrade to PostgreSQL"
echo "✅ Created feature/database-upgrade branch"

# Branch 2: Security enhancements  
git checkout main
git checkout -b feature/security-hardening

if [[ "$OSTYPE" == "darwin"* ]]; then
    sed -i '' 's/database.password=secret/database.password=${DB_PASSWORD}/' application.properties
    sed -i '' 's/logging.level=INFO/logging.level=WARN/' application.properties
else
    sed -i 's/database.password=secret/database.password=${DB_PASSWORD}/' application.properties
    sed -i 's/logging.level=INFO/logging.level=WARN/' application.properties
fi

echo "security.enabled=true" >> application.properties
echo "security.jwt.secret=${JWT_SECRET}" >> application.properties
git add application.properties
git commit -m "feat: add security configurations"
echo "✅ Created feature/security-hardening branch"

# Branch 3: Performance optimizations
git checkout main  
git checkout -b feature/performance-tuning

echo "cache.type=redis" >> application.properties  
echo "cache.ttl=3600" >> application.properties
echo "performance.pool.max=100" >> application.properties
git add application.properties
git commit -m "feat: add performance optimizations"
echo "✅ Created feature/performance-tuning branch"

echo ""
echo "=== Task 2.3: Complex three-way merge resolution ==="

git checkout main

# Merge first branch (should be clean)
echo ""
echo "Merging feature/database-upgrade..."
git merge feature/database-upgrade --no-edit
echo "✅ Merged database-upgrade"

# Merge second branch (will conflict)  
echo ""
echo "Merging feature/security-hardening (will create conflicts)..."
git merge feature/security-hardening --no-edit || {
    echo "⚠️  Merge conflict detected (expected)"
    echo ""
    echo "=== Conflict in application.properties ==="
    cat application.properties
    echo ""
    echo "=== Resolving conflict manually ==="
    
    # Resolve conflict by combining both changes
    cat > application.properties << 'RESOLVED_EOF'
# Application Configuration
server.port=8080
database.url=jdbc:postgresql://localhost:5432/appdb
database.username=postgres
database.password=${DB_PASSWORD}
database.schema=public
logging.level=WARN
cache.enabled=true
security.enabled=true
security.jwt.secret=${JWT_SECRET}
RESOLVED_EOF
    
    git add application.properties
    git commit -m "Merge feature/security-hardening and resolve conflicts"
    echo "✅ Resolved conflict and committed"
}

# Merge third branch (may conflict)
echo ""
echo "Merging feature/performance-tuning..."
git merge feature/performance-tuning --no-edit || {
    echo "⚠️  Merge conflict detected"
    # Add performance settings to existing file
    echo "cache.type=redis" >> application.properties
    echo "cache.ttl=3600" >> application.properties
    echo "performance.pool.max=100" >> application.properties
    git add application.properties
    git commit -m "Merge feature/performance-tuning and resolve conflicts"
    echo "✅ Resolved conflict and committed"
}

echo ""
echo "=== Final Merged Configuration ==="
cat application.properties

echo ""
echo "=== Task 2.4: Advanced conflict resolution techniques ==="

git checkout -b experiment-merge-strategies

echo ""
echo "=== Testing Different Merge Strategies ==="
echo ""
echo "Strategy 1: Ours (keep our version)"
echo "  git merge -s ours feature/security-hardening"
echo ""
echo "Strategy 2: Theirs (keep their version)"
echo "  git merge -s theirs feature/security-hardening"
echo ""
echo "Strategy 3: Prefer ours in conflicts"
echo "  git merge -X ours feature/security-hardening"
echo ""
echo "Strategy 4: Prefer theirs in conflicts"
echo "  git merge -X theirs feature/security-hardening"

echo ""
echo "=== Conflict Resolution Summary ==="
echo "✅ Created 3 conflicting branches"
echo "✅ Resolved complex three-way merge"
echo "✅ Demonstrated conflict resolution strategies"

echo ""
echo "=== Branch History ==="
git log --oneline --graph --all --max-count=15

cd ..
echo ""
echo "=== Exercise 2 Complete ==="
echo "Conflict resolution practice in: $WORK_DIR"
echo ""
echo "Practice commands:"
echo "  git checkout experiment-merge-strategies"
echo "  git merge -s ours feature/security-hardening"
echo "  git reset --hard HEAD~1"
echo "  git merge -X theirs feature/security-hardening"

