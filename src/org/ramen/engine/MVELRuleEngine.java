package org.ramen.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.MVEL;
import org.ramen.Context;
import org.ramen.function.Function;
import org.ramen.function.MVELFunction;
import org.ramen.rule.TriggerSet;
import org.ramen.rule.Rule;

public class MVELRuleEngine implements RuleEngine {

	private final Map<Rule, List<TriggerSet>> ruleMap;
	
	private final Map<String, Object> funcMap;

	public MVELRuleEngine() {
		ruleMap = new HashMap<Rule, List<TriggerSet>>();
		funcMap = new HashMap<String, Object>();
	}

	@Override
	public void add(final Rule rule) {
		ruleMap.put(rule, new LinkedList<TriggerSet>());
	}
	
	@Override
	public void def(final Function function) {
		if (funcMap.containsKey(function.name()))
			throw new IllegalArgumentException("A function with the same name is already defined: " + function.name());
		
		MVEL.eval(function.def(), funcMap);
	}

	/** A shortcut to define new MVEL function. This method is simply a wrapper for {@link #def(Function)}. */
	public void def(final String function) {
		def(new MVELFunction(function));
	}
	
	@Override
	public boolean eval(Context context) {
		// enrich context with function definition
		for (Entry<String, Object> func : funcMap.entrySet())
			context.setAlias(func.getKey(), func.getValue());
		
		// evaluate all rule
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
