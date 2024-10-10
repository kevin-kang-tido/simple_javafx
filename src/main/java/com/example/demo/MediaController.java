package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class MediaController {

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playButton, pauseButton, stopButton, closeButton;

    @FXML
    private Slider volumeSlider, timeSlider;

    @FXML
    private MenuItem openFile;

    private MediaPlayer mediaPlayer;

    public void initialize() {
        // Initialize controls and events
        openFile.setOnAction(event -> openMediaFile());

        playButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        pauseButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        stopButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100);
            }
        });

        closeButton.setOnAction(event -> {
            // Close the window
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void openMediaFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.m4v"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            Media media = new Media(file.toURI().toString());
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnReady(() -> {
                volumeSlider.setValue(mediaPlayer.getVolume() * 100);  // Initialize volume slider
                timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds()); // Set time slider max value

                mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
                    timeSlider.setValue(newTime.toSeconds()); // Update time slider as video plays
                });
            });

            // Seek video when timeSlider is dragged
            timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (timeSlider.isValueChanging() && mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
            });
        }
    }
}
