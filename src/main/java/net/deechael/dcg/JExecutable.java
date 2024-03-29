package net.deechael.dcg;

import net.deechael.dcg.creator.IfElseCreator;
import net.deechael.dcg.creator.SwitchCaseCreator;
import net.deechael.dcg.creator.TryCatchCreator;
import net.deechael.dcg.creator.TryCatchInnerCreator;
import net.deechael.dcg.operation.*;
import net.deechael.useless.function.parameters.DuParameter;
import net.deechael.useless.function.parameters.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Executable body which is without parameters
 *
 * @author DeeChael
 * @since 1.00.0
 */
public abstract class JExecutable implements JObject {

    protected final List<Operation> operations = new ArrayList<>();
    private final List<String> extraClasses = new ArrayList<>();
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();

    private String deal(String typeName) {
        if (typeName.startsWith("[L")) {
            return typeName.substring(2) + "[]";
        } else if (typeName.startsWith("[")) {
            return typeName.substring(1) + "[]";
        }
        return typeName;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public Var createVar(@NotNull JType type, @NotNull String name, @NotNull Var var) {
        name = "jvar_" + name;
        operations.add(new CreateVar(null, false, type, name, var.varString()));
        return Var.referringVar(type, name);
    }

    public Var createVar(boolean isFinal, @NotNull JType type, @NotNull String name, @NotNull Var var) {
        name = "jvar_" + name;
        operations.add(new CreateVar(null, isFinal, type, name, var.varString()));
        return Var.referringVar(type, name);
    }

    public Var createVar(Map<JType, Map<String, JStringVar>> annotations, boolean isFinal, @NotNull JType type, @NotNull String name, @NotNull Var var) {
        name = "jvar_" + name;
        operations.add(new CreateVar(annotations, isFinal, type, name, var.varString()));
        return Var.referringVar(type, name);
    }

    public void resetVar(@NotNull Var var, @Nullable Var value) {
        if (value == null) value = Var.nullVar();
        this.operations.add(new ResetVar(var.getName(), value.varString()));
    }

    public JLabel createLabel(String name) {
        name = "jlabel_" + name;
        this.operations.add(new Label(name));
        return new JLabel(name);
    }

    /**
     * Invoke a method in the executable body
     * <p>
     * Tips: If you are using the method which you added by JMethod,
     * please add "jmethod_" before your methodName because
     * generated code will add the prefix automatically
     * <p>
     * Generated code looks like: jvar_varName.methodName(arguments);
     *
     * @param var        The var that you want to use the method,
     *                   the method must be contained in the class of the var,
     *                   or else will throw an exception
     * @param methodName The method that you want to use,
     *                   the method must be contained in the class of the var,
     *                   or else will throw an exception
     * @param arguments  The arguments that the method needs,
     *                   is easier than reflection
     */
    public void invokeMethod(@NotNull Var var, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        InvokeMethod invokeMethod = new InvokeMethod(var.varString(), methodName, bodyBuilder.toString());
        operations.add(invokeMethod);
    }

    /**
     * Invoke a static method
     * <p>
     * Generated code looks like: Type.methodName(arguments);
     *
     * @param clazz      The type has the static method
     * @param methodName The method you want to use
     * @param arguments  The arguments that the method needs
     */
    public void invokeMethod(JType clazz, String methodName, Var... arguments) {
        if (!extraClasses.contains(clazz.typeString())) {
            extraClasses.add(clazz.typeString());
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        operations.add(new InvokeMethod(clazz.typeString(), methodName, bodyBuilder.toString()));
    }

    public void invokeMethodDirectly(String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        operations.add(new InvokeMethodDirectly(methodName, bodyBuilder.toString()));
    }

    /**
     * Invoke the method which implemented by parent class in child class
     * <p>
     * Generated code looks like: super.methodName(arguments);
     *
     * @param methodName The method name implemented by parent class
     * @param arguments  The arguments that the method needs
     */
    public void invokeSuperMethod(String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        InvokeMethod invokeMethod = new InvokeMethod("super", methodName, bodyBuilder.toString());
        operations.add(invokeMethod);
    }

    /**
     * Invoke the method, will execute the method is implemented by this class, if this class didn't implement this method but parent class did, execute the method in parent class
     * <p>
     * Generated code looks like: this.methodName(arguments);
     *
     * @param methodName The method name
     * @param arguments  The arguments that the method needs
     */
    public void invokeThisMethod(String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        InvokeMethod invokeMethod = new InvokeMethod("this", methodName, bodyBuilder.toString());
        operations.add(invokeMethod);
    }

    public void addExecutableStructure(Parameter<JExecutable> parameter) {
        JExecutable4InnerStructure executableStructure = new JExecutable4InnerStructure();
        parameter.apply(executableStructure);
        this.operations.add(new ExecutableStructure(executableStructure));
    }

    public void addSynchronizedStructure(Var object, Parameter<JExecutable> parameter) {
        JExecutable4InnerStructure executableStructure = new JExecutable4InnerStructure();
        parameter.apply(executableStructure);
        this.operations.add(new SynchronizedStructure(object, executableStructure));
    }

    public IfElseCreator ifElse(Requirement requirement, Parameter<JExecutable> ifExecuting) {
        return new IfElseCreator(this, requirement, ifExecuting);
    }

    public SwitchCaseCreator switchCase(Var toBeCased) {
        return new SwitchCaseCreator(this, toBeCased);
    }

    public TryCatchCreator tryCatch(Parameter<JExecutable> tryExecuting) {
        return new TryCatchCreator(this, tryExecuting);
    }

    public TryCatchInnerCreator tryCatch(JType clazz, String varName, Var var, DuParameter<JExecutable, Var> tryExecuting) {
        return new TryCatchInnerCreator(this, tryExecuting, clazz, varName, var);
    }

    public void whileLoop(Requirement requirement, Parameter<JExecutable4Loop> parameter) {
        JExecutable4Loop whileBody = new JExecutable4Loop();
        parameter.apply(whileBody);
        operations.add(new WhileLoop(requirement, whileBody));
    }

    public void doWhileLoop(Parameter<JExecutable4Loop> parameter, Requirement requirement) {
        JExecutable4Loop doBody = new JExecutable4Loop();
        parameter.apply(doBody);
        operations.add(new DoWhileLoop(doBody, requirement));
    }

    public void forLoop(@Nullable JType clazz, @Nullable String tempVarName, @Nullable Var initializedValue, @Nullable Function<Var, Requirement> judgement, @Nullable Var operation, @NotNull DuParameter<@Nullable Var, @NotNull JExecutable4Loop> forExecuting) {
        tempVarName = tempVarName != null ? "jforloop_" + tempVarName : null;
        Var referringVar = (clazz != null && tempVarName != null) ? Var.referringVar(clazz, tempVarName) : null;
        Requirement requirement = judgement != null ? judgement.apply(referringVar) : null;
        JExecutable4Loop forBody = new JExecutable4Loop();
        forExecuting.apply(referringVar, forBody);
        this.operations.add(new ForLoop(clazz, tempVarName, initializedValue, requirement, operation, forBody));
    }

    public void forEachLoop(@NotNull JType clazz, @NotNull String tempVarName, @NotNull Var iterable, @NotNull DuParameter<@NotNull Var, @NotNull JExecutable4Loop> forExecuting) {
        tempVarName = "jforeachloop_" + tempVarName;
        Var referringVar = Var.referringVar(clazz, tempVarName);
        JExecutable4Loop forBody = new JExecutable4Loop();
        forExecuting.apply(referringVar, forBody);
        this.operations.add(new ForEachLoop(clazz, tempVarName, iterable, forBody));
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

    @Override
    public void addAnnotation(JType annotation, Map<String, JStringVar> values) {
        getAnnotations().put(annotation, values);
    }

    @Override
    public Map<JType, Map<String, JStringVar>> getAnnotations() {
        return annotations;
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

        public void continueToLabel(JLabel label) {
            this.operations.add(new BreakToLabel(label.getName()));
        }

    }

    /**
     * Be used for switch-case inner structure
     */
    public static final class JExecutable4Switch extends JExecutable4InnerStructure {

        public void breakLoop(String a) {
            this.operations.add(new Break());
        }

        public void breakToLabel(JLabel label) {
            this.operations.add(new BreakToLabel(label.getName()));
        }

    }

}
