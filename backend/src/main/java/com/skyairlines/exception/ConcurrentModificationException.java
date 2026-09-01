package com.skyairlines.exception;

public class ConcurrentModificationException extends RuntimeException {

    private final String entityName;
    private final Object entityId;
    private final int expectedVersion;
    private final int actualVersion;

    public ConcurrentModificationException(String entityName, Object entityId, int expectedVersion, int actualVersion) {
        super(String.format("Concurrent modification detected for %s with ID %s. Expected version: %d, Actual version: %d",
                entityName, entityId, expectedVersion, actualVersion));
        this.entityName = entityName;
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    public ConcurrentModificationException(String message, String entityName, Object entityId, int expectedVersion, int actualVersion) {
        super(message);
        this.entityName = entityName;
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    public ConcurrentModificationException(String message, Throwable cause, String entityName, Object entityId, int expectedVersion, int actualVersion) {
        super(message, cause);
        this.entityName = entityName;
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }

    public int getExpectedVersion() {
        return expectedVersion;
    }

    public int getActualVersion() {
        return actualVersion;
    }
}