package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.body.*;
import net.deechael.library.dcg.dynamic.creator.IfElseCreator;
import net.deechael.library.dcg.dynamic.creator.TryCatchCreator;
import net.deechael.library.dcg.dynamic.items.*;
import net.deechael.useless.function.parameters.DuParameter;
import net.deechael.useless.function.parameters.Parameter;
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

    public void addOperation(Operation operation) {
        this.operations.add(operation);
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

    public IfElseCreator ifElse(Requirement requirement, Parameter<JExecutable> ifExecuting) {
        return new IfElseCreator(this, requirement, ifExecuting);
    }

    public TryCatchCreator tryCatch(Parameter<JExecutable> tryExecuting) {
        return new TryCatchCreator(this, tryExecuting);
    }

    public void whileLoop(Requirement requirement, Parameter<JExecutable4Loop> parameter) {
        JExecutable4Loop whileBody = new JExecutable4Loop();
        parameter.apply(whileBody);
        operations.add(new WhileLoop(requirement, whileBody));
    }

    public void returnValue(Var var) {
        this.operations.add(new ReturnValue(var.varString()));
    }

    public void returnVoid() {
        this.operations.add(new ReturnVoid());
    }

    protected List<Operation> getOperations() {
        return new ArrayList<>(this.operations);
    }

    public void setFieldValue(JField field, Var var) {
        this.operations.add(new SetFieldValue(field.getParent().getName(), null, field.getName(), var.varString(), field.isStatic()));
    }

    public void setThisFieldValue(JField field, Var var) {
        this.operations.add(new SetFieldValue("this", "this", field.getName(), var.varString(), field.isStatic()));
    }

    public void setOthersFieldValue(Var fieldOwner, String field, Var var) {
        this.operations.add(new SetFieldValue(null, fieldOwner.varString(), field, var.varString(), false));
    }

    /**
     * Be used for inner structure like if-else, try-catch
     */
    public static class JExecutable4InnerStructure extends JExecutable {

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

    /**
     * Be used for loop inner structure: for and while
     */
    public static final class JExecutable4Loop extends JExecutable4InnerStructure {

        public void breakLoop() {
            this.operations.add(new Break());
        }

        public void breakToLabel(JLabel label) {
            this.operations.add(new BreakToLabel(label.getName()));
        }

        public void continueLoop() {
            this.operations.add(new Continue());
        }

    }

}
