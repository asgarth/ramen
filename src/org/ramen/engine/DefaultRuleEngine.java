package org.ramen.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ramen.Context;
import org.ramen.rule.TriggerSet;
import org.ramen.rule.Rule;

public class DefaultRuleEngine implements RuleEngine {

	private final Map<Rule, List<TriggerSet>> ruleMap;

	public DefaultRuleEngine() {
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
			atLeastOneRuleFire = false;

			for (Entry<Rule, List<TriggerSet>> ruleEntry : ruleMap.entrySet()) {
				final Rule rule = ruleEntry.getKey();

				final List<TriggerSet> prevActivatorList = ruleEntry.getValue();
				final List<TriggerSet> newActivatorList = rule.eval(context, prevActivatorList);

				boolean fired = newActivatorList.size() > prevActivatorList.size();
				if (fired)
					atLeastOneRuleFire = true;

				ruleMap.put(rule, newActivatorList);
			}
		}

		return true;
	}

}
