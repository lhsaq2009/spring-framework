package org.springframework.beans.model;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20228/22
 */
public class Women {

	private Man man;

	public Women() {
	}

	public void say() {
		System.out.println("Women -> say()");
	}

	public Women(Man man) {
		this.man = man;
	}

	public Man getMan() {
		return man;
	}

	public void setMan(Man man) {
		this.man = man;
	}
}

