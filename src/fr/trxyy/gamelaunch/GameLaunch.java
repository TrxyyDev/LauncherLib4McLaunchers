package fr.trxyy.gamelaunch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import fr.trxyy.account.Account;
import fr.trxyy.base.Configuration;
import fr.trxyy.interfaces.utils.OSUtil;
import fr.trxyy.interfaces.utils.OSUtil.OperatingSystem;
import fr.trxyy.interfaces.utils.Wrapper;

public class GameLaunch {
	
	public static File workingDirectory = OSUtil.getDirectory();
	public static String gameDir = new File(workingDirectory, "bin").getAbsolutePath();
	public static String gameFile = new File(gameDir, "client.jar").getAbsolutePath();
	public static String nativesDir = new File(workingDirectory, "natives").getAbsolutePath();
	public static String assetsDir = new File(workingDirectory, "resources").getAbsolutePath();
	public static String librariesDir = new File(workingDirectory, "libraries").getAbsolutePath();
	
	public static Versions version = Configuration.getVersion();
	public static String minecraftArguments = version.getArguments();
	public static Tweaks tweak = Configuration.getTweak();
	
	private static File launchFile;
	
	public static void createInstance() {
		ProcessBuilder pb = new ProcessBuilder(new String[0]);
		ArrayList<String> commands = new ArrayList();
		commands.add(getJavaDir());
		
		boolean is32Bit = "32".equals(System.getProperty("sun.arch.data.model"));
		String ramArgument = is32Bit ? "-Xmx512M" : "-Xmx1G";
		commands.add(ramArgument);
		
	    if (OSUtil.getOS().equals(OperatingSystem.windows)) {
	    	commands.add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
	    	commands.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
	    	commands.add("-Dfml.ignorePatchDiscrepancies=true");
	    	commands.add("-Djava.library.path=" + new File(nativesDir).getAbsolutePath());
	    	commands.add("-Dminecraft.client.jar=" + gameFile);
	    } else if (OSUtil.getOS().equals(OperatingSystem.linux)) {
	    	commands.add("-Xdock:name=Minecraft");
	    	commands.add("-Xdock:icon=" + new File(new File(assetsDir).getAbsolutePath(), "icons/minecraft.icns").getAbsolutePath());
	    	commands.add("-XX:+UseConcMarkSweepGC");
	    	commands.add("-XX:+CMSIncrementalMode");
	    	commands.add("-XX:-UseAdaptiveSizePolicy");
	    	commands.add("-Djava.library.path=" + new File(nativesDir).getAbsolutePath());
	    	commands.add("-Dminecraft.client.jar=" + gameFile);
	    } else if (OSUtil.getOS().equals(OperatingSystem.macos)) {
	    	commands.add("-Xdock:name=Minecraft");
	    	commands.add("-Xdock:icon=" + new File(new File(assetsDir).getAbsolutePath(), "icons/minecraft.icns").getAbsolutePath());
	    	commands.add("-XX:+UseConcMarkSweepGC");
	    	commands.add("-XX:+CMSIncrementalMode");
	    	commands.add("-XX:-UseAdaptiveSizePolicy");
	    	commands.add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
	    	commands.add("-Djava.library.path=" + new File(nativesDir).getAbsolutePath());
	    	commands.add("-Dminecraft.client.jar=" + gameFile);
	    }
	    
		commands.add("-cp");
		commands.add(createClassPath());
		commands.add(tweak.getMainClass());
		
		minecraftArguments = minecraftArguments.replace("${auth_player_name}", Account.getUsername());
		minecraftArguments = minecraftArguments.replace("${version_name}", version.getVersion());
		minecraftArguments = minecraftArguments.replace("${game_directory}", gameDir);
		minecraftArguments = minecraftArguments.replace("${assets_root}", assetsDir);
		minecraftArguments = minecraftArguments.replace("${assets_index_name}", version.getAssetsIndex());
		minecraftArguments = minecraftArguments.replace("${auth_uuid}", Account.getUuID());
		minecraftArguments = minecraftArguments.replace("${auth_access_token}", Account.getAccessToken());
		minecraftArguments = minecraftArguments.replace("${user_properties}", "{}");
		minecraftArguments = minecraftArguments.replace("${user_type}", "legacy");
		minecraftArguments = minecraftArguments.replace("${version_type}", "release");
		
		String[] args = minecraftArguments.split(" ");
		commands.addAll(Arrays.asList(args));
		
		if (tweak.equals(Tweaks.FORGE_1_8_HIGHER) || tweak.equals(Tweaks.FORGE_1_7_10) || tweak.equals(Tweaks.OPTIFINE) || tweak.equals(Tweaks.SHADER)) {
			commands.add("--tweakClass=" + tweak.getTweakArgument());
		}
		Wrapper.log("Arguments: " + commands.toString());
		if (OSUtil.getOS() == OSUtil.OperatingSystem.windows) {
			createProcess(pb, commands, 0);
		} else if (OSUtil.getOS() == OSUtil.OperatingSystem.macos) {
			createProcess(pb, commands, 1);
		} else if (OSUtil.getOS() == OSUtil.OperatingSystem.linux) {
			createProcess(pb, commands, 2);
		}
	}
	
	private static void createProcess(ProcessBuilder pb, ArrayList<String> commands, int os) {
		if (os == 0) {
			launchFile = OSUtil.writeFile("offwin.bat", commands.toString());
			pb.command(commands);
			pb.inheritIO();
			try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
		else if (os == 1) {
			launchFile = OSUtil.writeFile("offlinux.sh", commands.toString());
			launchFile.setExecutable(true);
			launchFile.setReadable(true);
			launchFile.setWritable(true);
			Process proc  = null;
			try {
				proc = pb.redirectErrorStream(true).start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				proc.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			pb.directory(workingDirectory);
			Wrapper.log(commands.toString());
			pb.command(commands);
			pb.inheritIO();
			try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
		else if (os == 2) {
			launchFile = OSUtil.writeFile("offothers.sh", commands.toString());
			launchFile.setExecutable(true);
			launchFile.setReadable(true);
			launchFile.setWritable(true);
			try {
				Runtime.getRuntime().exec("chmod u=rwx,g=r,o=- " + launchFile.getAbsolutePath());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			Process proc  = null;
			try {
				proc = pb.redirectErrorStream(true).start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				proc.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			pb.directory(workingDirectory);
			Wrapper.log(commands.toString());
			pb.command(commands);
			pb.inheritIO();
			try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
			
	}
	
	private static String createClassPath() {
        String classpath = "";
        ArrayList<File> libs = OSUtil.list(new File(librariesDir));
        String separator = System.getProperty("path.separator");
        for(File lib : libs)
            classpath += lib.getAbsolutePath() + separator;

        classpath += gameFile;
        return classpath;
    }

	public static String getJavaDir() {
		String separator = java.lang.System.getProperty("file.separator");
		String path = java.lang.System.getProperty("java.home") + separator + "bin" + separator;
		if ((OSUtil.getOS() == OSUtil.OperatingSystem.windows) && (new File(path + "javaw.exe").isFile())) {
			return path + "javaw.exe";
		}
		return path + "java";
	}
	
}
