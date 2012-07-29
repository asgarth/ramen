package org.ramen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Context {

	private Map<String, Object> map;

	public Context() {
		map = new HashMap<String, Object>();
	}

	public void add( final String key, final Object ... values ) {
		// if we already have this key just add the value to the underlying list
		if( map.containsKey( key ) ) {
			Object container = map.get( key );
			if( container instanceof List ) {
				final List<Object> list = (List<Object>) container;
				for( Object obj : values )
					list.add( obj );
			}
			return;
		}

		// if this is a new key init the object list
		final List<Object> list = new LinkedList<Object>();
		for( Object obj : values )
			list.add( obj );
		map.put( key, list );
	}
	
	public void set( final String key, final Object value ) {
		map.put( key, value );
	}
	
	public void remove( final String key ) {
		map.remove( key );
	}

	public Object get( final String key ) {
		return map.get( key );
	}
	
	public Map<String, Object> asMap() {
		return map;
	}

}
