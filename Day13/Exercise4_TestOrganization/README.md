# Exercise 4: Test Organization & Structure

## Mô tả
Bài tập này demo test organization với @Nested classes, @Tag, và conditional testing.

## Nội dung
- UserServiceTest: Demo nested test classes với hierarchical organization
- PerformanceTest: Demo test tags (@Tag) cho test classification
- ConditionalTestingTest: Demo conditional test execution với @EnabledIf, @DisabledIf

## Chạy tests
```bash
mvn test -Dtest=com.xink.training.day13.exercise4.*
```

## Chạy tests với tags
```bash
# Chỉ chạy unit tests
mvn test -Dtest=com.xink.training.day13.exercise4.* -Dgroups=unit

# Chỉ chạy fast tests
mvn test -Dtest=com.xink.training.day13.exercise4.* -Dgroups=fast
```

