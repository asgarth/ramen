package org.ramen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Context {

	private Map<String, List<Object>> map;

	private Map<String, Object> aliasMap;
	
	/** Create a new empty context. */
	public Context() {
		map = new HashMap<String, List<Object>>();
		aliasMap = new HashMap<String, Object>();
	}

	/** Add new objects to the set with the specified label/key.
	 * 
	 * @param key the label of the set that will store the specified values
	 * @param values the objects to add to this context.
	 */
	public void add(final String key, final Object ... values) {
		// if we already have this key just add the value to the underlying set
		if (map.containsKey(key)) {
			final List<Object> set = map.get(key);
			for (Object obj : values)
				set.add(obj);

			return;
		}

		// if this is a new key init the object set
		final List<Object> set = new LinkedList<Object>();
		for (Object obj : values)
			set.add(obj);

		map.put(key, set);
	}

	/** Add new alias with the specified key/value.
	 * 
	 * @param key the alias name
	 * @param values the objects to add to this context.
	 */
	public void setAlias(final String key, final Object value) {
		if (aliasMap.containsKey(key) || map.containsKey(key))
			throw new IllegalArgumentException("Conflict between alias definition and objects in the current context: " + key);

		aliasMap.put(key, value);
	}


	/** Remove an alias with the specified label.
	 * 
	 * @param key the alias name
	 */
	public void removeAlias(final String key) {
		if (! aliasMap.containsKey(key))
			throw new IllegalArgumentException("Specified alias not found in the current context: " + key);

		aliasMap.remove(key);
	}

	/** Remove a key from the current context.
	 * 
	 * @param key the key to be removed.
	 */
	public void remove(final String key) {
		map.remove(key);
	}

	/** Get the set of objects associated with the specified label.
	 * 
	 * @param key the label to retrieve
	 * @return the object {@link Set} associated with this label.
	 */
	public List<Object> get(final String key) {
		return map.get(key);
	}

	/** Check if this context contains the specified key.
	 * 
	 * @param key the label to check
	 * @return <code>true</code> if the key is found, <code>false</code> otherwise.
	 */
	public boolean contains(final String key) {
		return map.containsKey(key);
	}

	/** Return the key/value map representing the aliases currently stored in this context. */
	public Map<String, Object> aliasMap() {
		return aliasMap;
	}

	/** Return the key/value map representing this context.
	 * 
	 * @return a {@link Map} containing all the elements stored in this context.
	 */
	public Map<String, Object> asMap() {
		final Map<String, Object> all = new HashMap<String, Object>(map.size() + aliasMap.size());
		all.putAll(map);
		all.putAll(aliasMap);
		return all;
	}

	@Override
	public String toString() {
		return "Context [map=" + map + ", alias=" + aliasMap + "]";
	}



}
