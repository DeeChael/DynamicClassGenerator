package net.deechael.library.dcg.dynamic.items;

public class ConstructorVar extends Var {

    private final String constructorString;

    /**
     * To create a var with constructor of String
     *
     * Example: Var var = new ConstructorVar(java.lang.String.class, "testing", "new String(\"Just for a test!\")")
     * Compiled to class: java.lang.String.class jvar_testing = new String("Just for a test!");
     *
     * Suggestion:
     * Do not use arguments whose class is not java-original class
     * Please use: String, int, boolean, byte, long, short, char, float, double, etc.
     *
     * @param type Var type
     * @param name the name of the var, will be added a prefix "jvar_" automatically
     * @param constructorString @see Suggestion
     */
    public ConstructorVar(Class<?> type, String name, String constructorString) {
        super(type, name);
        this.constructorString = constructorString;
    }

    @Override
    public String varString() {
        return constructorString;
    }

}
