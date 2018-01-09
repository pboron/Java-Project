/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odtwarzacz;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author Przemek
 */
public class FXMLDocumentController implements Initializable {
    
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    private String filePath;

    @FXML
    private Slider volumeSlider;
    
    @FXML
    private Slider videoSlider;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a file (*.mp4)", "*.mp4");
  
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(null);
            filePath = file.toURI().toString();
            
            if (filePath != null){
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                
                    DoubleProperty width = mediaView.fitWidthProperty();
                    DoubleProperty height = mediaView.fitHeightProperty();
                
                    width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                    height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
                    
                    volumeSlider.setValue(mediaPlayer.getVolume() * 100);
                    volumeSlider.valueProperty().addListener(new InvalidationListener() {
                        @Override
                        public void invalidated(Observable observable) {
                            mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                        }
                    });
                    
                    videoSlider.maxProperty().bind(Bindings.createDoubleBinding(() -> mediaPlayer.getTotalDuration().toSeconds(),mediaPlayer.totalDurationProperty()));
                    
                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                        @Override
                        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                            videoSlider.setValue(newValue.toSeconds());
                        }
                    });
                    
                    videoSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mediaPlayer.seek(Duration.seconds(videoSlider.getValue()));
                        }
                    });
                    
                mediaPlayer.play();
            }
    }
    
    @FXML
    private void playVideo(ActionEvent event){
        mediaPlayer.play();
    }
    
    @FXML
    private void pauseVideo(ActionEvent event){
        mediaPlayer.pause();
    }
    
    @FXML
    private void stopVideo(ActionEvent event){
        mediaPlayer.stop();
    }
    
    @FXML
    private void backwardVideo(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.millis(5000)));
    }
    
    @FXML
    private void forwardVideo(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.millis(5000)));
    }
    
    @FXML
    private void exitVideo(ActionEvent event){
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
