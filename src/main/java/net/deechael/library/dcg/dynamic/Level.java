package net.deechael.library.dcg.dynamic;

public enum Level {

    PRIVATE("private"), PUBLIC("public"), PROTECTED("protected"), UNNAMED("");

    private final String levelString;

    Level(String levelString) {
        this.levelString = levelString;
    }

    public String getString() {
        return levelString;
    }

}
