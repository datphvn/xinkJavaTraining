package datpv.example.backup.sync;

import java.nio.file.Path;

public class SyncConflict {
    public final Path source;
    public final Path target;

    public SyncConflict(Path s, Path t) { this.source = s; this.target = t; }
}
