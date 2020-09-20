package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SolutionDisplayer extends Canvas {

    private ArrayList<AState> solutionPath;
    private Maze maze;
    private Double zoom = 1.1;


    public SolutionDisplayer() {
        widthProperty().addListener(e->redraw());
        heightProperty().addListener(e->redraw());
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }
    public  void setSolution(ArrayList<AState>solutionPath ){
        this.solutionPath=solutionPath;
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

    public void redraw(){
        if(solutionPath!=null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getRows();
            double cellWidth = canvasWidth / maze.getColumns();

            Image solImage=null;
            Image endImage = null;
            try {
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                solImage = new Image(new FileInputStream(ImageFileSolvePath.get()));
                endImage = new Image(new FileInputStream(ImageFileEnd.get()));

                for(int i=0; i<solutionPath.size();i++){
                    MazeState state=(MazeState)solutionPath.get(i);
                    int row=state.getState().getRowIndex();
                    int col=state.getState().getColumnIndex();
                    gc.drawImage(solImage, col * cellWidth, row * cellHeight,cellWidth, cellHeight);
                }
                gc.drawImage(endImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex()* cellHeight, cellWidth, cellHeight);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        }
    }


    private StringProperty ImageFileEnd = new SimpleStringProperty();
    private StringProperty ImageFileSolvePath = new SimpleStringProperty();


    public String getImageFileSolvePath() {
        return ImageFileSolvePath.get();
    }

    public void setImageFileSolvePath(String imageFileNameWall) {
        this.ImageFileSolvePath.set(imageFileNameWall);
    }


    public void clear(){
        GraphicsContext gc = getGraphicsContext2D() ;
        gc.clearRect(0,0,getWidth(),getHeight());


    }
    public String getImageFileEnd() {
        return ImageFileEnd.get();
    }

    public void setImageFileEnd(String imageFileNameEnd) {
        this.ImageFileEnd.set(imageFileNameEnd);
    }

}
