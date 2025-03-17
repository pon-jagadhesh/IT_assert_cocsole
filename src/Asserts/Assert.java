package Asserts;

import java.time.LocalDateTime;

public class Assert {
    private int assetId;
    private String name;
    private String brandName;
    private String description;
    public Status status;
    private String lastUpdated;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    private int credits;

    // Enum for status field
    public enum Status {
        AVAILABLE("Available"),
        UNDER_MAINTENANCE("Under Maintenance"),
        OUT_OF_SERVICE("Out of Service");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    // Constructor
    public Assert(String name, Status status,String brandName,String description,int credits) {
        this.name = name;
        this.status = status;
        this.brandName = brandName;
        this.description = description;
        this.credits = credits;
        this.lastUpdated = LocalDateTime.now().toString();
    }

    // Default Constructor
    public Assert() {}

    // Getters and Setters
    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "assetId=" + assetId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}

