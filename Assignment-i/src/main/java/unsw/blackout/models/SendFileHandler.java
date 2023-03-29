package unsw.blackout.models;

import unsw.blackout.BlackoutController;

public class SendFileHandler {
    private Entity sender;
    private Entity recipient;
    private File file;
    private final BlackoutController controller;

    public SendFileHandler(Entity sender, Entity recipient, File file, BlackoutController controller) {
        this.sender = sender;
        this.recipient = recipient;
        this.file = file;
        this.controller = controller;

        // create file in recipient
        String fileName = file.getFileName();
        recipient.getDisk().addFile(fileName, new File(fileName, "", file.getFileSize()));
    }

    public Entity getSender() {
        return sender;
    }

    public Entity getReciver() {
        return recipient;
    }

    public void transfer() {
        if (!sender.communicableWith(recipient)) {
            // the partially downloaded file should be removed from the recipient.
            Disk recipientDisk = recipient.getDisk();
            recipientDisk.removeInvalidFile(file.getFileName());
        } else {
            // fairness and evenly allocate bandwidth
            int senderBandwidth = controller.evenlySenderBandwidth(sender);
            int recipientBandwidth = controller.evenlyReceiveBandwidth(recipient);
            int bandwidth = Math.min(senderBandwidth, recipientBandwidth);
            // update recipient file
            Disk recipientDisk = recipient.getDisk();

            String fileName = file.getFileName();
            String originalContent = file.getContent();
            File target = recipientDisk.getFile(fileName);
            String targetContent = target.getContent();
            int nextTargetContentLength = targetContent.length() + bandwidth;
            int contentLength = Math.min(originalContent.length(), nextTargetContentLength);

            String temp = originalContent.substring(0, contentLength);

            recipientDisk.updateFile(file.getFileName(), temp);
        }
    }
}
