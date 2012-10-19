package org.ramen.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ramen.Context;
import org.ramen.rule.TriggerSet;
import org.ramen.rule.Rule;

public class InMemoryRuleEngine implements RuleEngine {

	public Map<Rule, List<TriggerSet>> ruleMap;

	public InMemoryRuleEngine() {
		ruleMap = new HashMap<Rule, List<TriggerSet>>();
	}

	@Override
	public void add(final Rule rule) {
		ruleMap.put(rule, new LinkedList<TriggerSet>());
	}

	@Override
	public boolean eval(Context context) {
		boolean atLeastOneRuleFire = true;

		while (atLeastOneRuleFire) {
			//System.out.println("[ENGINE] Executing a loop on all rule");
			atLeastOneRuleFire = false;

			for (Entry<Rule, List<TriggerSet>> ruleEntry : ruleMap.entrySet()) {
				final Rule rule = ruleEntry.getKey();
				final List<TriggerSet> activatorList = ruleEntry.getValue();
				
				final List<TriggerSet> outActivatorList = rule.eval(context, activatorList);
				
				boolean fired = outActivatorList.size() > activatorList.size();
				if (fired)
					atLeastOneRuleFire = true;
				
				ruleMap.put(rule, outActivatorList);
			}
		}

		return true;
	}
	
}
