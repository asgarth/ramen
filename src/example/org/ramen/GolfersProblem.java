package example.org.ramen;

import org.ramen.Context;
import org.ramen.engine.InMemoryRuleEngine;
import org.ramen.engine.RuleEngine;
import org.ramen.rule.MVELRule;
import org.ramen.rule.Rule;

public class GolfersProblem {

	public static void main(String[] args) {
		Person tom = new Person("Tom");
		Person joe = new Person("Joe");
		Person fred = new Person("Fred");
		Person bob = new Person("Bob");

		Context context = new Context();
		context.add("person", tom, joe, fred, bob);

		context.add("availablePos", 1, 2, 3, 4);
		context.add("availableColors", "blue", "plaid", "red", "orange");

		Rule uniquePos = new MVELRule("Unique positions").on("person p").when("availablePos.size() == 1 && p.pos == 0")
				.then("p.pos = availablePos.removeFirst(); System.out.println(p.name + \" pos = \" + p.pos);");
		Rule uniqueColors = new MVELRule("Unique colors").on("person p")
				.when("availableColors.size() == 1 && p.color == null")
				.then("p.color = availableColors.removeFirst(); System.out.println(p.name + \" color = \" + p.color);");
		Rule joeIsInPos2 = new MVELRule("Joe is in position 2")
				.on("person p")
				.when("p.name == \"Joe\" && p.pos == 0")
				.then("p.pos = availablePos.remove(availablePos.indexOf(2)); System.out.println(\"Joe pos = \" + p.pos);");
		Rule personToFredRightIsBlue = new MVELRule("Person to Fred's immediate right is wearing blue pants")
				.on("person p1", "person p2")
				.when("p1.name == \"Fred\" && p2.pos == (p1.pos + 1) && p2.color == null")
				.then("availableColors.remove(availableColors.indexOf(\"blue\")); p2.color = \"blue\"; System.out.println(p2.name + \" color = blue\");");
		Rule fredNotPos4 = new MVELRule("Fred isn't in position 4")
				.on("person p")
				.when("p.name == \"Fred\" && ($ in availablePos if $ != 4).size() == 1")
				.then("p.pos = availablePos.remove(availablePos.indexOf(($ in availablePos if $ != 4)[0])); System.out.println(p.name + \" pos = \" + p.pos);");
		Rule tomNotInPos1or4 = new MVELRule("Tom isn't in position 1 or 4")
				.on("person p")
				.when("p.name == \"Tom\" && ($ in availablePos if $ != 4 && $ != 1).size() == 1")
				.then("p.pos = availablePos.remove(availablePos.indexOf(($ in availablePos if $ != 4 && $ != 1)[0])); System.out.println(p.name + \" pos = \" + p.pos);");
		Rule bobColorPlaid = new MVELRule("Bob is wearing plaid pants")
				.on("person p")
				.when("p.name == \"Bob\" && p.color == null")
				.then("p.color = availableColors.remove(availableColors.indexOf(\"plaid\")); System.out.println(\"Bob color = \" + p.color);");
		Rule tomColorNotOrange = new MVELRule("Tom isn't wearing orange pants")
				.on("person p")
				.when("p.name == \"Tom\" && ($ in availableColors if $ != \"orange\").size() == 1")
				.then("p.color = availableColors.remove(availableColors.indexOf(($ in availableColors if $ != \"orange\")[0])); System.out.println(p.name + \" color = \" + p.color);");

		RuleEngine engine = new InMemoryRuleEngine();
		engine.add(uniquePos);
		engine.add(uniqueColors);
		engine.add(joeIsInPos2);
		engine.add(personToFredRightIsBlue);
		engine.add(fredNotPos4);
		engine.add(tomNotInPos1or4);
		engine.add(bobColorPlaid);
		engine.add(tomColorNotOrange);

		engine.fire(context);
	}

	public static class Person {

		private String name;

		private int pos;

		private String color;

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPos() {
			return pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String toString() {
			return name;
		}

	}

}
