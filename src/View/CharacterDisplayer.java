package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CharacterDisplayer extends Canvas {
    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private Double zoom = 1.1;
    GraphicsContext gc = getGraphicsContext2D();





    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();

    }
    public void zoomInOut (ScrollEvent event){
        if(event.isControlDown()) {
            if (event.getDeltaY() < 0) {
                setHeight(getHeight() / zoom);
                setWidth(getWidth() / zoom);
            }
            if (event.getDeltaY() > 0) {
                setHeight(getHeight() * zoom);
                setWidth(getWidth() * zoom);
            }
        }
    }

    public void redraw() {

        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / maze.getRows();
        double cellWidth = canvasWidth / maze.getColumns();
        Image characterImage = null;
        try {
            gc.clearRect(0,0,getWidth(),getHeight());
            characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();


    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);

    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        widthProperty().addListener(e -> redraw());
        heightProperty().addListener(e -> redraw());
    }
    public void clear(){
        GraphicsContext gc = getGraphicsContext2D() ;
        gc.clearRect(0,0,getWidth(),getHeight());

    }
}
