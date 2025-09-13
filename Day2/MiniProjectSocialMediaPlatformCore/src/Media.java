public class Media {
    public enum MediaType { IMAGE, VIDEO, AUDIO, OTHER }
    private final MediaType type;
    private final String url;

    public Media(MediaType type, String url) {
        this.type = type;
        this.url = url;
    }

    public MediaType getType() { return type; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        return "[" + type + "] " + url;
    }
}
