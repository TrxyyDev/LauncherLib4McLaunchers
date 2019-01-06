package fr.trxyy.interfaces.components;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LauncherAlert {

	public LauncherAlert(String text, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle("Erreur");
		alert.setHeaderText("Argh! Une erreur est survenue!");
		alert.setContentText(text);
		alert.show();
	}
}
