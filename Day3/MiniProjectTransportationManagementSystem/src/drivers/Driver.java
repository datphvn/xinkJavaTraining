package drivers;

public class Driver {
    private String name;
    private String licenseId;

    public Driver(String name, String licenseId) {
        this.name = name;
        this.licenseId = licenseId;
    }

    public String getName() { return name; }
    public String getLicenseId() { return licenseId; }
}
