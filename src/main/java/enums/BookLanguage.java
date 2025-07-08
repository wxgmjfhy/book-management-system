package main.java.enums;

public enum BookLanguage {
    CHINESE("中文"),
    ENGLISH("英文");

    private final String label;

    BookLanguage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}