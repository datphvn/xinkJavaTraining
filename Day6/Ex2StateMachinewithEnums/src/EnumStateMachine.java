import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.Consumer;

public class EnumStateMachine<S extends Enum<S>, C> implements StateMachine<S, C> {
    private S currentState;
    private final Map<S, Set<S>> transitions = new HashMap<>();
    private final Map<S, Consumer<C>> onEnter = new HashMap<>();
    private final Map<S, Consumer<C>> onExit = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    public EnumStateMachine(S initialState) {
        this.currentState = initialState;
    }

    @Override
    public S getCurrentState() {
        lock.lock();
        try {
            return currentState;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addTransition(S from, S to) {
        transitions.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }

    @Override
    public void onEnter(S state, Consumer<C> action) {
        onEnter.put(state, action);
    }

    @Override
    public void onExit(S state, Consumer<C> action) {
        onExit.put(state, action);
    }

    @Override
    public boolean transitionTo(S newState, C context) {
        lock.lock();
        try {
            if (!transitions.getOrDefault(currentState, Collections.emptySet()).contains(newState)) {
                return false; // invalid transition
            }

            // Exit behavior
            if (onExit.containsKey(currentState)) {
                onExit.get(currentState).accept(context);
            }

            // Change state
            currentState = newState;

            // Enter behavior
            if (onEnter.containsKey(newState)) {
                onEnter.get(newState).accept(context);
            }

            return true;
        } finally {
            lock.unlock();
        }
    }
}
