package org.ramen;

import org.ramen.rule.Rule;

public interface RuleEngine {

	public void add( Rule rule );

	public boolean eval( Context context );

}
