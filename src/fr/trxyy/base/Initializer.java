package fr.trxyy.base;

import fr.trxyy.gamelaunch.Tweaks;
import fr.trxyy.gamelaunch.Versions;

public class Initializer {
	
	public Initializer() {}
	
	public static void registerBasics(String title, int width, int height) {
		Configuration.createBasicConfiguration(title, width, height);
	}
	
	public static void registerSpecs(String resourcesLocation, String downloadUrl, String installDirectory, Versions version, Tweaks tweak, boolean verification) {
		Configuration.createSpecsConfiguration(resourcesLocation, downloadUrl, installDirectory, version, tweak, verification);
	}
	
}
