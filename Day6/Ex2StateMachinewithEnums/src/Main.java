enum OrderState {
    CREATED, PAID, SHIPPED, DELIVERED, CANCELLED
}

class OrderContext {
    String orderId;
    OrderContext(String id) { this.orderId = id; }
}

public class Main {
    public static void main(String[] args) {
        HierarchicalStateMachine<OrderState, OrderContext> sm =
                new HierarchicalStateMachine<>(OrderState.CREATED);

        // Transitions
        sm.addTransition(OrderState.CREATED, OrderState.PAID);
        sm.addTransition(OrderState.PAID, OrderState.SHIPPED);
        sm.addTransition(OrderState.SHIPPED, OrderState.DELIVERED);
        sm.addTransition(OrderState.CREATED, OrderState.CANCELLED);

        // Behaviors
        sm.onEnter(OrderState.PAID, ctx -> System.out.println(ctx.orderId + " -> Payment received"));
        sm.onEnter(OrderState.DELIVERED, ctx -> System.out.println(ctx.orderId + " -> Order delivered"));
        sm.onExit(OrderState.CREATED, ctx -> System.out.println(ctx.orderId + " -> Leaving CREATED"));

        OrderContext ctx = new OrderContext("ORD123");

        sm.transitionTo(OrderState.PAID, ctx);
        sm.transitionTo(OrderState.SHIPPED, ctx);
        sm.transitionTo(OrderState.DELIVERED, ctx);

        // Persistence
        StatePersistence<OrderState> persistence = new FileStatePersistence<>("state.txt", OrderState.class);
        persistence.saveState(sm.getCurrentState());
        System.out.println("Saved state: " + sm.getCurrentState());

        OrderState recovered = persistence.loadState();
        System.out.println("Recovered state: " + recovered);
    }
}
