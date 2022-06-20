package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

//initializable is implemented to use the initialize method
public class Controller implements Initializable{
	
	//the fxml annotation will be used to inject all of the fxml objects into the java class.
	@FXML
	private Pane pane;
	@FXML
	private Label songLabel, songLabel1, songLabel2, endLabel, startLabel;
	@FXML
	private Button playButton, pauseButton, startButton, previousButton, nextButton;
	@FXML
	private ComboBox<String> speedBox;
	@FXML
	private Slider volumeSlider;
	@FXML
	private ProgressBar songProgressBar;
	@FXML
	private ImageView songImage, songImage1;
	
	private Media media;
	private MediaPlayer mediaPlayer;
		
	private File directory;
	private File[] files;	
	private ArrayList<File> songs;
	
	private int songNumber;
	
	private Timer timer;
	private TimerTask task;
	
	private boolean running;
	
	//handles the metadata aspect of mp3 files.
    private void handleMetadata(String key, Object value){
    	//image below this comment is the "image not found" image that you see if the metadata does not contain the 
    	//image key, if it does then the image key image will overlap the "image not found" image.
    	Image image = new Image("C:\\Users\\ACER\\eclipse-workspace\\MusePlayer\\src\\application\\da.png", 160,160,false,true);
    	songImage.setImage(image);
    	//changes the head text into the album name as according to the mp3 metadata.
        if (key.equals("album")) {
            songLabel.setText(value.toString());
        }
      //changes the head text into the artist name as according to the mp3 metadata.
        if (key.equals("artist")) {
            songLabel1.setText(value.toString());
        }
      //changes the head text into the title name as according to the mp3 metadata.
        if (key.equals("title")) {
            songLabel2.setText(value.toString());
        }
      //changes the head text into the image name as according to the mp3 metadata.
        if (key.equals("image")) {
            songImage1.setImage((Image)value);
        }
    }
    
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//forms an arraylist of files which store the music for easy accessibility.
		songs = new ArrayList<File>();
		
		//the location of the music folder, from which will be put into the array list.
		directory = new File("C:\\Users\\ACER\\eclipse-workspace\\MusePlayer\\src\\music");
		
		//listfiles returns an array of file paths from the directory folder which is the music folder mentioned above.
		files = directory.listFiles();
		
		//if files is present run this code.
		if(files != null) {
			
			//for file in files add the file to the array list
			for(File file : files) {
				
				songs.add(file);
			}
		}
		
