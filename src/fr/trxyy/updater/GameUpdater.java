package fr.trxyy.updater;

import java.util.HashMap;

import fr.trxyy.base.Configuration;
import fr.trxyy.gamelaunch.GameLaunch;
import fr.trxyy.interfaces.components.LauncherProgressBar;
import fr.trxyy.interfaces.utils.OSUtil;
import fr.trxyy.interfaces.utils.Wrapper;

public class GameUpdater {

	public static HashMap<String, LauncherFile> filesToDownload = new HashMap();
	public GameDownloader downloadTask;
	public Thread updateThread;
	public boolean updating = false;
	public static LauncherProgressBar progressBar;

	public void checkForUpdate(final GameParser parser, LauncherProgressBar suppBar) {
		if (suppBar != null) {
			progressBar = suppBar;
		} else {
			progressBar = new LauncherProgressBar();
		}
		updateThread = new Thread() {
			public void run() {
				parser.getFilesToDownload();

				if (!filesToDownload.isEmpty()) {
					downloadTask = new GameDownloader(filesToDownload, updateThread);
					updating = true;
					downloadTask.startDownloading();
				} else {
					downloadTask = new GameDownloader(filesToDownload, updateThread);
					updating = false;
					Wrapper.log("Aucune mise a jour requise.");
					if (Configuration.getVerification()) {
						downloadTask.verifyIntegrity(OSUtil.getDirectory());
					}
					else {
						Wrapper.log("Skipped verification.");
					}
					GameLaunch.createInstance();
				}
			}
		};
		updateThread.start();
	}
	
	public void checkForUpdateLauncher(final GameParser parser) {
		updateThread = new Thread() {
			public void run() {
				parser.getFilesToDownload();
				if (!filesToDownload.isEmpty()) {
					downloadTask = new GameDownloader(filesToDownload, updateThread);
					updating = true;
					downloadTask.startDownloadingLauncher();
				} else {
					downloadTask = new GameDownloader(filesToDownload, updateThread);
					updating = false;
					Wrapper.log("Aucune mise a jour requise launcher.");
					if (Configuration.getVerification()) {
						downloadTask.verifyIntegrity(OSUtil.getDirectory());
					}
					else {
						Wrapper.log("Skipped verification.");
					}
					downloadTask.launchLauncher();
				}
			}
		};
		updateThread.start();
	}

	public GameDownloader getDownloader() {
		return this.downloadTask;
	}

	public LauncherProgressBar getProgressBar() {
		return this.progressBar;
	}
	
	public boolean isUpdating() {
		return this.updating;
	}
}
