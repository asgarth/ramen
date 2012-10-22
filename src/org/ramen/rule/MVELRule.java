package org.ramen.rule;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.MVEL;
import org.ramen.Context;
import org.ramen.common.CartesianProductIterator;

public class MVELRule implements Rule {

	private final String name;

	private final Map<String, String> aliasMap;

	private final List<String> conditionList;

	private String action;

	public MVELRule(final String name) {
		this.name = name;
		this.aliasMap = new LinkedHashMap<String, String>();
		this.conditionList = new LinkedList<String>();
	}

	public String name() {
		return name;
	}

	public Rule on(final String ... triggers) {
		aliasMap.clear();

		for (String s : triggers) {
			if (! s.matches("\\w+ \\w+"))
				throw new IllegalArgumentException("Invalid trigger '" + s + "', expected format: 'trigger alias'");

			final String[] split = s.split(" ");
			final String reference = split[0];
			final String alias = split[1];

			if (aliasMap.containsKey(alias))
				throw new IllegalArgumentException("Invalid trigger '" + s + "', alias already defined for this rule");

			aliasMap.put(alias, reference);
		}

		return this;
	}

	public Rule when(final String condition) {
		conditionList.clear();

		conditionList.add(condition);
		return this;
	}

	public Rule and(final String condition) {
		conditionList.add(condition);
		return this;
	}

	public Rule then(final String action) {
		this.action = action;
		return this;
	}

	public List<TriggerSet> eval(final Context context) {
		return eval(context, new LinkedList<TriggerSet>());
	}

	public List<TriggerSet> eval(final Context context, final List<TriggerSet> previousTriggerSetList) {
		/** check precondition */
		// check for conflict between trigger alias and context key
		for (String alias : aliasMap.keySet())
			if (context.contains(alias))
				throw new UnsupportedOperationException("Conflict between alias definition and objects in the current context: " + alias);

		// check for trigger defined on values not in the current context
		for (String reference : aliasMap.values())
			if (! context.contains(reference))
				return previousTriggerSetList;

		/** start rule evaluation */
		// initialize a new set that will store all the triggers used to fire this rule
		final List<TriggerSet> result = new LinkedList<TriggerSet>(previousTriggerSetList);

		// get all the collection of objects specified as trigger for this rule
		final Map<String, List<Object>> aliasObjectsMap = buildAliasObjectMap(context);

		// iterate over all possible combination of trigger objects for this rule
		final CartesianProductIterator possibleTriggerSetIterator = new CartesianProductIterator(aliasObjectsMap.values().toArray(new Iterable[aliasObjectsMap.size()]));
		for (Object[] currentTriggerArray : possibleTriggerSetIterator) {
			final TriggerSet triggerSet = buildTriggerSet(currentTriggerArray);

			// skip if rule already fired on this trigger set
			if (result.contains(triggerSet))
				continue;

			// put all alias/object tuple in the current context
			for (Trigger t : triggerSet.asSet())
				context.setAlias(t.alias(), t.value());

			// evaluate rule condition
			boolean fired = evalRuleCondition(context.asMap());

			// if all condition are valid -> fire this rule
			if (fired) {
				MVEL.eval(action, context.asMap());
				result.add(triggerSet);
			}

			// remove all alias from this context
			for (Trigger t : triggerSet.asSet())
				context.removeAlias(t.alias());
		}

		return result;
	}

	/** Build the internal map used to store alias/objects relation. */
	private Map<String, List<Object>> buildAliasObjectMap(final Context context) {
		final Map<String, List<Object>> map = new LinkedHashMap<String, List<Object>>(aliasMap.size());
		for (Entry<String, String> trigger : aliasMap.entrySet())
			map.put(trigger.getKey(), context.get(trigger.getValue()));

		return map;
	}

	/** Build a trigger set object from the corresponding alias/trigger combination. */
	private TriggerSet buildTriggerSet(final Object[] currentTriggerArray) {
		final TriggerSet triggerSet = new TriggerSet();
		int i = 0;
		for (String alias : aliasMap.keySet()) {
			triggerSet.add(new Trigger(alias, currentTriggerArray[i]));
			i++;
		}

		return triggerSet;
	}

	/** Verify that all the rule conditions are valid on the current context. */
	private boolean evalRuleCondition(final Map<String, Object> contextMap) {
		for (String condition : conditionList)
			if (! MVEL.evalToBoolean(condition, contextMap))
				return false;

		return true;
	}

}
