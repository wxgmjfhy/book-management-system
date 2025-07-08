package main.java.enums;

public enum BookStatus {
    AVAILABLE("可借阅"),
    BORROWED("已借出"),
    MAINTENANCE("维修中");

    private final String label;

    BookStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}