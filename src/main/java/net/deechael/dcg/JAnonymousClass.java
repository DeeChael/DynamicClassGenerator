package net.deechael.dcg;

import net.deechael.dcg.body.Operation;
import net.deechael.dcg.items.Var;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class JAnonymousClass implements Var {

    private final String type;
    private final Var[] arguments;

    private final List<JField> fields = new ArrayList<>();
    private final List<JMethod> methods = new ArrayList<>();

    private final List<JClass> innerClasses = new ArrayList<>();

    public JAnonymousClass(@NotNull Class<?> type, @NotNull Var[] arguments) {
        this.type = type.getName();
        this.arguments = arguments;
    }

    public JAnonymousClass(@NotNull JGeneratable type, @NotNull Var[] arguments) {
        this.type = type.getName();
        this.arguments = arguments;
    }

    public JField addField(Level level, Class<?> type, String name) {
        name = "jfield_" + name;
        JField field = new JField(level, type, null, name, false, false);
        this.fields.add(field);
        return field;
    }

    public JField addFinalField(Level level, Class<?> type, String name, Var initialedValue) {
        name = "jfield_" + name;
        JField field = new JField(level, type, null, name, true, false);
        field.initialize(initialedValue);
        this.fields.add(field);
        return field;
    }

    public JMethod addMethod(Level level, String name, boolean isFinal, boolean isSynchronized) {
        return this.addMethod(void.class, level, name, isFinal, isSynchronized);
    }

    public JMethod addMethod(Class<?> returnType, Level level, String name, boolean isFinal, boolean isSynchronized) {
        JMethod method = new JMethod(returnType, level, null, name, false, isFinal, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public void addInner(JClass generatable) {
        generatable.setInner();
        generatable.level = Level.UNNAMED;
        this.innerClasses.add(generatable);
    }

    @Override
    public Class<?> getType() {
        throw new RuntimeException("No type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("No name");
    }

    @Override
    public String varString() {
        StringBuilder base = new StringBuilder();
        base.append("(new ").append(this.type).append("(");
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        base.append(bodyBuilder).append(") {\n");
        fields.forEach(field -> base.append(field.getString()).append("\n"));
        methods.forEach(method -> base.append(method.getString()).append("\n"));
        innerClasses.forEach(clazz -> base.append(clazz.getString()).append("\n"));
        return base.append("})").toString();
    }
}
