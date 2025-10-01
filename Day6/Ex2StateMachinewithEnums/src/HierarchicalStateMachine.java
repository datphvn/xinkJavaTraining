import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HierarchicalStateMachine<S extends Enum<S>, C> extends EnumStateMachine<S, C> {
    private final Map<S, StateMachine<?, C>> nestedMachines = new HashMap<>();

    public HierarchicalStateMachine(S initialState) {
        super(initialState);
    }

    public <NS extends Enum<NS>> void addNested(S parent, StateMachine<NS, C> child) {
        nestedMachines.put(parent, child);
    }

    public Optional<StateMachine<?, C>> getNested(S state) {
        return Optional.ofNullable(nestedMachines.get(state));
    }
}
