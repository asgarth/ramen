package org.ramen.engine;

import java.util.LinkedList;
import java.util.List;

import org.ramen.Context;
import org.ramen.rule.Rule;

public class InMemoryRuleEngine implements RuleEngine {

	public List<Rule> ruleList;

	public InMemoryRuleEngine() {
		ruleList = new LinkedList<Rule>();
	}

	public void add(final Rule rule) {
		ruleList.add(rule);
	}

	@Override
	public boolean fire(Context context) {
		boolean atLeastOneRuleFire = true;

		while (atLeastOneRuleFire) {
			System.out.println("[ENGINE] Executing a loop on all rule");

			atLeastOneRuleFire = false;

			for (Rule rule : ruleList) {
				boolean fired = rule.eval(context);
				
				if (fired)
					atLeastOneRuleFire = true;
			}
		}

		return true;
	}
	
}
