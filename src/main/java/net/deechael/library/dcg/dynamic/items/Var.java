package net.deechael.library.dcg.dynamic.items;

public class Var {

    private final Class<?> type;

    private final String name;

    public Var(Class<?> type, String name) {
        this.type = type;
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String varString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Var var = (Var) o;

        if (type != null ? !type.getName().equals(var.type.getName()) : var.type != null) return false;
        return name.equals(var.name);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
