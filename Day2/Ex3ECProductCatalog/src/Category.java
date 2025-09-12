
import java.util.ArrayList;
import java.util.List;

public class Category extends CatalogComponent {
    private final String name;
    private final List<CatalogComponent> children = new ArrayList<>();

    public Category(String name) { this.name = name; }

    @Override
    public void add(CatalogComponent c) { children.add(c); }

    @Override
    public void remove(CatalogComponent c) { children.remove(c); }

    @Override
    public List<CatalogComponent> getChildren() { return children; }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Category: " + name);
        for (CatalogComponent c : children) c.display(indent + "  ");
    }

    public String getName() { return name; }
}
