package org.ramen.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ramen.Context;
import org.ramen.rule.ActivatorSet;
import org.ramen.rule.Rule;

public class InMemoryRuleEngine implements RuleEngine {

	public Map<Rule, List<ActivatorSet>> ruleMap;

	public InMemoryRuleEngine() {
		ruleMap = new HashMap<Rule, List<ActivatorSet>>();
	}

	public void add(final Rule rule) {
		ruleMap.put(rule, new LinkedList<ActivatorSet>());
	}

	@Override
	public boolean eval(Context context) {
		boolean atLeastOneRuleFire = true;

		while (atLeastOneRuleFire) {
			//System.out.println("[ENGINE] Executing a loop on all rule");
			atLeastOneRuleFire = false;

			for (Entry<Rule, List<ActivatorSet>> ruleEntry : ruleMap.entrySet()) {
				final Rule rule = ruleEntry.getKey();
				final List<ActivatorSet> activatorList = ruleEntry.getValue();
				
				final List<ActivatorSet> outActivatorList = rule.eval(context, activatorList);
				
				boolean fired = outActivatorList.size() > activatorList.size();
				if (fired)
					atLeastOneRuleFire = true;
				
				ruleMap.put(rule, outActivatorList);
			}
		}

		return true;
	}
	
}
