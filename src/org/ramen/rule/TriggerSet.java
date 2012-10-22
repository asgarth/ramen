package org.ramen.rule;

import java.util.LinkedHashSet;
import java.util.Set;

/** Represent the set of trigger on which a rule is evaluated.  */
public class TriggerSet {

	private final Set<Trigger> set;

	public TriggerSet() {
		this.set = new LinkedHashSet<Trigger>();
	}

	public void add(final Trigger activator) {
		set.add(activator);
	}

	public void remove(final Trigger activator) {
		set.remove(activator);
	}

	public Set<Trigger> asSet() {
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
		TriggerSet other = (TriggerSet) obj;
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
