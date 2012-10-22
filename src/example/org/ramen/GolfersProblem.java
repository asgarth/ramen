package example.org.ramen;

import org.ramen.Context;
import org.ramen.engine.DefaultRuleEngine;
import org.ramen.engine.RuleEngine;
import org.ramen.rule.MVELRule;
import org.ramen.rule.Rule;

public class GolfersProblem {

	public static void main(String[] args) {
		Golfer tom = new Golfer("Tom");
		Golfer joe = new Golfer("Joe");
		Golfer fred = new Golfer("Fred");
		Golfer bob = new Golfer("Bob");

		Context context = new Context();
		context.add("golfer", tom, joe, fred, bob);

		context.add("availablePos", 1, 2, 3, 4);
		context.add("availableColors", "blue", "plaid", "red", "orange");

		Rule uniquePos = new MVELRule("Unique positions").on("golfer g").when("availablePos.size() == 1").and("g.pos == 0")
				.then("g.pos = availablePos.remove(0)");

		Rule uniqueColors = new MVELRule("Unique colors").on("golfer g")
				.when("availableColors.size() == 1").and("g.color == null")
				.then("g.color = availableColors.remove(0)");

		Rule joeIsInPos2 = new MVELRule("Joe is in position 2")
				.on("golfer g")
				.when("g.name == 'Joe'")
				.then("availablePos.remove(availablePos.indexOf(2)); g.pos = 2");

		Rule personToFredRightIsBlue = new MVELRule("Person to Fred's immediate right is wearing blue pants")
				.on("golfer g1", "golfer g2")
				.when("g1.name == 'Fred'").and("g2.pos == (g1.pos + 1)")
				.then("availableColors.remove('blue'); g2.color = 'blue'");

		Rule fredNotPos4 = new MVELRule("Fred isn't in position 4")
				.on("golfer g")
				.when("g.name == 'Fred'").and("($ in availablePos if $ != 4).size() == 1")
				.then("g.pos = availablePos.remove(availablePos.indexOf(($ in availablePos if $ != 4)[0]))");

		Rule tomNotInPos1or4 = new MVELRule("Tom isn't in position 1 or 4")
				.on("golfer g")
				.when("g.name == 'Tom'").and("($ in availablePos if $ != 4 && $ != 1).size() == 1")
				.then("g.pos = availablePos.remove(availablePos.indexOf(($ in availablePos if $ != 4 && $ != 1)[0]))");

		Rule bobColorPlaid = new MVELRule("Bob is wearing plaid pants")
				.on("golfer g")
				.when("g.name == 'Bob'")
				.then("availableColors.remove('plaid'); g.color = 'plaid'");

		Rule tomColorNotOrange = new MVELRule("Tom isn't wearing orange pants")
				.on("golfer g")
				.when("g.name == 'Tom'").and("($ in availableColors if $ != 'orange').size() == 1")
				.then("g.color = availableColors.remove(availableColors.indexOf(($ in availableColors if $ != 'orange')[0]))");

		RuleEngine engine = new DefaultRuleEngine();
		engine.add(uniquePos);
		engine.add(uniqueColors);
		engine.add(joeIsInPos2);
		engine.add(personToFredRightIsBlue);
		engine.add(fredNotPos4);
		engine.add(tomNotInPos1or4);
		engine.add(bobColorPlaid);
		engine.add(tomColorNotOrange);

		engine.eval(context);

		System.out.println(tom);
		System.out.println(joe);
		System.out.println(fred);
		System.out.println(bob);

	}

	public static class Golfer {

		private String name;

		private int pos;

		private String color;

		public Golfer(String name) {
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

		@Override
		public String toString() {
			return "Golfer [name=" + name + ", pos=" + pos + ", color=" + color + "]";
		}

	}

}
