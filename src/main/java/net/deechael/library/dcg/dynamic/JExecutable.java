package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.body.*;
import net.deechael.library.dcg.dynamic.items.*;
import net.deechael.library.dcg.function.ArgumentOnly;
import net.deechael.library.dcg.function.BigArgumentOnly;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executable body which is without parameters
 *
 * @author DeeChael
 * @since 1.0.0
 */
public abstract class JExecutable implements JObject {

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    protected final List<Operation> operations = new ArrayList<>();
    private final List<Class<?>> extraClasses = new ArrayList<>();
    
    final JClass parent;
    
    protected JExecutable(JClass parent) {
        this.parent = parent;
    }

    public Var createVar(@NotNull Class<?> type, @NotNull String name, @NotNull Var var) {
        name = "jvar_" + name;
        Var createdVar = new Var(type, name);
        operations.add(new CreateVar(type, name, var.varString()));
        return createdVar;
    }

    /**
     * Using a method in the executable body
     *
     * Tips: If you are using the method which you added by JMethod,
     *       please add "jmethod_" before your methodName because
     *       generated code will add the prefix automatically
     *
     * Generated code looks like: jvar_varName.methodName(arguments);
     *
     * @param var The var that you want to use the method,
     *            the method must be contained in the class of the var,
     *            or else will throw an exception
     * @param methodName The method that you want to use,
     *                   the method must be contained in the class of the var,
     *                   or else will throw an exception
     * @param arguments The arguments that the method needs,
     *                  is easier than reflection
     */
    public void usingMethod(@NotNull Var var, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        UsingMethod usingMethod = new UsingMethod(var.varString(), methodName, bodyBuilder.toString());
        operations.add(usingMethod);
    }

