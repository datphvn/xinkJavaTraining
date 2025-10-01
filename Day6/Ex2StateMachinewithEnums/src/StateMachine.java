import java.util.function.Consumer;

public interface StateMachine<S extends Enum<S>, C> {
    S getCurrentState();
    boolean transitionTo(S newState, C context);
    void addTransition(S from, S to);
    void onEnter(S state, Consumer<C> action);
    void onExit(S state, Consumer<C> action);
}
