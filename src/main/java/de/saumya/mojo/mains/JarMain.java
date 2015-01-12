package de.saumya.mojo.mains;

public class JarMain {

	public static void main(String... args) {
		JRubyMain.main("uri:classloader:/", args);
	}

}