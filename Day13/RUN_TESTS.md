# Hướng dẫn chạy tests

## Yêu cầu
- Java 17 hoặc cao hơn
- Maven 3.6+ (hoặc sử dụng Maven Wrapper)

## Cài đặt Maven (nếu chưa có)

### Windows
1. Tải Maven từ: https://maven.apache.org/download.cgi
2. Giải nén vào thư mục (ví dụ: C:\Program Files\Apache\maven)
3. Thêm vào PATH: C:\Program Files\Apache\maven\bin
4. Kiểm tra: `mvn -version`

## Chạy tests

### Chạy tất cả tests
```bash
mvn test
```

### Chạy tests theo exercise
```bash
# Exercise 1: JUnit 5 Architecture
mvn test -Dtest=com.xink.training.day13.exercise1.*

# Exercise 2: Parameterized Testing
mvn test -Dtest=com.xink.training.day13.exercise2.*

# Exercise 3: Dynamic Testing
mvn test -Dtest=com.xink.training.day13.exercise3.*

# Exercise 4: Test Organization
mvn test -Dtest=com.xink.training.day13.exercise4.*

# Exercise 5: Mocking
mvn test -Dtest=com.xink.training.day13.exercise5.*

# Mini Project
mvn test -Dtest=com.xink.training.day13.miniproject.*
```

### Chạy test cụ thể
```bash
mvn test -Dtest=CalculatorTest
```

### Chạy với tags
```bash
# Chỉ chạy unit tests
mvn test -Dgroups=unit

# Chỉ chạy fast tests
mvn test -Dgroups=fast
```

## Compile project
```bash
mvn clean compile
```

## Compile tests
```bash
mvn test-compile
```

## Xem test report
Sau khi chạy tests, xem report tại:
`target/surefire-reports/index.html`

## Troubleshooting

### Lỗi: mvn không được nhận diện
- Kiểm tra Maven đã được cài đặt: `mvn -version`
- Kiểm tra PATH environment variable
- Restart terminal/IDE sau khi cài Maven

### Lỗi: Java version không đúng
- Kiểm tra Java version: `java -version`
- Cần Java 17 hoặc cao hơn
- Set JAVA_HOME environment variable

### Lỗi: Dependencies không tải được
- Kiểm tra internet connection
- Xóa `.m2/repository` và chạy lại `mvn clean install`

