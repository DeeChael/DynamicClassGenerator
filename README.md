# Dynamic Class Generator
## Generate classes when a program is running!

<b>Go to wiki to learn more!</b>

### Example:
    JClass clazz = new JClass("net.deechael.test", "DynamicClassGeneratorTest", Level.PUBLIC);
    //Create field
    JField field = clazz.addField(Level.PUBLIC, String.class, "parent", false, false);
    field.initializeByValue(JStringVar.stringVar("aaaaaaaaaaaaaaaaaaaaaaa"));
    //Create constructor
    JConstructor constructor = clazz.addConstructor(Level.PUBLIC);
    Var testing = constructor.addParameter(String.class, "testing");
    Var human = constructor.addParameter(Human.class, "human");
    Var second = constructor.createNewInstanceVar(String.class, "second", testing);
    Var age = constructor.usingMethodAndCreateVar("age", human, "getAge");
    Var name = constructor.usingMethodAndCreateVar("name", human, "getName");
    constructor.usingMethod(human, "print", testing);
    constructor.usingMethod(human, "print", second);
    constructor.usingMethod(human, "print", human);
    constructor.usingMethod(human, "print", age);
    constructor.usingMethod(human, "print", name);
    constructor.ifElse_Equal(age, JStringVar.intVar(16), executable -> {
        executable.usingMethod(human, "print", JStringVar.stringVar("You entered if executable body"));
    }, executable -> {
        executable.usingMethod(human, "print", JStringVar.stringVar("You entered else executable body"));
    });
    //Create method
    JMethod method = clazz.addMethod(Level.PUBLIC, "testing");
    Var method_human = method.addParameter(Human.class, "human");
    Var method_age = method.usingMethodAndCreateVar("age", human, "getAge");
    method.usingMethod(method_human, "print", JStringVar.stringVar("Method testing"));
    method.ifElse_Equal(method_age, JStringVar.intVar(16), executable -> {
        executable.usingMethod(human, "print", JStringVar.stringVar("Method if body"));
    }, executable -> {
        executable.usingMethod(human, "print", JStringVar.stringVar("Method else body"));
    });
    method.usingMethod(method_human, "print", field);
    method.setFieldValue(field, JStringVar.stringVar("bbbbbbbbbbbbbbbbbbbb"));
    method.usingMethod(method_human, "print", field);
    //Generate class and use it by reflection
    Class<?> generated = clazz.generate();
    Constructor<?> cons = generated.getConstructor(String.class, Human.class);
    Object instance = cons.newInstance("Test message!", new Human());
    generated.getMethod("testing", Human.class).invoke(instance, new Human());

### Generated code:
    package net.deechael.test;

    import java.lang.String;
    import net.deechael.library.dcg.test.Human;

    public class DynamicClassGeneratorTest {

        public java.lang.String jfield_parent = ("aaaaaaaaaaaaaaaaaaaaaaa");

        public DynamicClassGeneratorTest (java.lang.String jparam_testing, net.deechael.library.dcg.test.Human jparam_human) {
            java.lang.String jvar_second = new java.lang.String(jparam_testing);
            int jvar_age = jparam_human.getAge();
            java.lang.String jvar_name = jparam_human.getName();
            jparam_human.print(jparam_testing);
            jparam_human.print(jvar_second);
            jparam_human.print(jparam_human);
            jparam_human.print(jvar_age);
            jparam_human.print(jvar_name);
            if (jvar_age == 16) {
                jparam_human.print("You entered if executable body");
            } else {
                jparam_human.print("You entered else executable body");
            }
        }

        public void testing(net.deechael.library.dcg.test.Human jparam_human) {
            int jvar_age = jparam_human.getAge();
            jparam_human.print("Method testing");
            if (jvar_age == 16) {
                jparam_human.print("Method if body");
            } else {
                jparam_human.print("Method else body");
            }
    
            jparam_human.print(this.jfield_parent);
            this.jfield_parent = ("bbbbbbbbbbbbbbbbbbbb");
            jparam_human.print(this.jfield_parent);
        }
    
    }

### Coming Soon:

1.<s>Extending class</s>\
2.<s>Implementing interfaces</s>\
3.Creating annotation\
4.Creating enum\
5.Creating interface\
6.<s>Try & Catch</s>\
7.<s>Try & multi Catch</s>\
8.<s>Try & 1 Catch with multi exceptions</s>\
9.<s>If & multi Else-ifs & else</s>\
10.<s>If & multi Else-ifs</s>\
11.More requirements for If-block and Else If-block\
12.Convenient variables managing\
13.<s>Try & Catch & Finally</s>\
...
