package org.ramen.rule;

import java.util.List;

import org.ramen.Context;

/** Rule interface, containing all methods to configure and evaluate the desired logic. */
public interface Rule {

	/** Returns the rule name.
	 * 
	 * @return the rule name.
	 */
	public String name();

	/** Set the trigger in the evaluation context on which this rule should be evaluated.
	 * <p>The input string should match a key defined in the context structure.
	 * Optionally an alias can be defined that can be used to easily refer the target 
	 * in the rule condition and action evaluation.
	 * 
	 * @param trigger the key of the object in the context on which this rule should be evaluated
	 * @return the rule itself to chain configuration calls.
	 */
	public Rule on(String ... trigger);

	/** Initialize with the specified <condition> the set of condition on which the rule will be 
	 * evaluated in order to be fired.
	 * 
	 * @param condition the condition expression to evaluate at runtime.
	 * @return the rule itself to chain configuration calls.
	 */
	public Rule when(String condition);

	/** Add the specified <condition> to the set of condition on which the rule will be 
	 * evaluated in order to be fired.
	 * 
	 * @param condition the condition expression to evaluate at runtime.
	 * @return the rule itself to chain configuration calls.
	 */
	public Rule and(String condition);

	/** Set the action the is performed if all the conditions defined for this rule are matched in the
	 * evaluation context.
	 * 
	 * @param action the action expression to perform at runtime.
	 * @return the rule itself to chain configuration calls.
	 */
	public Rule then(String action);

	/** Evaluate the rule on the specified context.
	 * 
	 * @param context the rule context.
	 * @return a {@link List} containing the {@link ActivatorSet} on which this rule has fired.
	 */
	public List<ActivatorSet> eval(Context context);
	
	/** Execute this rule on the specified context, excluding activators in the input {@link ActivatorSet} list.
	 * 
	 * @param context the rule context
	 * @param activators the activator list
	 * @return a {@link List} containing the {@link ActivatorSet} on which this rule has fired.
	 */
	public List<ActivatorSet> eval(Context context, List<ActivatorSet> activators);
	
}
