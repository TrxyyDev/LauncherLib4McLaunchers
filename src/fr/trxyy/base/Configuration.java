package fr.trxyy.base;

import fr.trxyy.gamelaunch.Tweaks;
import fr.trxyy.gamelaunch.Versions;

public class Configuration {
	/** BASIC SPECS */
	public static String LAUNCHER_TITLE = "";
	public static int LAUNCHER_WIDTH = 0;
	public static int LAUNCHER_HEIGHT = 0;
	/** OTHERS SPECS */
	public static String RESOURCES_LOCATION = "";
	public static String DOWNLOAD_URL = "";
	public static String INSTALL_DIR = "";
	public static Versions LAUNCHER_VERSION = Versions.V1_7_10;
	public static Tweaks TWEAK = Tweaks.VANILLA;
	
	public static boolean VERIFICATION = true;	
	public Configuration() {}
	
	public static void createBasicConfiguration(String launcherTitle, int launcherWidth, int launcherHeight) {
		LAUNCHER_TITLE = launcherTitle;
		LAUNCHER_WIDTH = launcherWidth;
		LAUNCHER_HEIGHT = launcherHeight;
	}
	
	public static void createSpecsConfiguration(String resLocation, String downldUrl, String directory, Versions version, Tweaks tweak, boolean verif) {
		RESOURCES_LOCATION = resLocation;
		DOWNLOAD_URL = downldUrl;
		INSTALL_DIR = directory;
		LAUNCHER_VERSION = version;
		TWEAK = tweak;
		VERIFICATION = verif;
	}
	
	public static String getTitle() {
		return LAUNCHER_TITLE;
	}
	
	public static int getWidth() {
		return LAUNCHER_WIDTH;
	}
	
	public static int getHeight() {
		return LAUNCHER_HEIGHT;
	}
	
	public static String getResourceLocation() {
		return RESOURCES_LOCATION;
	}
	
	public static String getDownloadUrl() {
		return DOWNLOAD_URL;
	}
	
	public static Versions getVersion() {
		return LAUNCHER_VERSION;
	}

	public static String getDirectory() {
		return INSTALL_DIR;
	}
	
	public static Tweaks getTweak() {
		return TWEAK;
	}
	
	public static boolean getVerification() {
		return VERIFICATION;
	}
	
}
