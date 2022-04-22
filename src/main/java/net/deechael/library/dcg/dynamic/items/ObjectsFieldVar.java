package net.deechael.library.dcg.dynamic.items;

class ObjectsFieldVar extends Var {

    private final String varName;
    private final String fieldName;

        public ObjectsFieldVar(String varName, String fieldName) {
        super(null, null);
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
