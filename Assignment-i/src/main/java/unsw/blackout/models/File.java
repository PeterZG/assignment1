package unsw.blackout.models;

public class File {
    private String fileName;
    private String content;
    private final int fileSize;

    public File(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.fileSize = content.length();
    }

    public File(String fileName, String content, int fileSize) {
        this.fileName = fileName;
        this.content = content;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getContent() {
        return this.content;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isComplete() {
        return this.fileSize == this.content.length();
    }
}
