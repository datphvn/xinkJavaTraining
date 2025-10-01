public interface StatePersistence<S extends Enum<S>> {
    void saveState(S state);
    S loadState();
}
