package example.org.ramen;

import org.ramen.Context;
import org.ramen.engine.MVELRuleEngine;
import org.ramen.engine.RuleEngine;
import org.ramen.function.MVELFunction;
import org.ramen.rule.MVELRule;

import example.org.ramen.SimpleRule.Person;

/** A simple class to demonstrate basic rule usage. */
public class SimpleFunction {

	public static void main(String[] args) {
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
	}

}
