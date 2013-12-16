package org.tomcurran.finity;

import CH.ifa.draw.application.DrawApplication;

public class Finity extends DrawApplication {

	private static final long serialVersionUID = 4056934322593307168L;

	public Finity() {
		super("Finity");
	}

	public static void main(String[] args) {
		Finity window = new Finity();
		window.open();
	}

}
