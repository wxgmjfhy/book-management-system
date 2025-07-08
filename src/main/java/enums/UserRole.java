package main.java.enums;

public enum UserRole {
    STUDENT("学生"),
    ADMIN("管理员");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
