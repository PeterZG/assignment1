package unsw.blackout.models;

public class SpaceLimitedDisk extends Disk {
    private final int maxFiles;
    private final int maxStorage;
    private int numFiles = 0;
    private int used = 0;

    public SpaceLimitedDisk(int maxFiles, int maxStorage) {
        this.maxFiles = maxFiles;
        this.maxStorage = maxStorage;
    }

    public boolean maxFilesReached() {
        return numFiles == maxFiles ? true : false;
    }

    public boolean maxStorageReached(File file) {
        if (file.getFileSize() + used > maxStorage) {
            return true;
        }
        return false;
    }

    /* make sure have room for add new file */
    public void addFile(String fileName, File file) {
        super.addFile(fileName, file);
        this.numFiles += 1;
        this.used += file.getFileSize();
    }
}
