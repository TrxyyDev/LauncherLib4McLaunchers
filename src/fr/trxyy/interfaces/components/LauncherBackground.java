package fr.trxyy.interfaces.components;

import fr.trxyy.base.Configuration;
import fr.trxyy.interfaces.utils.ResourceLocation;
import fr.trxyy.interfaces.utils.Wrapper;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class LauncherBackground {
	
	public ResourceLocation resourceLocation = new ResourceLocation();
    public int posX = 0;
    public int posY = 0;
    public static double opacity = 1.0;
    public static MediaPlayer player;
    public static MediaView viewer;
	
	public LauncherBackground(Media f, Pane root) {
        Wrapper.log("Loading Media Background...");
		player = new MediaPlayer(f);
		viewer = new MediaView(player);
        viewer.setFitWidth(Configuration.getWidth());
        viewer.setFitHeight(Configuration.getHeight());
        player.setAutoPlay(true);
        viewer.setPreserveRatio(false);
        viewer.setOpacity(opacity);
        player.setCycleCount(-1);
        player.play();
        
        root.getChildren().add(viewer);
	}
	
	public LauncherBackground(Media f, double opa, Pane root) {
        Wrapper.log("Loading Media Background...");
		MediaPlayer player = new MediaPlayer(f);
		MediaView viewer = new MediaView(player);
        viewer.setFitWidth(Configuration.getWidth());
        viewer.setFitHeight(Configuration.getHeight());
        player.setAutoPlay(true);
        viewer.setPreserveRatio(false);
        viewer.setOpacity(opa);
        player.setCycleCount(-1);
        player.play();
        
        root.getChildren().add(viewer);
	}
	
	public LauncherBackground(Media f, int posX, int posY, int sizeX, int sizeY, Pane root) {
        Wrapper.log("Loading Media Background...");
		MediaPlayer player = new MediaPlayer(f);
		MediaView viewer = new MediaView(player);
        viewer.setFitWidth(sizeX);
        viewer.setFitHeight(sizeY);
        viewer.setLayoutX(posX);
        viewer.setLayoutX(posY);
        player.setAutoPlay(true);
        viewer.setPreserveRatio(false);
        viewer.setOpacity(opacity);
        player.setCycleCount(-1);
        player.play();
        
        root.getChildren().add(viewer);
	}
	
	public LauncherBackground(Media f, int posX, int posY, int sizeX, int sizeY, double opa, Pane root) {
        Wrapper.log("Loading Media Background...");
		MediaPlayer player = new MediaPlayer(f);
		MediaView viewer = new MediaView(player);
        viewer.setFitWidth(sizeX);
        viewer.setFitHeight(sizeY);
        viewer.setLayoutX(posX);
        viewer.setLayoutX(posY);
        player.setAutoPlay(true);
        viewer.setPreserveRatio(false);
        viewer.setOpacity(opa);
        player.setCycleCount(-1);
        player.play();
        
        root.getChildren().add(viewer);
	}
	
	public static void setOpacity(double opaci) {
		opacity = opaci;
	}
	
	public static MediaPlayer getPlayer() {
		return player;
	}
	
	public static MediaView getViewer() {
		return viewer;
	}
}
