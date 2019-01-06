package fr.trxyy.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fr.trxyy.base.Configuration;
import fr.trxyy.gamelaunch.GameLaunch;
import fr.trxyy.interfaces.utils.FileUtil;
import fr.trxyy.interfaces.utils.OSUtil;
import fr.trxyy.interfaces.utils.Wrapper;
import javafx.application.Platform;

public class GameDownloader {
	public int downloadedFiles = 0;
	public int needToDownload = 0;
	public int totalSize = 0;
	public int downloadedSize;
	public double percentage = 0;

	public static HashMap<String, LauncherFile> filesToDownload = new HashMap();
	public Thread updateThread;
	public GameUpdater updater = new GameUpdater();

	public GameDownloader(HashMap<String, LauncherFile> totalFiles, Thread thread) {
		this.filesToDownload = totalFiles;
		this.updateThread = thread;
		this.needToDownload = totalFiles.size();
		this.totalSize = this.calcSize();
		Wrapper.log(this.needToDownload + " files need to be up to date.");
	}

	public void startDownloading() {
		for (String name : this.filesToDownload.keySet()) {
			String fileDest = name.replaceAll(Configuration.DOWNLOAD_URL, "");
			String fileName = fileDest;
			int index = fileName.lastIndexOf("\\");
			String dirLocation = fileName.substring(index + 1);
			
			if (!name.endsWith("/")) {
				this.download(Configuration.getDownloadUrl() + name, OSUtil.getDirectory() + File.separator + dirLocation, dirLocation);
			}
			if (this.downloadedFiles == this.needToDownload) {
				this.updateThread.interrupt();
				this.updater.updating = false;
				Wrapper.log("Download finished.");
				Wrapper.log("Verification de l'installation...");
				if (Configuration.getVerification()) {
					verifyIntegrity(OSUtil.getDirectory());
				}
				else {
					Wrapper.log("Skipped verification.");
				}
				GameLaunch.createInstance();
			}
		}
	}
	
	public void startDownloadingLauncher() {
		for (String name : this.filesToDownload.keySet()) {
			String fileDest = name.replaceAll(Configuration.DOWNLOAD_URL, "");
			String fileName = fileDest;
			int index = fileName.lastIndexOf("\\");
			String dirLocation = fileName.substring(index + 1);
			
			if (!name.endsWith("/")) {
				this.downloadLauncher(Configuration.getDownloadUrl() + name, OSUtil.getDirectory() + File.separator + dirLocation, dirLocation);
			}
			if (this.downloadedFiles == this.needToDownload) {
				this.updateThread.interrupt();
				this.updater.updating = false;
				Wrapper.log("Download finished.");
				Wrapper.log("Verification de l'installation...");
				launchLauncher();
			}
		}
	}
	
	public void launchLauncher() {
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", OSUtil.getDirectory() + "/launcher.jar");
		Process p = null;
		try {
			p = pb.start();
		} catch (IOException e) {e.printStackTrace();}
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String s = "";
		try {
			while((s = in.readLine()) != null){
			    System.out.println(s);
			}
		} catch (IOException e) {e.printStackTrace();}
		int status = 0;
		try {
			status = p.waitFor();
		} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println("Exited with status: " + status);
	}
	
	public void verifyIntegrity(File dir) {
		File[] subDirs = dir.listFiles();
		int index = 0;
		boolean bool = false;
		if (dir.isDirectory()) {
			for (index = 0; index < subDirs.length; index++) {
				File next = subDirs[index];
				verifyIntegrity(next);
				File realEmpl = new File("" + subDirs[index].toString().replace(OSUtil.getDirectory() + "\\", ""));
				File realEmpl_ = new File("" + subDirs[index]);
				
				if (!realEmpl_.isDirectory()) {
					bool = exists(Configuration.getDownloadUrl() + "" + subDirs[index].toString().replace(OSUtil.getDirectory() + "\\", ""));
					if (!bool) {
						File f = new File(OSUtil.getDirectory() + "/" + subDirs[index].toString().replace(OSUtil.getDirectory() + "\\", ""));
						FileUtil.deleteSomething(f.getAbsolutePath());
					}
				}
//				else {
//					String fileName = realEmpl_.getName();
//					fileName = fileName.replace("\\", "/");
//					String[] splittedFileName = fileName.split("/");
//					String simpleFileName = splittedFileName[splittedFileName.length - 1];
//					Path path = Paths.get(simpleFileName);
//					File fileToCheck = path.toFile();
//					boolean bool2 = fileToCheck.isDirectory();
//						if(!(realEmpl_.list().length > 0)) {
//							Wrapper.log("Fichier: " + realEmpl_ + " est invalide. Suppression en cours...");
//							FileUtil.deleteSomething(next.getPath());
//						}
//				}
			}
		}
	}
	
	public static boolean exists(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
//	      HttpURLConnection.setInstanceFollowRedirects(false);
	      HttpURLConnection huc = (HttpURLConnection) new URL(URLName).openConnection();
	      huc.setRequestMethod("HEAD");
	      return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	  }

	public void download(String fileUrl, String fileName, String flNm) {
		Wrapper.log("Downloading: " + flNm);
		updater.getProgressBar().setCurrentFile(flNm);
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int filesize = connection.getContentLength();
			float totalDataRead = 0;

			File file = new File(fileName);
			file.getParentFile().mkdirs();
			Wrapper.log("Trying to create file: " + file.getPath());
			file.createNewFile();

			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			FileOutputStream fos = new FileOutputStream(fileName);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int read = 0;
			while ((read = in.read(data, 0, 1024)) >= 0) {
				this.downloadedSize += read;
				bout.write(data, 0, read);

				double total = this.totalSize;
				double actuel = this.downloadedSize;
				if (total == 0.0D) {
					total = 0.1D;
				}
				percentage = ((actuel / total * 100) / 10000.0D);
			}
			Platform.runLater(() -> updater.getProgressBar().setProgress(percentage));
			bout.close();
			in.close();
			this.downloadedFiles++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadLauncher(String fileUrl, String fileName, String flNm) {
		Wrapper.log("Downloading: " + flNm);
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int filesize = connection.getContentLength();
			float totalDataRead = 0;

			File file = new File(fileName);
			file.getParentFile().mkdirs();
			Wrapper.log("Trying to create file: " + file.getPath());
			file.createNewFile();

			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			FileOutputStream fos = new FileOutputStream(fileName);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int read = 0;
			while ((read = in.read(data, 0, 1024)) >= 0) {
				this.downloadedSize += read;
				bout.write(data, 0, read);

				double total = this.totalSize;
				double actuel = this.downloadedSize;
				if (total == 0.0D) {
					total = 0.1D;
				}
				percentage = ((actuel / total * 100) / 10000.0D);
			}
			bout.close();
			in.close();
			this.downloadedFiles++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int calcSize() {
		double size = 0.0D;
		int i = 0;
		for (Map.Entry<String, LauncherFile> entry : filesToDownload.entrySet()) {
			size += ((LauncherFile) entry.getValue()).getSize();
			i++;
		}
		Wrapper.log("Total size of needed files: " + size);
		return (int) (size / 100.0D);
	}
}
