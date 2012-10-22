package org.ramen.engine;

import org.ramen.Context;
import org.ramen.function.Function;
import org.ramen.rule.Rule;

public interface RuleEngine {

	/** Add a {@link Rule} to this engine.
	 *  
	 * @param rule a new rule to add to this engine rule collection.
	 */
	public void add(Rule rule);
	
	/** Define a new function that can be used in the {@link Rule} used in this engine.
	 *  
	 * @param func a new function to add to this engine function collection.
	 */
	public void def(Function function);


	/** Evaluate the specified {@link Context} on the rules stored in the current engine.
	 * 
	 * @param context the context containing the object to evaluate
	 * @return <code>true</code> if the evaluation is completed without errors, <code>false</code> otherwise.
	 */
	public boolean eval(Context context);

}
