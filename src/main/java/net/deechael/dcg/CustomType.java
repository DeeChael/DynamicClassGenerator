package net.deechael.dcg;

final class CustomType implements JType {

    private final String typeString;

    CustomType(String typeString) {
        this.typeString = typeString;
    }

    @Override
    public String typeString() {
        return this.typeString;
    }

}
