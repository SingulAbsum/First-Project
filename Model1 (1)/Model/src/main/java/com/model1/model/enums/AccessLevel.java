package main.java.com.model1.model.enums;

public enum AccessLevel {
    CASHIER(1),
    MANAGER(2),
    ADMINISTRATOR(3);

    private final int value;

    AccessLevel(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static AccessLevel fromValue(int value) {
        for (AccessLevel accessLevel : values()) {
            if (accessLevel.value == value) {
                return accessLevel;
            }
        }
        throw new IllegalArgumentException("Unknown access level: " + value);
    }
}
