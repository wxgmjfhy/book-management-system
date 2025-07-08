package main.java.enums;

public enum BookCategory {
    COMPUTER("计算机"),
    LITERATURE("文学"),
    HISTORY("历史"),
    SCIENCE("科学"),
    ART("艺术"),
    OTHER("其他");

    private final String label;

    BookCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}