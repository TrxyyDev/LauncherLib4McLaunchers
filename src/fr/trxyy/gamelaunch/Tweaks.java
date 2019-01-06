package fr.trxyy.gamelaunch;

public enum Tweaks {
	
	VANILLA("Vanilla", "net.minecraft.client.main.Main", "not.needed"),
	OPTIFINE("OptiFine", "net.minecraft.launchwrapper.Launch", "optifine.OptiFineTweaker"),
	SHADER("Shader", "net.minecraft.launchwrapper.Launch", "shadersmod.launch.SMCTweaker"),
	FORGE_1_7_10("ForgeOld", "net.minecraft.launchwrapper.Launch", "cpw.mods.fml.common.launcher.FMLTweaker"),
	FORGE_1_8_HIGHER("Forge", "net.minecraft.launchwrapper.Launch", "net.minecraftforge.fml.common.launcher.FMLTweaker");
	
	public final String tweakName;
	public final String mainClass;
	public final String tweakArgument;
	
	private Tweaks(String name, String main, String arg) {
		this.tweakName = name;
		this.mainClass = main;
		this.tweakArgument = arg;
	}
	
	public String getTweakName() {
		return this.tweakName;
	}
	
	public String getMainClass() {
		return this.mainClass;
	}
	
	public String getTweakArgument() {
		return this.tweakArgument;
	}
	
}
