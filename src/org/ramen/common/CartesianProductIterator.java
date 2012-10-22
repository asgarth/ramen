package org.ramen.common;

import java.util.Iterator;

/** Iterates over Cartesian product for an array of {@link Iterable}, 
 * returns an array of Objects on each step containing values from each of the {@link Iterable}.
 *
 * @author jacek.p.kolodziejczyk@gmail.com
 */
public class CartesianProductIterator implements Iterator, Iterable<Object[]> {

	private final Iterable[] iterables;
	private final Iterator[] iterators;
	private Object[] values;
	private int size;
	private boolean empty;

	/** Initialize this Cartesian product with the specified input iterable collections.
	 * 
	 * @param iterables array of {@link Iterable} being the source for the Cartesian product.
	 */
	public CartesianProductIterator(Iterable ... iterables) {
		this.size = iterables.length;
		this.iterables = iterables;
		this.iterators = new Iterator[size];

		// initialize iterators
		for (int i = 0; i < size; i++) {
			iterators[i] = iterables[i].iterator();
			// if one of the iterators is empty then the whole Cartesian product is empty
			if (!iterators[i].hasNext()) {
				empty = true;
				break;
			}
		}

		// initialize the tuple of the iteration values except the last one
		if (! empty) {
			values = new Object[size];
			for (int i = 0; i < size - 1; i++)
				setNextValue(i);
		}
	}

	@Override
	public boolean hasNext() {
		if (empty)
			return false;

		for (int i = 0; i < size; i++)
			if (iterators[i].hasNext())
				return true;

		return false;
	}

	@Override
	public Object[] next() {
		// find first in reverse order iterator that has a next element
		int cursor;
		for (cursor = size - 1; cursor >= 0; cursor--)
			if (iterators[cursor].hasNext())
				break;

		// initialize iterators next from the current one
		for (int i = cursor + 1; i < size; i++)
			iterators[i] = iterables[i].iterator();

		// get the next value from the current iterator and all the next ones
		for (int i = cursor; i < size; i++)
			setNextValue(i);

		return values.clone();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Object[]> iterator() {
		return this;
	}

	/** Gets the next value provided there is one from the iterator at the given index. */
	private void setNextValue(int index) {
		final Iterator it = iterators[index];
		if (it.hasNext())
			values[index] = it.next();
	}

}
