package org.ramen.function;

public class MVELFunction implements Function {

	private final String name;
	
	private final String def;
	
	public MVELFunction(final String def) {
		final String trimDef = def.trim();
		if (! trimDef.startsWith("def"))
			throw new IllegalArgumentException("Function definition should start with 'def'");
		
		final String[] nameSplit = trimDef.replaceFirst("def[ ]*", "").split("\\(");
		if (nameSplit.length < 2)
			throw new IllegalArgumentException("Function definition does not contains brackets '(', ')'");
		
		this.name = nameSplit[0];
		this.def = trimDef;
	}
	
	public String name() {
		return name;
	}
	
	public String def() {
		return def;
	}

}
