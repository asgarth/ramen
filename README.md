# Ramen
Ramen is a simple, powerfull _rule engine_ for Java and Scala. It is designed to be easy to use and to integrate into new or existing application.

## Download

You can download the latest release from https://github.com/asgarth/ramen/releases.

Ramen rules use the MVEL (https://github.com/mvel/mvel) expression language to define both rule _conditions_ and _actions_.

## Getting started

The following snippet give a fast overview of the basic usage to define some rules in java code:

``` java
Person john = new Person("John", 15);
Person anna = new Person("Anna", 35);

Context context = new Context();
context.add("person", john);
context.add("person", anna);

Rule testStudent = new MVELRule("Test Student").on("person p").when("p.age < 18").then("p.student = true");

RuleEngine engine = new MVELRuleEngine();
engine.add(testStudent);

engine.eval(context);

System.out.println(john);
System.out.println(anna);
```

### Define rule on multiple values

``` java
Context context = new Context();
context.add("person", new Person("John", 15));
context.add("person", new Person("Anna", 35));

Rule testStudent = new MVELRule("Always true")
    .on("person p1", "person p2")
    .when("true")
    .then("System.out.println('FIRED: ' + p1.name + ' - ' + p2.name)");

RuleEngine engine = new MVELRuleEngine();
engine.add(testStudent);

engine.eval(context);
```

### Define some functions

``` java
Person john = new Person("John", 15);
Person anna = new Person("Anna", 35);

Context context = new Context();
context.add("person", john);
context.add("person", anna);

RuleEngine engine = new MVELRuleEngine();

engine.def(new MVELFunction("def setStudent(x) { x.student = true }"));
engine.add(new MVELRule("Test Student").on("person p").when("p.age < 18").then("setStudent(p)"));

engine.eval(context);

System.out.println(john);
System.out.println(anna);
```

## Licence

Ramen is distributed under [MIT License](http://en.wikipedia.org/wiki/MIT_License).
