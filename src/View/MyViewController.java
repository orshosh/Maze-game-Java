package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ChoiceBox;
import javafx.scene.input.*;
import ViewModel.MyViewModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class MyViewController implements Observer,IView {

        boolean musicB = false;
        boolean won = false;
        String uriString = new File("resources/images/gameSong.mp3").toURI().toString();

        MediaPlayer player = new MediaPlayer( new Media(uriString));


        @FXML
        private MyViewModel viewModel;
        public MazeDisplayer mazeDisplayer;
        public CharacterDisplayer characterDisplayer;
        public SolutionDisplayer solutionDisplayer;
        public javafx.scene.control.Button btn_generateMaze;
        public javafx.scene.control.ToggleButton solveB;
        public javafx.scene.control.TextField txtfld_rowsNum;
        public javafx.scene.control.TextField txtfld_columnsNum;
        public javafx.scene.control.Label lbl_rowsNum;
        public javafx.scene.control.Label lbl_columnsNum;
        public javafx.scene.layout.Pane pane;
        public javafx.scene.control.ChoiceBox<String> choiceBox;
        public javafx.scene.control.ChoiceBox<String> choiceBoxWall;





        public void setViewModel(MyViewModel viewModel) {
                this.viewModel = viewModel;
        }

        @Override
        public void displayMaze(Maze maze) {
                mazeDisplayer.setMaze(maze);
                solveB.setDisable(false);

        }

        public void displayCharacter(Maze maze){
                int characterPositionRow = viewModel.getCharacterPositionRow();
                int characterPositionColumn = viewModel.getCharacterPositionColumn();
                if(mazeDisplayer.checkWin(characterPositionRow,characterPositionColumn)){
                        winWindow();
                }
                else {
                        characterDisplayer.setMaze(maze);
                        characterDisplayer.setCharacterPosition(characterPositionRow,characterPositionColumn);
                        viewModel.characterPositionRow.set(characterPositionRow + "");
                        viewModel.characterPositionColumn.set(characterPositionColumn + "");

                }

        }

        public void winWindow(){

                try {
                        won = true;
                        player.stop();
                        musicB=false;
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        Parent viewPageParent = null;
                        viewPageParent = fxmlLoader.load(getClass().getResource("WinView.fxml").openStream());
                        Stage stage = new Stage();
                        stage.setTitle("A Song Of Ice And Fire");
                        Scene WinPageScene = new Scene(viewPageParent);
                        stage.setScene(WinPageScene);
                        WinPageScene.getStylesheets().add(getClass().getResource("WinStyle.css").toExternalForm());

                        WinViewController view = fxmlLoader.getController();
                        view.setViewModel(viewModel);
                        view.playMusic();
                        music(musicB,"player");

                        mazeDisplayer.clear();
                        characterDisplayer.clear();
                        solutionDisplayer.clear();
                        stage.show();

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        public void displaySolution(ArrayList<AState> solutionPath,Maze maze)
        {
               // solutionDisplayer.setVisible(true);
                solutionDisplayer.setMaze(maze);
                solutionDisplayer.setSolution(solutionPath);
        }

        @Override
        public void update(Observable o, Object arg) {
                if (o == viewModel) {
                        displayMaze(viewModel.getMaze());
                        btn_generateMaze.setDisable(false);
                        displayCharacter(viewModel.getMaze());

                        if(arg.equals("1")){
                                solutionDisplayer.setVisible(true);
                                displaySolution(viewModel.getSolution(),viewModel.getMaze());
                        }

                }
        }

        public void generateMaze() {
                try {

                        int heigth = Integer.valueOf(txtfld_rowsNum.getText());
                        int width = Integer.valueOf(txtfld_columnsNum.getText());
                        if (heigth < 2 || width < 2) {
                                showAlert("You need to insert values larger than two");
                        } else {
                                won = false;
                                btn_generateMaze.setDisable(true);
                                solveB.setDisable(true);
                                viewModel.generateMaze(width, heigth);
                                solutionDisplayer.setVisible(false);
                                music(musicB, "player");

                        }
                }
                catch(Exception e){
                        showAlert("You need to insert numerical values");
                }
        }

        public void music (boolean music,String num){
                if(num.equals("player")) {
                        if (!music) {
                                player.play();
                                musicB = true;
                        }
                }

        }
        public void stopOrPlay(ActionEvent event){
                if(event.getEventType()== ActionEvent.ACTION){
                        if(musicB){
                                player.pause();
                                musicB=false;
                        }
                        else{
                                player.play();
                                musicB=true;
                        }

                }
        }

        public void solveMaze(ActionEvent event) {
                if(event.getEventType()== ActionEvent.ACTION){
                        if(!solutionDisplayer.isVisible()){
                                showAlert("Solving maze..");
                                viewModel.solveMaze();
                                solutionDisplayer.setVisible(true);
                        }
                        else{
                                solutionDisplayer.setVisible(false);
                        }
                }
        }


                private void showAlert(String alertMessage) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(alertMessage);
                alert.show();
        }

        public void KeyPressed(KeyEvent keyEvent) {
                viewModel.moveCharacter(keyEvent);
                keyEvent.consume();
        }

        //region String Property for Binding
        public StringProperty characterPositionRow = new SimpleStringProperty();

        public StringProperty characterPositionColumn = new SimpleStringProperty();

        public String getCharacterPositionRow() {
                return characterPositionRow.get();
        }

        public StringProperty characterPositionRowProperty() {
                return characterPositionRow;
        }

        public String getCharacterPositionColumn() {
                return characterPositionColumn.get();
        }

        public StringProperty characterPositionColumnProperty() {
                return characterPositionColumn;
        }


        public void setResizeEvent(Scene scene) {
                scene.widthProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                                mazeDisplayer.widthProperty().bind(pane.widthProperty());
                                characterDisplayer.widthProperty().bind(pane.widthProperty());
                                solutionDisplayer.widthProperty().bind(pane.widthProperty());
                        }
                });
                scene.heightProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                               mazeDisplayer.heightProperty().bind(pane.heightProperty());
                               characterDisplayer.heightProperty().bind(pane.heightProperty());
                                solutionDisplayer.heightProperty().bind(pane.heightProperty());

                        }
                });

        }
        public void zoomInOut(ScrollEvent event){
               mazeDisplayer.zoomInOut(event);
               characterDisplayer.zoomInOut(event);
               solutionDisplayer.zoomInOut(event);
        }


        public void About(ActionEvent actionEvent) {
                try {
                        Stage stage = new Stage();
                        stage.setTitle("AboutController");
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                        stage.show();
                } catch (Exception e) {

                }
        }

        public void help(ActionEvent actionEvent){
                try {
                        Stage stage = new Stage();
                        stage.setTitle("HelpController");
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                        stage.show();
                } catch (Exception e) {

                }
        }

        public void newOption(){
                mazeDisplayer.clear();
                characterDisplayer.clear();
                solutionDisplayer.clear();
        }


        public void exitP(){
               viewModel.stopServers();
                Platform.exit();
        }

        public void setFocus(MouseEvent event){
                if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                        mazeDisplayer.requestFocus();
                }


        }

         public  void setStageCloseEvent(Stage stage) {
                stage.setOnCloseRequest(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK){
                                // ... user chose OK
                                // Close the program properly
                                exitP();

                        } else {
                                event.consume();
                        }
                });
        }


        public void saveGame(){
                if(viewModel.getMaze()==null){
                        showAlert("You haven't started playing yet");
                }
                else {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save Game");
                        File file = fileChooser.showSaveDialog(new Stage());
                        if (file != null) {
                                viewModel.saveMaze(file);
                        }
                }
        }

        public void loadGame(){
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null){
                     viewModel.loadMaze(file);
                     //solutionDisplayer.setVisible(false);
                     txtfld_rowsNum.setText(""+viewModel.getMaze().getRows());
                     txtfld_columnsNum.setText(""+viewModel.getMaze().getColumns());

                }


        }
        public void Properties(ActionEvent actionEvent) {
                try {
                        Stage stage = new Stage();
                        stage.setTitle("PropertiesController");
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                        stage.show();
                } catch (Exception e) {

                }
        }

        public  void chooseCharacter(ActionEvent event){
                                switch (choiceBox.getValue()) {
                                        case "Jon":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/jon.jpeg");
                                                displayCharacter(viewModel.getMaze());
                                                break;
                                        case "Daenerys":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/Daenerys.jpeg");
                                                displayCharacter(viewModel.getMaze());

                                                break;
                                        case "Arya":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/Arya.jpeg");
                                                displayCharacter(viewModel.getMaze());
                                                break;
                                        case "Drogo":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/Khal.jpeg");
                                                displayCharacter(viewModel.getMaze());

                                                break;
                                        case "Sansa":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/Sansa.jpeg");
                                                displayCharacter(viewModel.getMaze());
                                                break;
                                        case "Tyrion":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/Tyrion.jpeg");
                                                displayCharacter(viewModel.getMaze());

                                                break;
                                        case "Cersei":
                                                characterDisplayer.setImageFileNameCharacter("resources/images/Cersei.jpeg");
                                                displayCharacter(viewModel.getMaze());

                                                break;

                                }
                        }
        public  void chooseWall(ActionEvent event){
                switch (choiceBoxWall.getValue()) {
                        case "Fire":
                                mazeDisplayer.setImageFileNameWall("resources/images/Fire.jpeg");
                                displayCharacter(viewModel.getMaze());
                                displayMaze(viewModel.getMaze());
                                break;
                        case "Ice":
                                mazeDisplayer.setImageFileNameWall("resources/images/Ice.jpeg");
                                displayCharacter(viewModel.getMaze());
                                displayMaze(viewModel.getMaze());

                                break;
                        case "Wall":
                                mazeDisplayer.setImageFileNameWall("resources/images/wall.jpeg");
                                displayCharacter(viewModel.getMaze());
                                displayMaze(viewModel.getMaze());

                                break;

                }
        }
        public void dragMouse(MouseEvent mouseEvent){
                if(mazeDisplayer!=null){
                        double moveX = (mouseEvent.getX())/(mazeDisplayer.getWidth()/viewModel.getMaze().getColumns());
                        double moveY = (mouseEvent.getY())/(mazeDisplayer.getHeight()/viewModel.getMaze().getRows());

                        if(!won) {

                                if (moveX > viewModel.getCharacterPositionColumn() + 1) {
                                        viewModel.moveCharacterCode(KeyCode.NUMPAD6);
                                } else if (moveX < viewModel.getCharacterPositionColumn() - 1) {
                                        viewModel.moveCharacterCode(KeyCode.NUMPAD4);
                                } else if (moveY > viewModel.getCharacterPositionRow() + 1) {
                                        viewModel.moveCharacterCode(KeyCode.NUMPAD2);
                                } else if (moveY < viewModel.getCharacterPositionRow() - 1) {
                                        viewModel.moveCharacterCode(KeyCode.NUMPAD8);
                                }
                        }


                }
        }
}