		//the Media class takes a string of the filepath as a parameter.
		//toURI is used to gain a more specific address of the filepath.
		//it starts by taking the value of the songs array list at the 0th index.
		media = new Media(songs.get(songNumber).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
	     
		//metadata is checked to find the available information on the mp3 file
		//getmetadata returns returns a map that contains keys and objects with the infomation we need
		//mapchangelistener allows us to detect the changes made such as when a key is added
		media.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
//			System.out.println(media.getMetadata());
            if (ch.wasAdded()) {
                handleMetadata(ch.getKey(), ch.getValueAdded());
            }
        });
	
		//displays all of the available speeds in the combobox.
		int[] speed = {10, 15,25, 50, 75, 100, 125, 150, 175, 200, 1000};
		for(int i = 0; i < speed.length; i++) {	
			speedBox.getItems().add(Integer.toString(speed[i])+"%");
		}
		
		//anonymous change listener
		volumeSlider.valueProperty().addListener((NULL) ->{
					//sets the volume of the song according to the value of the slider 
					//increasing the height of the slider increases the value thus the volume and vice versa.
					mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);			
			}
		);
		
		//the color of the progress bar
		songProgressBar.setStyle("-fx-accent: #00FF00;");
		
		//sets the time seen on the duration and current time of the song
		setTime();
		
	}
			

	public void setTime() {
			//a listener that takes the current time of the media player.
			mediaPlayer.currentTimeProperty().addListener((NULL) ->{
			
			int i, j, k, m;
			double current = mediaPlayer.getCurrentTime().toSeconds();
			double end = media.getDuration().toSeconds();
			//divided by 60 and casted to int to get the minutes of the song without the float.
			//modulo 60 of the time to get the seconds and again casted to int to remove float values.
			i = (int) (end / 60);
			j = (int) (end % 60);
			k = (int) (current / 60);
			m = (int) (current % 60);
			
			//startlabel is the current time of the song, in the program it is shown left of the progress bar 
			//and endlabel is the total duration of the song shown on the right side of the proress bar.
			//the if runs when the value of the seconds is less than 10 thus needing a 0 to be at the front of the
			//timer for it to makes sense.
			if(m < 10) {
				startLabel.setText(k + ":0" + m);
			}
			else {
			startLabel.setText(k + ":" + m);
			}
			
			if(j < 10) {
				endLabel.setText(i + ":0" + j);
			}
			else {
            endLabel.setText(i + ":" + j);
			}
		});
		
	}
	public void playMedia() {
		
		//begins the timer which is how the progress bar moves
		beginTimer();
		changeSpeed(null);
		mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
		mediaPlayer.play(); 
	}
	
	public void pauseMedia() {
		
		//the timer stops when the media is paused to stop the progress bar from moving.
		cancelTimer();
		mediaPlayer.pause();
	}
	
	public void startMedia() {
		
		//since the song starts over the progress bar regresses back to its original position.
		songProgressBar.setProgress(0);
		//the mediaplayer looks to play the media at the 0th second mark. starting from the beginning.
		mediaPlayer.seek(Duration.seconds(0));
	}
	
	public void previousMedia() {
				
		//if songnumber not at 0 then decrease the song number to go back to the previous track on the array.
		if(songNumber > 0) {
			//go to the previous element of the arraylist
			songNumber--;
			
			//stops the current song playing
			mediaPlayer.stop();
			
			//stops the timer if the player wasn't already paused
			if(running) {
				
				cancelTimer();
			}
			
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			songImage1.setImage(null);
			
			media.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
	            if (ch.wasAdded()) {
	                handleMetadata(ch.getKey(), ch.getValueAdded());
	            }
	        });
			
			playMedia();
			setTime();
		}
		//if songnumber is 0 then songnumber should be set to the last index.
		else {
			
			songNumber = songs.size() - 1;
			
			mediaPlayer.stop();
			
			songImage1.setImage(null);
			
			if(running) {
				
				cancelTimer();
			}
			
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			
			media.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
	            if (ch.wasAdded()) {
	                handleMetadata(ch.getKey(), ch.getValueAdded());
	            }
	        });
			
			playMedia();
			setTime();
		}
	}	
	
	public void nextMedia() {
		//if the songnumber is still less than the size of the arraylist then keep going
		if(songNumber < songs.size() - 1) {
			
			songNumber++;
			
			mediaPlayer.stop();
			
			if(running) {
				
				cancelTimer();
			}
			
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			
			songImage1.setImage(null);
			
			media.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
	            if (ch.wasAdded()) {
	                handleMetadata(ch.getKey(), ch.getValueAdded());
	            }
	        });
			
			playMedia();
			setTime();
		}
		//if it isnt then start over from index 0.
		else {
			
			songNumber = 0;
			
			mediaPlayer.stop();
			
			songImage1.setImage(null);
			
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			
			media.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
	            if (ch.wasAdded()) {
	                handleMetadata(ch.getKey(), ch.getValueAdded());
	            }
	        });
			
			playMedia();
			setTime();
		}
	}
	
	public void changeSpeed(ActionEvent event) {
		
		if(speedBox.getValue() == null) {
			//if combobox is not touched then the music will play in normal speed.
			mediaPlayer.setRate(1);
		}
		else {
			//places the combobox value into a string array of two compromising of the int value and the string "%".
			String[] rate = speedBox.getValue().split("%");
			//sets the rate of the song playing.
			mediaPlayer.setRate(Integer.parseInt(rate[0]) * 0.01);
		}
	}
	
	public void beginTimer() {
		
		//new timer object to start the timer for the progress bar.
		timer = new Timer();
		
		//a timer task is an object which contains a method that can be run every now and then.
		task = new TimerTask() {
			
			@Override
			public void run() {
				
				running = true;
				double current = mediaPlayer.getCurrentTime().toSeconds();
				double end = media.getDuration().toSeconds();
				
				//progress of the bar, in decimal. represents the length of the current progress
				songProgressBar.setProgress(current/end);
				
				//once the current and end are the same it will cancel the timer, ending the clock.
				if(current/end == 1) {
					
					cancelTimer();
				}
			}
		};
		
		//schedule a method to be run (timer task) every 1000 ms, the middle parameter is the time at which
		//the first time the method should be run.
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	//stops the clock
	public void cancelTimer() {
		running = false;
		timer.cancel();
	}
	
	
	
	
}

