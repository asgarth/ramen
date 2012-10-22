package example.org.ramen;

import org.ramen.Context;
import org.ramen.engine.DefaultRuleEngine;
import org.ramen.engine.RuleEngine;
import org.ramen.rule.MVELRule;
import org.ramen.rule.Rule;

/** A simple class to demonstrate basic rule usage. */
public class MultipleTriggerRule {

	public static void main(String[] args) {
		Person john = new Person("John", 15);
		Person anna = new Person("Anna", 35);

		Context context = new Context();
		context.add("person", john);
		context.add("person", anna);

		Rule testStudent = new MVELRule("Always true")
				.on("person p1", "person p2")
				.when("true")
				.then("System.out.println('FIRED: ' + p1.name + ' - ' + p2.name)");

		RuleEngine engine = new DefaultRuleEngine();
		engine.add(testStudent);

		engine.eval(context);
	}

	public static class Person {

		private String name;

		private int age;

		private boolean student;

		public Person(String name, int age) {
			this.name = name;
			this.age = age;
			this.student = false;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public boolean isStudent() {
			return student;
		}

		public void setStudent(boolean student) {
			this.student = student;
		}

		public String toString() {
			return name;
		}

	}

}
