package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class OpenViewController implements Initializable {

    @FXML
    private MediaView media;
    MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("/images/openVideo.mp4").toExternalForm()));


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        player.setAutoPlay(true);
        media.setMediaPlayer(player);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        media.setViewport(primaryScreenBounds);
    }

    public void stopVideo(ActionEvent event) throws IOException {
        player.stop();

        MyModel model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //-------------
        System.out.println("stop me!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("A Song of Ice and Fire");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent viewPageParent =  fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene viewPageScene = new Scene(viewPageParent);
        //--------------
        MyViewController view = fxmlLoader.getController();
        view.setStageCloseEvent(stage);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        stage.setScene(viewPageScene);
        viewPageScene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());


       stage.setMaximized(true);
        view.setResizeEvent(viewPageScene);

        stage.show();
    }

}
