package net.deechael.dcg.items;

final class ConstructorVar extends Var {

    private final String bodyString;

    public ConstructorVar(Class<?> type, String bodyString) {
        super(type, null);
        this.bodyString = bodyString;
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "new " + getType().getName() + "(" + bodyString + ")";
    }

}
