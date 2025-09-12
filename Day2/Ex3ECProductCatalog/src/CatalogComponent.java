
import java.util.List;

public abstract class CatalogComponent {
    public void add(CatalogComponent c) { throw new UnsupportedOperationException(); }
    public void remove(CatalogComponent c) { throw new UnsupportedOperationException(); }
    public List<CatalogComponent> getChildren() { throw new UnsupportedOperationException(); }
    public abstract void display(String indent);
}
