package View;

import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class WinViewController {

    MyViewModel viewModel;
    String uriString2 = new File("resources/images/winSong.mp3").toURI().toString();
    MediaPlayer player1 = new MediaPlayer( new Media(uriString2));

    public void playMusic(){
        player1.play();
    }


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;

    }


    public void exitP(){
        viewModel.stopServers();
        Platform.exit();
    }

    public void play(ActionEvent event){

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }


}
