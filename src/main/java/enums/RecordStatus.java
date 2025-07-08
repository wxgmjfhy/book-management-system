package main.java.enums;

public enum RecordStatus {
    BORROWED("已借出"),
    RETURNED("已归还");

    private final String label;

    RecordStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
