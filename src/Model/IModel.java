package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.ArrayList;

public interface IModel {

    void generateMaze(int width, int height);
    Maze getMaze();
    void moveCharacter(KeyEvent movement);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    void solveMaze();
    ArrayList<AState> getSolution();
     void move();
     Maze getMove();
     void stopServers();
    void saveMaze(File file);
     void loadMaze(File file);
     void moveCharacterCode(KeyCode movement);





        // void moveCharacter (int characterPositionRow,int characterPositionColumn);




    }
