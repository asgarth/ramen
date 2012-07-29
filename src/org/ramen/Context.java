package org.ramen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Context {

	private Map<String, Object> map;

	/** Create a new empty context. */
	public Context() {
		map = new HashMap<String, Object>();
	}

	/** Add new objects to the list with the specified label/key.
	 * 
	 * @param key the label of the list that will store the specified values
	 * @param values the objects to add to this context.
	 */
	public void add(final String key, final Object ... values) {
		// if we already have this key just add the value to the underlying list
		if (map.containsKey(key)) {
			Object container = map.get(key);
			if (container instanceof List) {
				final List<Object> list = (List<Object>) container;
				for (Object obj : values)
					list.add(obj);
			}
			return;
		}

		// if this is a new key init the object list
		final List<Object> list = new LinkedList<Object>();
		for (Object obj : values)
			list.add(obj);
		map.put(key, list);
	}

	/** Set the specified label in the context to contain the input value. Any previous objects stored with that 
	 * key is overwritten.
	 * 
	 * @param key the label of the list that will store the specified value
	 * @param value the input value to store.
	 */
	public void set(final String key, final Object value) {
		map.put(key, value);
	}

	/** Remove a key from the current context.
	 * 
	 * @param key the key to be removed.
	 */
	public void remove(final String key) {
		map.remove(key);
	}

	/** Get the object associated with the specified label. The result value can be a list if multiple objects 
	 * where added to this context with the same label.
	 * 
	 * @param key the label to retrieve
	 * @return the object associated with this label.
	 */
	public Object get(final String key) {
		return map.get(key);
	}

	/** Return the key/value map representing this context.
	 * 
	 * @return a {@link Map} containing all the elements stored inthis context.
	 */
	public Map<String, Object> asMap() {
		return map;
	}

}
