package fr.trxyy.interfaces.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import fr.trxyy.base.Configuration;
import fr.trxyy.interfaces.components.LauncherAlert;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class ResourceLocation {
	
	public ResourceLocation() {
		
	}
	
	public static Media getMedia(String media) {
		Media theMedia = null;
		final URL resourceUrl = ResourceLocation.class.getResource(Configuration.getResourceLocation() + media);
		try {
			theMedia = new Media(resourceUrl.toURI().toString());
		} catch (URISyntaxException e) {
	        new LauncherAlert("" + e.toString(), AlertType.ERROR);
		}
		return theMedia;
	}
	
	public static Image loadImage(String image) {
		
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(ResourceLocation.class.getResource(Configuration.getResourceLocation() + image));
		} catch (IOException e) {
		}
		Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
		return fxImage;
	}
}
