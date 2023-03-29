< Put your links to your blog post(s) here >

All objects are entities, whether they are devices or satellites. All entities have a location, height, and speed. Devices and satellites inherit from Entity. Considering that entities need to communicate with each other, each entity also has a communicable range.

Different entities have different movement strategies, and here we use the Strategy Pattern. Each entity has a MoveStrategy for calculating the next position.

Different types of devices are similar, and the only difference is the communicable range, so the only difference between different device classes is the constructor.

When the device is immovable, the device as an entity is a MovelessStrategy, and the next position is still the current position. The implementation of entity movement adopts the Strategy Pattern, so the function for calculating the next position is loosely coupled with the entities. Either CLOCKWISE or ANTI CLOCKWISE movement is implemented, and a bidirectional movement strategy is used to calculate the next position by specifying the direction. CLOCKWISE movement is just setting the direction of the bidirectional movement class to CLOCKWISE. For RelaySatellite and TeleportingSatellite, we only need to add the code for when to change direction. This part of the movement strategy conforms to the open-closed principle.

Design a File class to express the size and actual content of the file, and design a Disk class to store files. A disk with limited size and number of files can be derived from the Disk class. However, the sub-classes have additional methods that the parent class does not have, resulting in runtime type judgments and forced type conversions when used.

File transmission is essentially communication between two disk classes, and the SendFileHandler class handles file transfer between the two disks. The disks are not aware of how the transfer is conducted; they only provide file access, addition, and update.

The movement strategy part of this project is a well-designed part, but the method interface of the Disk class and its subclasses is not fully unified, resulting in type conversions when used. By applying the design patterns learned from the course, I improved my code design, helped decouple a part of the code, and laid the foundation for future extensions. It was difficult to decide whether the Controller should manage all entities uniformly or manage devices and satellites separately. If we need to manage spaceships in the future, for example, I only used a data structure in the Controller to express all entities.
