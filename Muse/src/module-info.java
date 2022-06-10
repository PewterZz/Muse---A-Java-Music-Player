module muse {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	
	opens muse.player to javafx.graphics, javafx.fxml;
}
