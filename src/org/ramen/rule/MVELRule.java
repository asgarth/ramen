package org.ramen.rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mvel2.MVEL;
import org.ramen.Context;

public class MVELRule implements Rule {

	private final String name;

	private Map<String, String> triggers;

	private List<String> conditionList;

	private String action;
	
	private List<Set<Activator>> activatorList;

	public MVELRule(final String name) {
		this.name = name;
		this.triggers = new HashMap<String, String>();
		this.conditionList = new LinkedList<String>();
		
		this.activatorList = new LinkedList<Set<Activator>>();
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
				throw new IllegalArgumentException("Invalid trigger '" + trigger + "', alias already used for another ojbect");

			triggers.put(alias, contextReference);
		}

		return this;
	}

	public Map<String, String> trigger() {
		return triggers;
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
	
	public void reset() {
		activatorList.clear();
	}

	public boolean eval(final Context context) {
		// check for conflict between trigger alias and context key
		for (String alias : triggers.keySet())
			if (context.contains(alias))
				throw new UnsupportedOperationException("Conflict between an alias definition and the supplied context: " + alias);
		
		return eval(context, new LinkedList<Entry<String, String>>(triggers.entrySet()));
	}

	public boolean eval(final Context context, final LinkedList<Entry<String, String>> triggerList) {
		System.out.println("Evaluating rule " + name() + " on " + context);
		
		// evaluate rule if all trigger have been evaluated
		if (triggerList.size() == 0) {
			// evaluate the activator set for this rule
			final Set<Activator> activatorSet = new HashSet<Activator>();
			for (Entry<String, Object> entry: context.aliasMap().entrySet())
				activatorSet.add(new Activator(entry.getKey(), entry.getValue()));
			System.out.println("Current activator ser " + activatorSet);
			
			// check if this rule was already fired on this set of object
			if (activatorList.contains(activatorSet)) {
				return false;
			}
			
			// check for rule condition	
			for (String condition : conditionList) {
				if (!MVEL.evalToBoolean(condition, context.asMap()))
					return false;
			}
			
			activatorList.add(activatorSet);
				
			Object result = MVEL.eval(action, context.asMap());
			return true;
		}
		
		// iterate on all object in this trigger
		boolean fired = false;
		
		final Entry<String, String> trigger = triggerList.removeFirst();
		final List<Object> args = context.get(trigger.getValue());
		
		for (Object obj : args) {
			context.setAlias(trigger.getKey(), obj);
			
			boolean res = eval(context, new LinkedList<Entry<String, String>>(triggerList));
			if (res)
				fired = true;

			context.removeAlias(trigger.getKey());
		}
		
		return fired;
	}

}