    /**
     * Using a static method
     *
     * Generated code looks like: Type.methodName(arguments);
     *
     * @param clazz The type has the static method
     * @param methodName The method you want to use
     * @param arguments The arguments that the method needs
     */
    public void usingMethod(Class<?> clazz, String methodName, Var... arguments) {
        if (!extraClasses.contains(clazz)) {
            extraClasses.add(clazz);
        }
        boolean hasMethod = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers())) {
                hasMethod = true;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        UsingStaticMethod usingMethod = new UsingStaticMethod(clazz.getName(), methodName, bodyBuilder.toString());
        operations.add(usingMethod);
    }

    /**
     * Using the method which implemented by parent class in child class
     * 
     * Generated code looks like: super.methodName(arguments);
     * 
     * @param methodName The method name implemented by parent class
     * @param arguments The arguments that the method needs
     */
    public void usingSuperMethod(String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        UsingMethod usingMethod = new UsingMethod("super", methodName, bodyBuilder.toString());
        operations.add(usingMethod);
    }

    /**
     * Using the method, will execute the method is implemented by this class, if this class didn't implement this method but parent class did, execute the method in parent class
     *
     * Generated code looks like: this.methodName(arguments);
     *
     * @param methodName The method name
     * @param arguments The arguments that the method needs
     */
    public void usingThisMethod(String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        UsingMethod usingMethod = new UsingMethod("this", methodName, bodyBuilder.toString());
        operations.add(usingMethod);
    }

    public <T extends Throwable> void tryCatch(Class<T> throwing, String throwableObjectName, ArgumentOnly<JExecutable> tryExecuting, BigArgumentOnly<JExecutable, Var> catchExecuting) {
        JExecutable4InnerStructure tryBody = new JExecutable4InnerStructure(this, this.parent);
        JExecutable4InnerStructure catchBody = new JExecutable4InnerStructure(this, this.parent);
        throwableObjectName = "jthrowable_" + throwableObjectName;
        Var var = new Var(throwing, throwableObjectName);
        tryExecuting.apply(tryBody);
        catchExecuting.apply(catchBody, var);
        TryAndCatch tryAndCatch = new TryAndCatch(throwing, throwableObjectName, tryBody, catchBody);
        this.operations.add(tryAndCatch);
    }

    /**
     * Using if-else block in executable body
     * ArgumentOnly is a functional interface
     * You can replace with lambda:
     *     (executable) -> {
     *         //Modify the executable body
     *         //Example: executable.usingMethod(...);
     *     }
     *
     * Check two vars with "=="
     *
     * Generated code looks like:
     *     if (var == isEqual) {
     *         The code generated by the JExecutable in ifExecuting
     *     } else {
     *         The code generated by the JExecutable in elseExecuting
     *     }
     *
     * @param var The var at the left of equal checker(==)
     * @param isEqual The var at the right of equal checker(==)
     * @param ifExecuting The executable body in if block
     * @param elseExecuting The executable body in else block
     */
    public void ifElse_Equal(Var var, Var isEqual, ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting) {
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        elseExecuting.apply(elseBody);
        IfAndElse ifAndElse = new IfAndElse(new EqualCheck(var, isEqual), ifBody, elseBody);
        operations.add(ifAndElse);
    }

    public void ifElse_BooleanVar(Var booleanTypeVar, ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting) {
        if (!(booleanTypeVar.getType() == boolean.class || booleanTypeVar.getType() == Boolean.class)) {
            throw new RuntimeException("The var is not boolean type var");
        }
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        elseExecuting.apply(elseBody);
        IfAndElse ifAndElse = new IfAndElse(new BooleanVarCheck(booleanTypeVar.varString()), ifBody, elseBody);
        operations.add(ifAndElse);
    }

    public void ifElse_Method(ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting, @NotNull Var var, @NotNull String methodName, Var... arguments) {
        Class<?> clazz = var.getType();
        boolean hasMethod = false;
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                    throw new RuntimeException("The return type of the method is not a boolean type!");
                }
                hasMethod = true;
                result = method;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + var.getType().getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i]);
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        elseExecuting.apply(elseBody);
        IfAndElse ifAndElse = new IfAndElse(new UsingMethodAsVar(var.varString(), result.getName(), bodyBuilder.toString()), ifBody, elseBody);
        operations.add(ifAndElse);
    }

    public void ifElse_StaticMethod(ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting, @NotNull Class<?> clazz, @NotNull String methodName, Var... arguments) {
        if (!extraClasses.contains(clazz)) {
            extraClasses.add(clazz);
        }
        boolean hasMethod = false;
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                    throw new RuntimeException("The return type of the method is not a boolean type!");
                }
                hasMethod = true;
                result = method;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i]);
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        elseExecuting.apply(elseBody);
        IfAndElse ifAndElse = new IfAndElse(new UsingStaticMethodAsVar(result, bodyBuilder.toString()), ifBody, elseBody);
        operations.add(ifAndElse);
    }

    public void ifOnly_Equal(Var var, Var isEqual, ArgumentOnly<JExecutable> ifExecuting) {
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        IfOnly ifOnly = new IfOnly(new EqualCheck(var, isEqual), ifBody);
        operations.add(ifOnly);
    }

    public void ifOnly_BooleanVar(Var booleanTypeVar, ArgumentOnly<JExecutable> ifExecuting) {
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        IfOnly ifOnly = new IfOnly(new BooleanVarCheck(booleanTypeVar.varString()), ifBody);
        operations.add(ifOnly);
    }

    public void ifOnly_Method(ArgumentOnly<JExecutable> ifExecuting, @NotNull Var var, @NotNull String methodName, Var... arguments) {
        Class<?> clazz = var.getType();
        boolean hasMethod = false;
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                    throw new RuntimeException("The return type of the method is not a boolean type!");
                }
                hasMethod = true;
                result = method;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + var.getType().getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i]);
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        IfOnly ifOnly = new IfOnly(new UsingMethodAsVar(var.varString(), result.getName(), bodyBuilder.toString()), ifBody);
        operations.add(ifOnly);
    }

    public void ifOnly_StaticMethod(ArgumentOnly<JExecutable> ifExecuting, Class<?> clazz, @NotNull String methodName, Var... arguments) {
        if (!extraClasses.contains(clazz)) {
            extraClasses.add(clazz);
        }
        boolean hasMethod = false;
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                    throw new RuntimeException("The return type of the method is not a boolean type!");
                }
                hasMethod = true;
                result = method;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i]);
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
        ifExecuting.apply(ifBody);
        IfOnly ifOnly = new IfOnly(new UsingStaticMethodAsVar(result, bodyBuilder.toString()), ifBody);
        operations.add(ifOnly);
    }

    public void returnValue(Var var) {
        this.operations.add(new ReturnValue(var.varString()));
    }

    public void returnEmpty() {
        this.operations.add(new ReturnVoid());
    }

    protected List<Operation> getOperations() {
        return new ArrayList<>(this.operations);
    }

    public void setFieldValue(JField field, Var var) {
        this.operations.add(new SetFieldValue(field.getParent().getName(), null, field.getName(), var.varString(), field.isStatic()));
    }

    public void setThisFieldValue(JField field, Var var) {
        if (field.parent != this.parent) throw new RuntimeException("The parent class of the field is not correct");
        this.operations.add(new SetFieldValue(parent.getName(), "this", field.getName(), var.varString(), field.isStatic()));
    }

    public void setOthersFieldValue(Var fieldOwner, String field, Var var) {
        this.operations.add(new SetFieldValue(null, fieldOwner.varString(), field, var.varString(), false));
    }

    /**
     * Be used to if and else executable body
     */
    private static class JExecutable4InnerStructure extends JExecutable {

        private final JExecutable executable;

        public JExecutable4InnerStructure(JExecutable parent, JClass clazz) {
            super(clazz);
            this.executable = parent;
        }

        public void usingMethod(Class<?> clazz, String methodName, Var... arguments) {
            if (!executable.extraClasses.contains(clazz)) {
                executable.extraClasses.add(clazz);
            }
            boolean hasMethod = false;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers())) {
                    hasMethod = true;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i].varString());
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            UsingStaticMethod usingMethod = new UsingStaticMethod(clazz.getName(), methodName, bodyBuilder.toString());
            operations.add(usingMethod);
        }

        public void ifElse_Equal(Var var, Var isEqual, ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting) {
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            elseExecuting.apply(elseBody);
            IfAndElse ifAndElse = new IfAndElse(new EqualCheck(var, isEqual), ifBody, elseBody);
            operations.add(ifAndElse);
        }

        public void ifElse_BooleanVar(Var booleanTypeVar, ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting) {
            if (!(booleanTypeVar.getType() == boolean.class || booleanTypeVar.getType() == Boolean.class)) {
                throw new RuntimeException("The var is not boolean type var");
            }
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            elseExecuting.apply(elseBody);
            IfAndElse ifAndElse = new IfAndElse(new BooleanVarCheck(booleanTypeVar.varString()), ifBody, elseBody);
            operations.add(ifAndElse);
        }

        public void ifElse_Method(ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting, @NotNull Var var, @NotNull String methodName, Var... arguments) {
            Class<?> clazz = var.getType();
            boolean hasMethod = false;
            Method result = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                    if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                        throw new RuntimeException("The return type of the method is not a boolean type!");
                    }
                    hasMethod = true;
                    result = method;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + var.getType().getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i]);
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            elseExecuting.apply(elseBody);
            IfAndElse ifAndElse = new IfAndElse(new UsingMethodAsVar(var.varString(), result.getName(), bodyBuilder.toString()), ifBody, elseBody);
            operations.add(ifAndElse);
        }

        public void ifElse_StaticMethod(ArgumentOnly<JExecutable> ifExecuting, ArgumentOnly<JExecutable> elseExecuting, @NotNull Class<?> clazz, @NotNull String methodName, Var... arguments) {
            if (!executable.extraClasses.contains(clazz)) {
                executable.extraClasses.add(clazz);
            }
            boolean hasMethod = false;
            Method result = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                    if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                        throw new RuntimeException("The return type of the method is not a boolean type!");
                    }
                    hasMethod = true;
                    result = method;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i]);
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            JExecutable4InnerStructure elseBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            elseExecuting.apply(elseBody);
            IfAndElse ifAndElse = new IfAndElse(new UsingStaticMethodAsVar(result, bodyBuilder.toString()), ifBody, elseBody);
            operations.add(ifAndElse);
        }

        public void ifOnly_Equal(Var var, Var isEqual, ArgumentOnly<JExecutable> ifExecuting) {
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            IfOnly ifOnly = new IfOnly(new EqualCheck(var, isEqual), ifBody);
            operations.add(ifOnly);
        }

        public void ifOnly_BooleanVar(Var booleanTypeVar, ArgumentOnly<JExecutable> ifExecuting) {
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            IfOnly ifOnly = new IfOnly(new BooleanVarCheck(booleanTypeVar.varString()), ifBody);
            operations.add(ifOnly);
        }

        public void ifOnly_Method(ArgumentOnly<JExecutable> ifExecuting, @NotNull Var var, @NotNull String methodName, Var... arguments) {
            Class<?> clazz = var.getType();
            boolean hasMethod = false;
            Method result = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                    if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                        throw new RuntimeException("The return type of the method is not a boolean type!");
                    }
                    hasMethod = true;
                    result = method;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + var.getType().getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i]);
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            IfOnly ifOnly = new IfOnly(new UsingMethodAsVar(var.varString(), result.getName(), bodyBuilder.toString()), ifBody);
            operations.add(ifOnly);
        }

        public void ifOnly_StaticMethod(ArgumentOnly<JExecutable> ifExecuting, Class<?> clazz, @NotNull String methodName, Var... arguments) {
            if (!executable.extraClasses.contains(clazz)) {
                executable.extraClasses.add(clazz);
            }
            boolean hasMethod = false;
            Method result = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                    if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                        throw new RuntimeException("The return type of the method is not a boolean type!");
                    }
                    hasMethod = true;
                    result = method;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i]);
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            JExecutable4InnerStructure ifBody = new JExecutable4InnerStructure(this, this.parent);
            ifExecuting.apply(ifBody);
            IfOnly ifOnly = new IfOnly(new UsingStaticMethodAsVar(result, bodyBuilder.toString()), ifBody);
            operations.add(ifOnly);
        }

        public Var createUsingMethodAsString(@NotNull Var var, @NotNull String methodName, Var... arguments) {
            Class<?> clazz = var.getType();
            boolean hasMethod = false;
            Method result = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                    hasMethod = true;
                    result = method;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + var.getType().getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i].varString());
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            return new UsingMethodAsVar(var.varString(), result.getName(), bodyBuilder.toString());
        }

        public Var createUsingMethodAsString(@NotNull Class<?> clazz, @NotNull String methodName, Var... arguments) {
            if (!executable.extraClasses.contains(clazz)) {
                executable.extraClasses.add(clazz);
            }
            boolean hasMethod = false;
            Method result = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers())) {
                    hasMethod = true;
                    result = method;
                    break;
                }
            }
            if (!hasMethod) {
                throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
            }
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                bodyBuilder.append(arguments[i].varString());
                if (i != arguments.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            return new UsingStaticMethodAsVar(result, bodyBuilder.toString());
        }

        @Override
        public String getString() {
            StringBuilder base = new StringBuilder();
            for (Operation operation : this.getOperations()) {
                base.append(operation.getString()).append("\n");
            }
            return base.toString();
        }
    }

    protected List<Class<?>> getRequirementTypes() {
        return new ArrayList<>(extraClasses);
    }

    @Override
    public void addAnnotation(Class<?> annotation, Map<String, JStringVar> values) {
        getAnnotations().put(annotation, values);
    }

    @Override
    public Map<Class<?>, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

}
