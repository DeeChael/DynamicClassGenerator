# Dynamic Class Generator 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.deechael/dcg/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.deechael/dcg)
[![License](https://img.shields.io/github/license/Ocean-Project/DynamicClassGenerator.svg)](https://www.gnu.org/licenses/gpl-3.0.html)
[![CodeFactor](https://www.codefactor.io/repository/github/ocean-project/dynamicclassgenerator/badge)](https://www.codefactor.io/repository/github/ocean-project/dynamicclassgenerator)
[![Forks](https://img.shields.io/github/forks/Ocean-Project/DynamicClassGenerator.svg)](https://github.com/Ocean-Project/DynamicClassGenerator/fork)
[![Stars](https://img.shields.io/github/stars/Ocean-Project/DynamicClassGenerator.svg)]()
## Allow you to generate classes while the program is running!

<b>Go to wiki to learn more!</b>

### Importation:
#### For Maven
```xml
<dependency>
    <groupId>net.deechael</groupId>
    <artifactId>dcg</artifactId>
    <version>1.03.3</version>
    <scope>compile</scope>
</dependency>
```
#### For Gradle
```kotlin
dependencies { 
    //...
    implementation 'net.deechael:dcg:1.03.3'
}
```

### Example:
```java
JClass clazz = new JClass(Level.PUBLIC, "net.deechael.test", "DynamicClassGeneratorTest");
JField field = clazz.addField(Level.PUBLIC, String.class, "parent", false, false);
field.initialize(JStringVar.stringVar("aaaaaaaaaaaaaaaaaaaaaaa"));
JConstructor constructor = clazz.addConstructor(Level.PUBLIC);
Var testing = constructor.addParameter(String.class, "testing");
Var human = constructor.addParameter(Human.class, "human");
Var second = constructor.createVar(String.class, "second", Var.constructor(String.class, testing));
Var age = constructor.createVar(int.class, "age", Var.invokeMethod(human, "getAge"));
Var name = constructor.createVar(String.class, "name", Var.invokeMethod(human, "getName"));
constructor.invokeMethod(human, "print", testing);
constructor.invokeMethod(human, "print", second);
constructor.invokeMethod(human, "print", human);
constructor.invokeMethod(human, "print", age);
constructor.invokeMethod(human, "print", name);
constructor.ifElse(Requirement.isEqual(age, JStringVar.intVar(16)), (executable) -> {
executable.invokeMethod(human, "print", JStringVar.stringVar("You entered if executable body"));
}).setElse(((executable) -> {
executable.invokeMethod(human, "print", JStringVar.stringVar("You entered else executable body"));
}));

JMethod method = clazz.addMethod(Level.PUBLIC, "testing", false, false, false);
Var method_human = method.addParameter(Human.class, "human");
Var method_age = method.createVar(int.class, "age", Var.invokeMethod(human, "getAge"));
method.invokeMethod(method_human, "print", JStringVar.stringVar("Method testing"));
method.ifElse(Requirement.isEqual(method_age, JStringVar.intVar(16)), (executable) -> {
executable.invokeMethod(method_human, "print", JStringVar.stringVar("Method if body"));
}).setElse((executable) -> {
executable.invokeMethod(method_human, "print", JStringVar.stringVar("Method else body"));
});
method.invokeMethod(method_human, "print", field);
method.setFieldValue(field, JStringVar.stringVar("bbbbbbbbbbbbbbbbbbbb"));
method.invokeMethod(method_human, "print", field);

Class<?> generated = JGenerator.generate(clazz);
Constructor<?> cons = generated.getConstructor(String.class, Human.class);
Object instance = cons.newInstance("Test message!", new Human("Name", 16));
generated.getMethod("testing", Human.class).invoke(instance, new Human("DeeChael", 16));
generated.getMethod("testing", Human.class).invoke(instance, new Human("DeeChael", 39));
```

### Generated code:
```java
package net.deechael.test;

import java.lang.String;
import net.deechael.library.dcg.test.Human;

/**
 * The generated code as you can see is formatted by hand,
 * there is no space at the start of the lines in the real generated code
 */
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
```

### Coming Soon:

1.<s>Extending class</s>\
2.<s>Implementing interfaces</s>\
3.Creating annotation\
4.<s>Creating enum</s>\
5.<s>Creating interface</s>\
6.<s>Try & Catch</s>\
7.<s>Try & multi Catch</s>\
8.<s>Try & 1 Catch with multi exceptions</s>\
9.<s>If & multi Else-ifs & else</s>\
10.<s>If & multi Else-ifs</s>\
11.More requirements for If-block and Else If-block\
12.Convenient variables managing\
13.<s>Try & Catch & Finally</s>\
14.<s>Abstract class</s>\
...
