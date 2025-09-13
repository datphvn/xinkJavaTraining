import java.io.Serializable;

public class Profile implements Serializable {
    private String displayName;
    private String bio;
    private String location;

    public Profile(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return displayName + (bio != null ? " - " + bio : "");
    }
}
