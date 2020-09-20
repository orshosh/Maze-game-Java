package ViewModel;

import algorithms.mazeGenerators.Maze;
import Model.IModel;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            setChanged();
            notifyObservers(arg);
        }

    }

    public void generateMaze(int width, int height) {
        model.generateMaze(width, height);
    }


    public Maze getMove(){return model.getMove();}

    public void moveCharacter(KeyEvent movement) {
        model.moveCharacter(movement);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public ArrayList<AState> getSolution() {
        return model.getSolution();

    }

    public void solveMaze(){
        model.solveMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public void stopServers(){
        model.stopServers();
    }
    public void saveMaze(File file){
        model.saveMaze(file);
    }

    public void loadMaze (File file){
        model.loadMaze(file);
    }
    public void moveCharacterCode(KeyCode movement){
        model.moveCharacterCode(movement);
    }


}
