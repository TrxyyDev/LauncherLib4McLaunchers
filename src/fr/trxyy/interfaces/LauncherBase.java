package fr.trxyy.interfaces;

import java.awt.Point;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fr.trxyy.base.Configuration;
import fr.trxyy.interfaces.components.LauncherAlert;
import fr.trxyy.interfaces.utils.Wrapper;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class LauncherBase {

	final Point dragDelta = new Point();

	public LauncherBase(Stage stage, Scene scene, StageStyle style, boolean moveable) {
		Wrapper.log("Initializing...");
		
	    try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			new LauncherAlert(e.toString(), AlertType.ERROR);
			Wrapper.err(e.toString());
		}
		
		stage.initStyle(style);
		stage.setResizable(false);
		stage.setWidth(Configuration.getWidth());
		stage.setHeight(Configuration.getHeight());
		stage.setTitle(Configuration.getTitle());
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
    	});
		if (moveable) {
			scene.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					dragDelta.x = (int) (stage.getX() - mouseEvent.getScreenX());
					dragDelta.y = (int) (stage.getY() - mouseEvent.getScreenY());
				}
			});
			scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					stage.setX(mouseEvent.getScreenX() + dragDelta.x);
					stage.setY(mouseEvent.getScreenY() + dragDelta.y);
				}
			});
		}
		stage.setScene(scene);
		Wrapper.log("Displaying Panel... (Trying)");
		stage.show();
		Wrapper.log("Displaying Panel... (Successfully)");
	}
	
	public static void setIconImage(Stage primaryStage, Image img) {
		primaryStage.getIcons().add(img);
	}

}
