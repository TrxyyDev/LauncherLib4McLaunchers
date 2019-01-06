package fr.trxyy.interfaces.utils;

public class Wrapper {
	
	public static String LAUNCHER_NAME = "Trxyy| FXLib";
	
	public static void log(String txt) {
		System.out.println("[" + LAUNCHER_NAME + "] " + txt);
	}
	
	public static void err(String txt_) {
		System.err.println("[" + LAUNCHER_NAME + "] [!!!] " + txt_);
	}
}
