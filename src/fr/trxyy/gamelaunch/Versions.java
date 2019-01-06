package fr.trxyy.gamelaunch;

public enum Versions {
	
	V1_7_10("1.7.10", "1.7.10", "--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userProperties=${user_properties} --userType=${user_type}"),
	V1_8("1.8",       "1.8",	"--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userProperties=${user_properties} --userType=${user_type}"),
	V1_9("1.9", 	  "1.9",	"--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userType=${user_type} --versionType=${version_type}"),
	V1_10("1.10", 	  "1.10", 	"--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userType=${user_type} --versionType=${version_type}"),
	V1_11("1.11", 	  "1.11", 	"--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userType=${user_type} --versionType=${version_type}"),
	V1_12("1.12",     "1.12", 	"--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userType=${user_type} --versionType=${version_type}"),
	V1_13("1.13", 	  "1.13", 	"--username=${auth_player_name} --version=${version_name} --gameDir=${game_directory} --assetsDir=${assets_root} --assetIndex=${assets_index_name} --uuid=${auth_uuid} --accessToken=${auth_access_token} --userType=${user_type} --versionType=${version_type}");
	
    private final String version;
    private final String assetsIndex;
    private final String minecraftArguments;
	
	private Versions(String v, String assts, String args) {
		this.version = v;
		this.assetsIndex = assts;
		this.minecraftArguments = args;
	}
	
    public String getVersion()
    {
        return version;
    }
    
    public String getAssetsIndex()
    {
        return assetsIndex;
    }
    
    public String getArguments()
    {
        return minecraftArguments;
    }
}
