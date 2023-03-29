package unsw.blackout.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.FileInfoResponse;

public class Disk {
    private Map<String, File> allFiles = new HashMap<>();

    public void addFile(String fileName, String content) {
        allFiles.put(fileName, new File(fileName, content));
    }

    public void addFile(String fileName, File file) {
        allFiles.put(fileName, file);
    }

    public File getFile(String fileName) {
        return allFiles.get(fileName);
    }

    public void updateFile(String fileName, String content) {
        allFiles.get(fileName).updateContent(content);
    }

    public void removeInvalidFile(String fileName) {
        if (!allFiles.get(fileName).isComplete()) {
            allFiles.remove(fileName);
        }
    }

    public List<File> getIncompleteFiles() {
        List<File> res = new ArrayList<>();
        for (File file : allFiles.values()) {
            if (!file.isComplete()) {
                res.add(file);
            }
        }
        return res;
    }

    public Map<String, FileInfoResponse> getFilesInfo() {
        HashMap<String, FileInfoResponse> res = new HashMap<>();
        for (File file : allFiles.values()) {
            String fileName = file.getFileName();
            String data = file.getContent();
            int fileSize = file.getFileSize();
            res.put(fileName, new FileInfoResponse(fileName, data, fileSize, file.isComplete()));
        }
        return res;
    }
}
