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

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Double zoom = 1.1;

    public MazeDisplayer() {
        widthProperty().addListener(e->redraw());
        heightProperty().addListener(e->redraw());
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

    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }

    public void redraw() {
        if (maze != null) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0,0,getWidth(),getHeight());
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getRows();
            double cellWidth = canvasWidth / maze.getColumns();

            Image wallImage = null;
            Image endImage = null;
            try {
                wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                endImage = new Image(new FileInputStream(ImageFileEnd.get()));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //Draw Maze
            for (int i = 0; i < maze.getRows(); i++) {
                for (int j = 0; j < maze.getColumns(); j++) {
                    if (maze.checkCells(i,j)) {
                        gc.drawImage(wallImage, j * cellWidth, i * cellHeight,cellWidth, cellHeight);
                    }

                }
            }

             gc.drawImage(endImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex()* cellHeight, cellWidth, cellHeight);

        }
    }

    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileEnd = new SimpleStringProperty();


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileEnd() {
        return ImageFileEnd.get();
    }

    public void setImageFileEnd(String imageFileNameEnd) {

        this.ImageFileEnd.set(imageFileNameEnd);
    }

    public boolean checkWin(int row, int col){
        if(row==maze.getGoalPosition().getRowIndex() && col==maze.getGoalPosition().getColumnIndex()){
            return true;
        }
        return false;
    }
    public void clear(){
        GraphicsContext gc = getGraphicsContext2D() ;
        gc.clearRect(0,0,getWidth(),getHeight());

    }
}
