package fr.trxyy.account;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import fr.trxyy.interfaces.utils.Wrapper;

public class GameSaver {
	
	public static File usernameFile;
	public static File filePath;
	private Random rand = new Random();
	
	public GameSaver(File path_) {
		filePath = path_;
		usernameFile = new File(filePath + "/lastlogin.cfg");
		if (!usernameFile.exists()){
			try {
				usernameFile.createNewFile();
			} catch (IOException e) {}
		}
		
	}
	
	public static void saveUsername(String username) {
		try {
			FileWriter fw = new FileWriter(usernameFile);
			fw.write(username);
			fw.close();
			Wrapper.log("Username saved. (" + username + ")");
		} catch (IOException e) {
			Wrapper.err(e.toString());
		}
	}
	
	public static String getUsername(String base) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(usernameFile));
			String username = br.readLine();
			br.close();
			Wrapper.log("Username loaded. (" + username + ")");
			return username;
		} catch (IOException e) {
			Wrapper.err(e.toString());
		}
		return base;
	}
}
