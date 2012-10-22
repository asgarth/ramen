# Ramen Rule
Ramen is a simple and elegant _rule engine_ for Java and Scala designed to be easy to use and fun to work with.

## Download

You can download the latest release and nightly build from http://github.com/asgarth/ramen/downloads.

The only dependencies is the MVEL expression language (the MVEL jar file is already included in the archive but the latest release can be download from http://mvel.codehaus.org/Downloading+MVEL).

## Getting started

An example of using some rules in java code:

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

## Licence

Ramen is distributed under [MIT License](http://en.wikipedia.org/wiki/MIT_License).
