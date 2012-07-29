import org.ramen.Context
import org.ramen.RuleEngine
import org.ramen.engine.InMemoryRuleEngine
import org.ramen.rule.MVELRule
import org.ramen.rule.Rule

class Person {
    var name = "Pippo"
    var age = 15
    var student = false
    
    def this(n : String, a : Int) = {
        this();
        this.name = n
        this.age = a
    }
    
    def getName() : String = {
        return this.name;  
    }
    def setName(value : String) {
        this.name = value;
    }
    
    def getAge() : Int = {
        return this.age;  
    }
    def setAge(value : Int) {
        this.age = age;
    }
    
    def isStudent() : Boolean = {
        return this.student;  
    }
    def setStudent(value : Boolean) {
        this.student = student;
    }
}

var testStudent = new MVELRule("pippo").on("person p").when("p.age < 18").then("p.student = true")

var engine = new InMemoryRuleEngine()
engine.add(testStudent)

var context = new Context()

var john = new Person("John", 15);
var anna = new Person("Anna", 35);

context.add("person", john);
context.add("person", anna);

engine.eval(context)

println(john.getName() + ": " + john.isStudent());
println(anna.getName() + ": " + anna.isStudent());
