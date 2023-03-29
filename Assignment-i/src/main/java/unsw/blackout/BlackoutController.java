package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.models.Device;
import unsw.blackout.models.Disk;
import unsw.blackout.models.Entity;
import unsw.blackout.models.File;
import unsw.blackout.models.SpaceLimitedDisk;
import unsw.blackout.models.RelaySatellite;
import unsw.blackout.models.Satellite;
import unsw.blackout.models.SendFileHandler;
import unsw.blackout.models.Slope;
import unsw.blackout.models.SpaceBandwidthLimitedDisk;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;
import static unsw.blackout.FileTransferException.*;

public class BlackoutController {
    private Map<String, Entity> entities = new HashMap<>();
    private List<SendFileHandler> handlers = new ArrayList<>();
    private List<Slope> allSlopes = new ArrayList<>();

    public void createDevice(String deviceId, String type, Angle position) {
        entities.put(deviceId, Entity.createEntity(deviceId, type, MathsHelper.RADIUS_OF_JUPITER, position));
    }

    public void removeDevice(String deviceId) {
        entities.remove(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        entities.put(satelliteId, Entity.createEntity(satelliteId, type, height, position));
    }

    public void removeSatellite(String satelliteId) {
        entities.remove(satelliteId);
    }

    public List<String> listDeviceIds() {
        ArrayList<String> res = new ArrayList<>();
        for (Entity e : entities.values()) {
            if (e instanceof Device) {
                res.add(e.getId());
            }
        }
        return res;
    }

    public List<String> listSatelliteIds() {
        ArrayList<String> res = new ArrayList<>();
        for (Entity e : entities.values()) {
            if (e instanceof Satellite) {
                res.add(e.getId());
            }
        }
        return res;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        ((Device) entities.get(deviceId)).createFile(filename, content);
    }

    public EntityInfoResponse getInfo(String id) {
        Entity e = entities.get(id);
        return new EntityInfoResponse(id, e.getPosition(), e.getHeight(), e.getType(), e.getFilesInfo());
    }

    public void updateSendFiles() {
        for (SendFileHandler handler : handlers) {
            handler.transfer();
        }
    }

    public void simulate() {
        for (Entity e : entities.values()) {
            e.next();
        }
        updateSendFiles();
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> entitiesInRange(String id) {
        List<String> res = new ArrayList<>();

        Entity choose = entities.get(id);
        for (Entity e : entities.values()) {
            if (e.getId().equals(id)) {
                continue;
            }

            if (choose.communicableWith(e)) {
                res.add(e.getId());
            }
        }

        return res;
    }

    public Map<String, List<String>> relations() {
        Map<String, List<String>> graph = new HashMap<>();
        for (Entity e : entities.values()) {
            graph.put(e.getId(), entitiesInRange(e.getId()));
        }
        return graph;
    }

    public void findAllConnectable(Map<String, List<String>> graph, String id, List<String> allEntitiesId) {
        List<String> connectableList = graph.get(id);
        for (String anotherId : connectableList) {
            if (anotherId.equals(id))
                continue;
            if (!allEntitiesId.contains(anotherId)) {
                allEntitiesId.add(anotherId);

                if (entities.get(anotherId) instanceof RelaySatellite) {
                    findAllConnectable(graph, anotherId, allEntitiesId);
                }
            }
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        List<String> res = new ArrayList<>();

        Map<String, List<String>> graph = relations();
        findAllConnectable(graph, id, res);

        return res;
    }

    /* A helper function */
    private int satelliteMaxSendBandwidth(Entity satellite) {
        SpaceBandwidthLimitedDisk disk = (SpaceBandwidthLimitedDisk) satellite.getDisk();
        return disk.getMaxSendBandwidth();
    }

    /* A helper function */
    private int satelliteMaxReceiveBandwidth(Entity satellite) {
        SpaceBandwidthLimitedDisk disk = (SpaceBandwidthLimitedDisk) satellite.getDisk();
        return disk.getMaxReceiveBandwidth();
    }

    public int evenlySenderBandwidth(Entity sender) {
        if (sender instanceof Device) {
            return Integer.MAX_VALUE;
        }
        var allSender = findHandlersBySender(sender);
        var numSender = allSender.size();

        return satelliteMaxSendBandwidth(sender) / numSender;
    }

    public int evenlyReceiveBandwidth(Entity receiver) {
        if (receiver instanceof Device) {
            return Integer.MAX_VALUE;
        }
        var allReceiver = findHandlersByReceiver(receiver);
        var numReceiver = allReceiver.size();

        return satelliteMaxReceiveBandwidth(receiver) / numReceiver;
    }

    public List<SendFileHandler> findHandlersBySender(Entity e) {
        List<SendFileHandler> res = new ArrayList<>();

        for (SendFileHandler h : handlers) {
            if (h.getSender() == e) {
                res.add(h);
            }
        }

        return res;
    }

    public List<SendFileHandler> findHandlersByReceiver(Entity e) {
        List<SendFileHandler> res = new ArrayList<>();

        for (SendFileHandler h : handlers) {
            if (h.getReciver() == e) {
                res.add(h);
            }
        }

        return res;
    }

    public boolean haveBandwithForSender(Entity e) {
        if (e instanceof Device) {
            return true;
        }

        int numSender = findHandlersBySender(e).size();

        SpaceBandwidthLimitedDisk disk = (SpaceBandwidthLimitedDisk) e.getDisk();
        if (disk.getMaxSendBandwidth() == numSender) {
            return false;
        }
        return true;
    }

    public boolean haveBandwithForReceiver(Entity e) {
        if (e instanceof Device) {
            return true;
        }

        int numReceiver = findHandlersByReceiver(e).size();

        SpaceBandwidthLimitedDisk disk = (SpaceBandwidthLimitedDisk) e.getDisk();
        if (disk.getMaxReceiveBandwidth() == numReceiver) {
            return false;
        }
        return true;
    }

    public void handleSendFileExceptions(String fileName, String fromId, String toId) throws FileTransferException {
        Entity from = entities.get(fromId);
        Entity to = entities.get(toId);

        File originalFile = from.getFile(fileName);
        if (originalFile == null || !originalFile.isComplete()) {
            throw new VirtualFileNotFoundException(fileName);
        }

        if (to.getFile(fileName) != null) {
            throw new VirtualFileAlreadyExistsException(fileName);
        }

        // if Satellite Bandwidth if full
        if (!haveBandwithForSender(from) || !haveBandwithForSender(to)) {
            throw new VirtualFileNoBandwidthException(fileName);
        }

        Disk recipientDisk = to.getDisk();
        if (recipientDisk instanceof SpaceLimitedDisk) {
            SpaceLimitedDisk disk = (SpaceLimitedDisk) recipientDisk;
            if (disk.maxFilesReached()) {
                throw new VirtualFileNoStorageSpaceException("Max Files Reached");
            }
            if (disk.maxStorageReached(originalFile)) {
                throw new VirtualFileNoStorageSpaceException("Max Storage Reached");
            }
        }
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        handleSendFileExceptions(fileName, fromId, toId);
        Entity from = entities.get(fromId);
        File file = from.getFile(fileName);

        handlers.add(new SendFileHandler(from, entities.get(toId), file, this));
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);

        if (isMoving) {
            Device device = (Device) entities.get(deviceId);
            device.setMoving(isMoving, allSlopes);
        }
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        allSlopes.add(new Slope(startAngle, endAngle, gradient));
    }
}
