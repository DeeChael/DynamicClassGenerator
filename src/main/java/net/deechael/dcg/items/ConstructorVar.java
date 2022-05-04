package net.deechael.dcg.items;

final class ConstructorVar implements Var {

    private final Class<?> type;
    private final String bodyString;

    public ConstructorVar(Class<?> type, String bodyString) {
        this.type = type;
        this.bodyString = bodyString;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "new " + getType().getName().replace("$", ".") + (hasTypes(this.type) ? "<>" : "") + "(" + bodyString + ")";
    }

    private boolean hasTypes(Class<?> clazz) {
        return clazz.getTypeParameters().length > 0;
    }

}
