package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
	
public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
       
		//loads the fxml file from "Scene.fxml"
		Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
		//icon placed as the thumbnail of the program.
		Image icon = new Image("C:\\Users\\ACER\\eclipse-workspace\\MusePlayer\\src\\music.png");
        stage.getIcons().add(icon);
        //title of the program
        stage.setTitle("Muse");
        //disallows the user from going fullscreen or changing the size of the window.
		stage.setResizable(false);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		//shows the window stage
		stage.show();
		
		//if a close request is found (The user clicks on the X of the program) close the program.
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				Platform.exit();
				System.exit(0);	
			}		
		});
	}	

	//the launch method is a method that belongs to the application class
	public static void main(String[] args) {
		Application.launch(args);
	}
}