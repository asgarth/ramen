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

	private final Map<String, String> triggers;

	private final List<String> conditionList;

	private String action;

	public MVELRule(final String name) {
		this.name = name;
		this.triggers = new LinkedHashMap<String, String>();
		this.conditionList = new LinkedList<String>();
	}

	public String name() {
		return name;
	}

	public Rule on(final String ... triggerList) {
		triggers.clear();

		for (String trigger : triggerList) {
			if (!trigger.matches("\\w+ \\w+"))
				throw new IllegalArgumentException("Invalid trigger '" + trigger + "', expected format: 'trigger alias'");

			final String[] split = trigger.split(" ");
			final String alias = split[1];
			final String contextReference = split[0];

			if (triggers.containsKey(alias))
				throw new IllegalArgumentException("Invalid trigger '" + trigger + "', alias already defined for this rule");

			triggers.put(alias, contextReference);
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

	public List<ActivatorSet> eval(final Context context) {
		return eval(context, new LinkedList<ActivatorSet>());
	}

	public List<ActivatorSet> eval(final Context context, final List<ActivatorSet> activators) {
		// check for conflict between trigger alias and context key
		for (String alias : triggers.keySet())
			if (context.contains(alias))
				throw new UnsupportedOperationException("Conflict between an alias definition and objects in the current context: " + alias);

		// check for trigger defined on values not in the current context
		for (String contextReference : triggers.values())
			if (!context.contains(contextReference))
				return activators;

		// start rule evaluation
		//System.out.println("Evaluating rule " + name() + " on " + context);

		// initialize a new set that will contains all the activator set that have been used to fire this rule
		final List<ActivatorSet> result = new LinkedList<ActivatorSet>(activators);

		// get all the collection of objects specified as trigger for this rule
		final Map<String, List<Object>> aliasObjectsMap = new LinkedHashMap<String, List<Object>>(triggers.size());
		for (Entry<String, String> trigger : triggers.entrySet())
			aliasObjectsMap.put(trigger.getKey(), context.get(trigger.getValue()));

		// iterate over all possible combination of rule trigger objects
		final CartesianProductIterator possibleActivatorIterator = new CartesianProductIterator(aliasObjectsMap.values().toArray(new Iterable[aliasObjectsMap.size()]));
		for (Object[] currentActivatorSet : possibleActivatorIterator) {
			final ActivatorSet activatorSet = new ActivatorSet();
			int i = 0;
			for (String aliasName : triggers.keySet()) {
				activatorSet.add(new Activator(aliasName, currentActivatorSet[i]));
				i++;
			}
			
			//System.out.println("Evaluating set " + activatorSet);
			
			if (result.contains(activatorSet))
				continue;

			for (Activator a : activatorSet.asSet())
				context.setAlias(a.getAlias(), a.getValue());

			// check for rule condition
			boolean fired = true;
			for (String condition : conditionList) {
				if (! MVEL.evalToBoolean(condition, context.asMap())) {
					fired = false;
					break;
				}
			}

			if (fired) {
				//System.out.println("'" + name + "' fired on " + activatorSet);
				result.add(activatorSet);
				MVEL.eval(action, context.asMap());
			}

			for (Activator act : activatorSet.asSet())
				context.removeAlias(act.getAlias());
		}

		return result;
	}

	/*private List<ActivatorSet> eval(final Context context, final List<ActivatorSet> activators,
	 * final LinkedList<Entry<String, String>> triggerList) {
	 * System.out.println("Evaluating rule " + name() + " on " + context);
	 * 
	 * // evaluate rule if all trigger have been evaluated
	 * if (triggerList.size() == 0) {
	 * // evaluate the activator set for this rule
	 * final ActivatorSet activatorSet = new ActivatorSet();
	 * for (Entry<String, Object> entry : context.aliasMap().entrySet())
	 * activatorSet.add(new Activator(entry.getKey(), entry.getValue()));
	 * 
	 * System.out.println("Current activator set " + activatorSet);
	 * 
	 * // check if this rule was already fired on this set of object
	 * if (activators.contains(activatorSet)) {
	 * return false;
	 * }
	 * 
	 * // check for rule condition
	 * for (String condition : conditionList) {
	 * if (!MVEL.evalToBoolean(condition, context.asMap()))
	 * return false;
	 * }
	 * 
	 * activators.add(activatorSet);
	 * 
	 * Object result = MVEL.eval(action, context.asMap());
	 * return true;
	 * }
	 * 
	 * // iterate on all object in this trigger
	 * boolean fired = false;
	 * 
	 * final Entry<String, String> trigger = triggerList.removeFirst();
	 * final List<Object> args = context.get(trigger.getValue());
	 * 
	 * for (Object obj : args) {
	 * context.setAlias(trigger.getKey(), obj);
	 * 
	 * boolean res = eval(context, new LinkedList<Entry<String, String>>(triggerList));
	 * if (res)
	 * fired = true;
	 * 
	 * context.removeAlias(trigger.getKey());
	 * }
	 * 
	 * return fired;
	 * }
	 * */

}
