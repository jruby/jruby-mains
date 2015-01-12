package de.saumya.mojo.mains;

public class ExtractingWarMain {

	public static void main(String... args) throws Exception {
		new ExtractingLauncher().main("WEB-INF/classes", "WEB-INF/lib", args);
	}

}
