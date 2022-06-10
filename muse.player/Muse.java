package muse.player;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.MapChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.Reflection;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.lang.Object;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;



public class Muse implements Initializable {

    private MediaPlayer player1;

    @FXML
    Button PlayBtn, PauseBtn, RewindBtn, SkipBtn, ShuffleBtn, RepeatBtn;

    @FXML
    Menu FileMenu, InfoMenu;

    @FXML
    MenuItem OpenItem, ExitItem, AboutItem;

    @FXML
    Label SongTitle, SongArtist, SongAlbum, FilenameLabel, endLabel, startLabel;

    @FXML
    Slider VolumeSlider, TimeSlider;

    @FXML
    ImageView PlayImg, RewindImg, SkipImg, ShuffleImg, RepeatImg, SongAlbumCover;

    @FXML
    Stage stage;

    private DecimalFormat formatter = new DecimalFormat("00.00");
    private Label totalDuration = new Label();
    private Label currentDuration = new Label();
    private Duration totalTime;
    private SliderBar timeSlider = new SliderBar();
    boolean playing;
    private static double MIN_CHANGE = 0.1;



    @SuppressWarnings(value = "unchecked")
    @FXML
    public void initialize(URL location, ResourceBundle resources) {


        Media pick = null;
        FilenameLabel.setText("filename");

        //the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an audio file.");
        fileChooser.setInitialDirectory(new File("C:\\Users\\ACER\\eclipse-workspace\\muse\\src\\Music"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.m4a"));
        FilenameLabel.setText(fileChooser.getInitialFileName());
        File song = fileChooser.showOpenDialog(stage);
 
                if (song != null) {
                    //File f = new File(song.toString());
//                    String abd = song.toString();
//                    String abcd = "file:///" +abd;
//                    abcd = abcd.replace("\\\\", "/");

//                    URI uri123 = null;
//                    try {
//                        uri123 = new URI("File:///" +song.toString());
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    File fileABC = new File(uri123);


                    String fileHeaderName = song.getName();
                    //String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, song.getName().length());

                    FilenameLabel.setText(fileHeaderName);




                    String loc = new File(song.toString()).toURI().toString();

                    //final String fileAsString;
                    System.out.println(loc);
                    pick = new Media(loc);
                    player1 = new MediaPlayer(pick);

                    Media songSelection = new Media(loc);
                    player1.stop();
                    player1 = new MediaPlayer(songSelection);

                    player1.play();
                    //TimeSlider.setValue(player1.getCurrentTime().toMinutes());

                } else {
                    //chosen.setText(null);
                }

        pick.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
            if (ch.wasAdded()) {
                handleMetadata(ch.getKey(), ch.getValueAdded());
            }
        });

        TimeSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                player1.pause();
                player1.seek(Duration.seconds(TimeSlider.getValue()));
                //player1.seek(Duration.seconds(TimeSlider.getValue()));
                player1.play();
            }
            else{
            }
        });

        TimeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!TimeSlider.isValueChanging()) {
                double currentTime = player1.getCurrentTime().toMinutes();
                if (Math.abs(currentTime - newValue.doubleValue()) < 0)/*> MIN_CHANGE*/ {
                    player1.seek(Duration.seconds(newValue.doubleValue()));
                }
            }
            else{

            }
        });

        player1.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!TimeSlider.isValueChanging()) {
                TimeSlider.setValue(newTime.toSeconds());
            }
        });

        // Listener for Slider adjustments
        TimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            DecimalFormatSymbols dfSymbols = new DecimalFormatSymbols(Locale.getDefault());
            dfSymbols.setDecimalSeparator(':');
            DecimalFormat df = new DecimalFormat("#.##", dfSymbols);
            df.setMinimumFractionDigits(2);

            // Current Time code
            String playerString = df.format(player1.getCurrentTime().toMinutes()/1.67);
            System.out.println(playerString);
            String[] filteredPlayer = playerString.split(":");
            String trackSeconds = filteredPlayer[1];
            String finalSeconds = "00";
            int remSeconds1;
            int playerMinutes = 0;

            String playerString2 = playerString.replaceAll("[-+.^:,]","");
            int PlayerInt = Integer.parseInt(playerString2);
            int CurrSecond = Integer.parseInt(trackSeconds);

            // If minutes is divisible by 60
            if (CurrSecond % 60 == 0) {
                remSeconds1 = CurrSecond % 60;
                // If Prt2counter is less than 10
                if (remSeconds1 >= 0 && remSeconds1 < 10) {
                    finalSeconds = "0" + remSeconds1;
                    playerMinutes = PlayerInt / 60;
                }
                else {
                    finalSeconds = "" +remSeconds1;
                    playerMinutes = PlayerInt / 60;
                }
            }
            // If Parts2Num is NOT divisible by 60
            else if (CurrSecond % 60 != 0) {
                remSeconds1 = CurrSecond % 60;
                if (remSeconds1 >= 0 && remSeconds1 < 10) {
                    finalSeconds = "0" + remSeconds1;
                    playerMinutes = PlayerInt / 60;
                }
                else {
                    finalSeconds = "" +remSeconds1;
                    playerMinutes = PlayerInt / 60;
                }
            }
            else {
                System.out.println("issue");
            }
            //String result123 = minBit +":" +part2;
            double PlayingTime = Double.parseDouble(playerMinutes + "." + finalSeconds);


            // Total Time code
            String durationString = df.format(player1.getTotalDuration().toMinutes());
            String [] filteredDuration = new String[0];

            filteredDuration = durationString.split(":");
            String durationSeconds = filteredDuration[1];
            filteredDuration = durationString.split(":");
            durationSeconds = filteredDuration[1];
            String totalSeconds;
            //int remSeconds2;

            String durationString2 = durationString.replaceAll("[-+.^:,]","");
            
            int seconds2 = Integer.parseInt(durationSeconds);
            double trueSeconds = (seconds2/100.0) * 60;
            int durationSeconds2 = (int) trueSeconds;
            totalSeconds = "" +durationSeconds2;
            
            if(Integer.parseInt(filteredDuration[1]) <= 10){
            	totalSeconds = "0" + totalSeconds;
            }

            String durationMinutes = filteredDuration[0];

            //String result123 = minBit +":" +part2;
            double DurationTime = Double.parseDouble(durationMinutes + "." + totalSeconds);
            

            // Start Time label
            startLabel.setText(df.format(PlayingTime));

            // Total Time label
            endLabel.setText(df.format(DurationTime));
        });


        // Play the track and select the playButton
        player1.play();
        playing = true;
        //PlayBtn.setSelected(true);

        player1.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                TimeSlider.setValue(newValue.divide(totalTime.toMillis()).toMillis() * 100.0);
                currentDuration.setText(String.valueOf(formatter.format(newValue.toSeconds())));
            }
        });

        player1.setOnReady(() -> {
            // Set the total duration
            totalTime = player1.getMedia().getDuration();
            totalDuration.setText(" / " + String.valueOf(formatter.format(Math.floor(totalTime.toSeconds()))));
        });


        //Open File menu
        OpenItem.setOnAction(new EventHandler() {
            public void handle(Event event) {
                //Create a file chooser
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose an audio file.");
                fileChooser.setInitialDirectory(new File("C:\\\\Users\\\\ACER\\\\eclipse-workspace\\\\muse\\\\src\\\\Music"));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.m4a"));
                FilenameLabel.setText(fileChooser.getInitialFileName());
                File song = fileChooser.showOpenDialog(stage);
                if (song != null) {
                    String fileName = song.getName();
                    FilenameLabel.setText(fileName);
                    String abd5 = new File(song.toString()).toURI().toString();

                    //final String fileAsString;
                    Media songSelection = new Media(abd5);
                    player1.stop();
                    player1 = new MediaPlayer(songSelection);

                    player1.play();

                } else {
                }
            }
        });

        //Exit Item Menu option
        ExitItem.setOnAction(e -> {
            player1.stop();
            stage.close();
        });

        // TimeSlider colour
        TimeSlider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (TimeSlider.getValue() - TimeSlider.getMin()) / (TimeSlider.getMax() - TimeSlider.getMin()) * 100.0;
            return String.format("-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
                    + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);", percentage, percentage);
        }, TimeSlider.valueProperty(), TimeSlider.minProperty(), TimeSlider.maxProperty()));


        // VolumeSlider colour
        VolumeSlider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (VolumeSlider.getValue() - VolumeSlider.getMin()) / (VolumeSlider.getMax() - VolumeSlider.getMin()) * 100.0;
            return String.format("-slider-track-color: linear-gradient(to top, -slider-filled-track-color 0%%, "
                    + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);", percentage, percentage);
        }, VolumeSlider.valueProperty(), VolumeSlider.minProperty(), VolumeSlider.maxProperty()));

        //About Menu
        AboutItem.setOnAction(new EventHandler() {
            public void handle(Event event) {
                Alert alert = new Alert(AlertType.INFORMATION, "Muse Music Player", ButtonType.OK);// set alert type
                // set content text
                alert.setTitle("About");
                alert.setHeaderText("Muse Music Player");
                alert.setContentText("Muse Music Player was developed by Peter Nelson Subrata May/June 2022");
                // show the dialog
                alert.showAndWait();
                return;
            }
        });
        PlayBtn.setStyle("-fx-border-color: transparent;");
        // Play Button
        PlayBtn.setOnAction(e -> {
            if (playing == true) {
                player1.pause();
                playing = false;
            } else {
                player1.play();
                playing = true;
            }
        });

        // Skip Button
        SkipBtn.setOnAction(new EventHandler() {
            public void handle(Event event) {
            }
        });

        // Rewind Button
        RewindBtn.setOnAction(new EventHandler() {
            public void handle(Event event) {
            }
        });

        // Shuffle Button
        ShuffleBtn.setOnAction(new EventHandler() {
            public void handle(Event event) {
            }
        });

        // Repeat Button
        RepeatBtn.setOnAction(new EventHandler() {
            public void handle(Event event) {
            }
        });
    }

    private void handleMetadata(String key, Object value) {
        if (key.equals("album")) {
            SongAlbum.setText(value.toString());
        } else if (key.equals("artist")) {
            SongArtist.setText(value.toString());
        } if (key.equals("title")) {
            SongTitle.setText(value.toString());
        } if (key.equals("image")) {
            SongAlbumCover.setImage((Image)value);
        }
    }

    private void createControls(){
        SongArtist = new Label();
        SongArtist.setId("artist");
        SongAlbum = new Label();
        SongAlbum.setId("album");
        SongTitle = new Label();
        SongTitle.setId("title");

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.2);
    }

    private class SliderBar extends StackPane {

        private Slider slider = new Slider();

        private ProgressBar progressBar = new ProgressBar();

        public SliderBar() {
            getChildren().addAll(progressBar, slider);
            bindValues();
        }
        private void bindValues(){
            progressBar.prefWidthProperty().bind(slider.widthProperty());
            progressBar.progressProperty().bind(slider.valueProperty().divide(100));
        }

        public DoubleProperty sliderValueProperty() {
            return slider.valueProperty();
        }

        public boolean isTheValueChanging() {
            return slider.isValueChanging();
        }

    }

 // sets necessary data for the music player
    @FXML
    public void setData(Stage stage) {
        this.stage = stage;
    }
}
