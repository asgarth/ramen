package org.ramen.rule;

import java.util.LinkedHashSet;
import java.util.Set;

public class ActivatorSet {

	private final Set<Activator> set;
	
	public ActivatorSet() {
		this.set = new LinkedHashSet<Activator>();
	}
	
	public ActivatorSet(final Set<Activator> set) {
		this.set = set;
	}

	public void add(final Activator activator) {
		set.add(activator);
	}
	
	public void remove(final Activator activator) {
		set.remove(activator);
	}
	
	public Set<Activator> asSet() {
		return set;
	}

	@Override
	public int hashCode() {
		return set.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivatorSet other = (ActivatorSet) obj;
		if (set == null) {
			if (other.set != null)
				return false;
		} else if (!set.equals(other.set))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivatorSet: " + set;
	}
	
}
