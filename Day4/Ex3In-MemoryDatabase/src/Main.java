import java.util.*;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();

        // ---------------------------
        // Create table users(id INT, name STRING, age INT)
        Schema userSchema = new Schema();
        userSchema.addColumn("id", DataType.INTEGER);
        userSchema.addColumn("name", DataType.STRING);
        userSchema.addColumn("age", DataType.INTEGER);
        db.createTable("users", userSchema);
        Table users = db.getTable("users");

        // Insert rows
        Map<String,Object> r1 = Map.of("id", 1, "name", "Alice", "age", 30);
        Map<String,Object> r2 = Map.of("id", 2, "name", "Bob", "age", 25);
        Map<String,Object> r3 = Map.of("id", 3, "name", "Carol", "age", 40);
        users.insert(r1, null);
        users.insert(r2, null);
        users.insert(r3, null);

        // Create index on name for fast lookup
        users.createIndex("name");

        // Select where name = 'Bob'
        System.out.println("Select Bob:");
        List<Map<String,Object>> bob = db.select("users", List.of("id","name","age"), Map.of("name","Bob"));
        System.out.println(bob);

        // Aggregation
        System.out.println("AVG age: " + db.aggregate("users", "age", "AVG", null));
        System.out.println("COUNT: " + db.aggregate("users", "age", "COUNT", null));
        System.out.println("SUM age: " + db.aggregate("users", "age", "SUM", null));

        // Update: increment age of Bob
        users.update(Map.of("name","Bob"), Map.of("age", 26), null);
        System.out.println("After update Bob: " + db.select("users", List.of("id","name","age"), Map.of("name","Bob")));

        // Transaction demo
        Transaction tx = db.beginTransaction();
        users.insert(Map.of("id",4,"name","Dave","age",28), tx);
        users.delete(Map.of("name","Alice"), tx);
        System.out.println("Before rollback, all users: ");
        users.getAllRows().forEach(System.out::println);
        tx.rollback();
        System.out.println("After rollback, all users: ");
        users.getAllRows().forEach(System.out::println);

        // Join example: create table orders(userId INT, amount DOUBLE)
        Schema orderSchema = new Schema();
        orderSchema.addColumn("orderId", DataType.INTEGER);
        orderSchema.addColumn("userId", DataType.INTEGER);
        orderSchema.addColumn("amount", DataType.DOUBLE);
        db.createTable("orders", orderSchema);
        Table orders = db.getTable("orders");
        orders.insert(Map.of("orderId",101,"userId",1,"amount",50.0), null);
        orders.insert(Map.of("orderId",102,"userId",2,"amount",75.0), null);
        orders.insert(Map.of("orderId",103,"userId",1,"amount",20.0), null);

        // join users.id == orders.userId
        System.out.println("Join users & orders on id=userId:");
        List<Map<String,Object>> joined = db.join("users","orders","id","userId");
        joined.forEach(System.out::println);

        // Aggregation with where
        System.out.println("SUM amount where userId=1: " + db.aggregate("orders","amount","SUM", Map.of("userId",1)));

        System.out.println("Demo complete.");
    }
}
