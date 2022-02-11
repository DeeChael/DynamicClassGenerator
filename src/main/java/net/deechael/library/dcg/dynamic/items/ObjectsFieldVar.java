package net.deechael.library.dcg.dynamic.items;

public class ObjectsFieldVar extends Var {

    private final String varName;
    private final String fieldName;

        public ObjectsFieldVar(Class<?> type, String varName, String fieldName) {
        super(type, null);
        this.varName = varName;
        this.fieldName = fieldName;
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return varName + "." + fieldName;
    }

}
