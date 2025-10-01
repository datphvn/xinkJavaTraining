import java.io.*;

public class FileStatePersistence<S extends Enum<S>> implements StatePersistence<S> {
    private final String filePath;
    private final Class<S> stateClass;

    public FileStatePersistence(String filePath, Class<S> stateClass) {
        this.filePath = filePath;
        this.stateClass = stateClass;
    }

    @Override
    public void saveState(S state) {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(state.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public S loadState() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            return Enum.valueOf(stateClass, line);
        } catch (Exception e) {
            return null;
        }
    }
}
