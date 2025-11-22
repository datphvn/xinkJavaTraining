# Day 13: JUnit Testing Mastery - Complete Guide

## 📋 Tổng quan

Dự án này bao gồm tất cả các bài tập và ví dụ về JUnit 5 Testing từ cơ bản đến nâng cao.

## 📁 Cấu trúc dự án

```
Day13/
├── src/
│   ├── main/java/com/xink/training/day13/
│   │   ├── model/          # Domain models (User, Address)
│   │   └── service/        # Business logic services
│   └── test/java/com/xink/training/day13/
│       ├── exercise1/      # JUnit 5 Architecture & Fundamentals
│       ├── exercise2/        # Parameterized Testing
│       ├── exercise3/        # Dynamic Testing
│       ├── exercise4/        # Test Organization & Structure
│       ├── exercise5/        # Mocking & Test Doubles
│       └── miniproject/     # Comprehensive Test Suite
├── Exercise1_JUnit5Architecture/
├── Exercise2_ParameterizedTesting/
├── Exercise3_DynamicTesting/
├── Exercise4_TestOrganization/
├── Exercise5_Mocking/
└── MiniProject_ComprehensiveTestSuite/
```

## 🚀 Cài đặt và chạy

### Yêu cầu
- Java 17+
- Maven 3.6+

### Chạy tất cả tests
```bash
mvn test
```

### Chạy tests theo exercise
```bash
# Exercise 1
mvn test -Dtest=com.xink.training.day13.exercise1.*

# Exercise 2
mvn test -Dtest=com.xink.training.day13.exercise2.*

# Exercise 3
mvn test -Dtest=com.xink.training.day13.exercise3.*

# Exercise 4
mvn test -Dtest=com.xink.training.day13.exercise4.*

# Exercise 5
mvn test -Dtest=com.xink.training.day13.exercise5.*

# Mini Project
mvn test -Dtest=com.xink.training.day13.miniproject.*
```

## 📚 Nội dung học tập

### Exercise 1: JUnit 5 Architecture & Fundamentals
- Test Lifecycle (@BeforeAll, @BeforeEach, @AfterEach, @AfterAll)
- Test Information Injection (TestInfo, TestReporter)
- Advanced Assertions (JUnit 5 + AssertJ)
- Custom Assertions

### Exercise 2: Parameterized Testing
- @CsvSource, @ValueSource, @EnumSource
- @CsvFileSource, @MethodSource
- @ArgumentsSource với custom providers
- Advanced parameterized patterns

### Exercise 3: Dynamic Testing
- @TestFactory với Collection, Stream, Iterator
- Runtime test generation
- Configuration-based testing

### Exercise 4: Test Organization & Structure
- @Nested classes cho hierarchical organization
- @Tag cho test classification
- Conditional testing (@EnabledIf, @DisabledIf)
- Performance testing với @Timeout

### Exercise 5: Mocking & Test Doubles
- Advanced Mockito patterns
- Argument matchers và captors
- Sequential method calls
- Custom test doubles và fake implementations
- Interaction verification

## 🎯 Learning Objectives

Sau khi hoàn thành Day 13, bạn sẽ có thể:

✅ Hiểu rõ JUnit 5 architecture (Platform, Jupiter, Vintage)
✅ Sử dụng thành thạo test lifecycle annotations
✅ Viết comprehensive assertions với JUnit 5 và AssertJ
✅ Tạo parameterized tests với multiple sources
✅ Generate dynamic tests với @TestFactory
✅ Tổ chức tests với @Nested classes và @Tag
✅ Sử dụng Mockito hiệu quả với JUnit 5
✅ Tạo custom test doubles và fake objects
✅ Verify interactions và prevent unwanted calls

## 📝 Notes

- Tất cả các test classes đều có @DisplayName để dễ đọc
- Sử dụng AssertJ cho fluent assertions
- Mockito được tích hợp với JUnit 5 qua @ExtendWith(MockitoExtension.class)
- Test data được tổ chức trong src/test/resources

## 🔗 Tài liệu tham khảo

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/doc/)

