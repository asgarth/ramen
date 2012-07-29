package org.ramen.rule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;
import org.ramen.Context;

public class MVELRule implements Rule {

	private final String name;

	private Map<String, String> triggers;

	private String alias;

	private List<String> conditionList;

	private String action;

	public MVELRule( final String name ) {
		this.name = name;
		this.triggers = new HashMap<String, String>();
		this.conditionList = new LinkedList<String>();
	}

	public String name() {
		return name;
	}

	public Rule on( String ... targetList ) {
		for( String trigger : targetList ) {
			if( !trigger.matches( "\\w+( \\w+$|$)" ) )
				throw new IllegalArgumentException( "Invalid trigger '" + trigger + "'. Expected target as 'trigger [alias]'" );

			final String[] args = trigger.split( " " );
			triggers.put( args.length > 1 ? args[1] : args[0], args[0] );
			/*this.trigger = args[0];

			if( args.length > 1 )
				this.alias = args[1];
			else
				this.alias = args[0];*/
		}

		return this;
	}

	public Map<String, String> trigger() {
		return triggers;
	}

	public String alias() {
		return alias;
	}

	public Rule when( final String condition ) {
		conditionList.add( condition );
		return this;
	}

	public Rule and( final String condition ) {
		conditionList.add( condition );
		return this;
	}

	public Rule then( final String action ) {
		this.action = action;
		return this;
	}

	public boolean eval( final Context context ) {
		for( String condition : conditionList )
			if( ! MVEL.evalToBoolean( condition, context.asMap() ) )
				return false;

		Object result = MVEL.eval( action, context.asMap() );
		return true;
	}

}
