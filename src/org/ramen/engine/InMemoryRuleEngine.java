package org.ramen.engine;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.ramen.Context;
import org.ramen.RuleEngine;
import org.ramen.rule.Rule;

public class InMemoryRuleEngine implements RuleEngine {

	public List<Rule> ruleList;

	public InMemoryRuleEngine() {
		ruleList = new LinkedList<Rule>();
	}

	public void add( Rule rule ) {
		ruleList.add( rule );
	}

	public boolean evalOld( Context context ) {
		for( Rule rule : ruleList ) {
			Object container = null; //context.get( rule.trigger() );
			Collection<Object> args;
			if( container instanceof Collection ) {
				args = (Collection<Object>) container;
			} else {
				args = new LinkedList<Object>();
				args.add( container );
			}

			// try to apply this rule to each element in the trigger list in this context
			for( Object arg : args ) {
				if( rule.alias() != null && ! rule.alias().equals( "" ) )
					context.set( rule.alias(), arg );

				rule.eval( context );

				if( rule.alias() != null && ! rule.alias().equals( "" ) )
					context.remove( rule.alias() );
			}
		}

		return true;
	}

	@Override
	public boolean eval( Context context ) {
		boolean atLeastOneRuleFire = true;

		while( atLeastOneRuleFire ) {
			System.out.println( "[ENGINE] Executing a loop on all rule" );
			
			atLeastOneRuleFire = false;
			
			for( Rule rule : ruleList ) {
				LinkedList<Entry<String, String>> triggers = new LinkedList<Entry<String,String>>();
				triggers.addAll( rule.trigger().entrySet() );

				boolean fired = iterateTrigger( rule, triggers, context );
				
				if( fired )
					atLeastOneRuleFire = true;
			}
		}

		return true;
	}

	private boolean iterateTrigger( Rule rule, LinkedList<Entry<String, String>> triggers, Context context ) {
		if( triggers.size() == 0 ) {
			//System.out.println("Eval rule on " + context.asMap() );
			return rule.eval( context );
		}

		boolean fired = false;
		final Entry<String, String> current = triggers.removeFirst();
		final String alias = current.getKey();
		final String trigger = current.getValue();

		Object container = context.get( trigger );
		Collection<Object> args;
		if( container instanceof Collection ) {
			args = (Collection<Object>) container;
		} else {
			args = new LinkedList<Object>();
			args.add( container );
		}

		// try to apply this rule to each element in the trigger list in this context
		for( Object arg : args ) {
			//System.out.println("adding alias " + alias + " (value: " + arg + ")" );
			context.set( alias, arg );

			boolean res = iterateTrigger( rule, new LinkedList<Entry<String,String>>( triggers ), context );
			if( res )
				fired = true;
			
			//System.out.println("removing alias " + alias );
			context.remove( alias );
		}

		return fired;
	}

}
