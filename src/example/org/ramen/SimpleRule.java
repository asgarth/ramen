package example.org.ramen;

import org.ramen.Context;
import org.ramen.engine.InMemoryRuleEngine;
import org.ramen.engine.RuleEngine;
import org.ramen.rule.MVELRule;
import org.ramen.rule.Rule;

/** A simple class to demonstrate basic rule usage. */
public class SimpleRule {

	public static void main(String[] args) {
		Person john = new Person("John", 15);
		Person anna = new Person("Anna", 35);

		Context context = new Context();
		context.add("person", john);
		context.add("person", anna);

		Rule testStudent = new MVELRule("Test Student").on("person p").when("p.age < 18").then("p.student = true");

		RuleEngine engine = new InMemoryRuleEngine();
		engine.add(testStudent);

		engine.eval(context);

		System.out.println(john.getName() + ": " + john.isStudent());
		System.out.println(anna.getName() + ": " + anna.isStudent());
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

		@Override
		public String toString() {
			return "'" + name + "'";
		}

	}

}
