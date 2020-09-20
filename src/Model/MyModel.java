package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

public class MyModel extends Observable implements  IModel {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private ArrayList<AState> solutionPath;
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();

    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();

    }


    @Override
    public void generateMaze(int width, int height) {
        buildMaze(width,height);

        setChanged();
        notifyObservers(2);
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    private Maze buildMaze(int width, int height) {
        // maze = mazeGenerator.generate(width,height);
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{width, height};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[((width*height)+12)];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        solutionPath = null;
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

        return maze;

    }



    @Override
    public void moveCharacter(KeyEvent movement) {
        switch (movement.getText()) {
            case "8":
                int row = characterPositionRow;
                row--;
                if (maze.checkAdj(row, characterPositionColumn)) {
                    characterPositionRow--;
                }
                break;

            case "2":
                row = characterPositionRow;
                row++;
                if (maze.checkAdj(row, characterPositionColumn)) {
                    characterPositionRow++;
                }

                break;

            case "6":
                int col = characterPositionColumn;
                col++;
                if (maze.checkAdj(characterPositionRow, col)) {
                    characterPositionColumn++;
                }

                break;

            case "4":
                col = characterPositionColumn;
                col--;
                if (maze.checkAdj(characterPositionRow, col)) {
                    characterPositionColumn--;
                }
                break;
            case "1":
                col = characterPositionColumn;
                row = characterPositionRow;
                col--;
                row++;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn--;
                    characterPositionRow++;
                }
                break;
            case "3":
                col = characterPositionColumn;
                row = characterPositionRow;
                col++;
                row++;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn++;
                    characterPositionRow++;
                }
                break;
            case "7":
                col = characterPositionColumn;
                row = characterPositionRow;
                col--;
                row--;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn--;
                    characterPositionRow--;
                }
                break;
            case "9":
                col = characterPositionColumn;
                row = characterPositionRow;
                col++;
                row--;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn++;
                    characterPositionRow--;
                }
                break;

        }
        setChanged();
        notifyObservers(2);
    }

    @Override
    public void moveCharacterCode(KeyCode movement) {
        switch (movement) {
            case NUMPAD8:
                int row = characterPositionRow;
                row--;
                if (maze.checkAdj(row, characterPositionColumn)) {
                    characterPositionRow--;
                }

                break;

            case NUMPAD2:
                row = characterPositionRow;
                row++;
                if (maze.checkAdj(row, characterPositionColumn)) {
                    characterPositionRow++;
                }
                break;

            case NUMPAD6:
                int col = characterPositionColumn;
                col++;
                if (maze.checkAdj(characterPositionRow, col)) {
                    characterPositionColumn++;
                }
                break;
            case NUMPAD4:
                col = characterPositionColumn;
                col--;
                if (maze.checkAdj(characterPositionRow, col)) {
                    characterPositionColumn--;
                }
                break;
            case NUMPAD1:
                col = characterPositionColumn;
                col--;
                row = characterPositionRow;
                row++;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn--;
                    characterPositionRow++;
                }
                break;
            case NUMPAD3:
                col = characterPositionColumn;
                col++;
                row = characterPositionRow;
                row++;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn++;
                    characterPositionRow++;
                }
                break;
            case NUMPAD7:
                col = characterPositionColumn;
                col--;
                row = characterPositionRow;
                row--;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn--;
                    characterPositionRow--;
                }
                break;
            case NUMPAD9:
                col = characterPositionColumn;
                col++;
                row = characterPositionRow;
                row--;
                if (maze.checkAdj(row, col)) {
                    characterPositionColumn++;
                    characterPositionRow--;
                }
                break;

        }
        setChanged();
        notifyObservers(2);
    }


    public void solveMaze(){
        try {
            solutionPath = null;
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        solutionPath = mazeSolution.getSolutionPath();


                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

        setChanged();
        notifyObservers("1");

    }

    public void saveMaze(File file){
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            ObjectOutputStream object = new ObjectOutputStream(fileOutputStream);
            byte[] bytes = new byte[maze.toByteArray().length+4];
            byte[] mazeB = maze.toByteArray();
            for(int i=0; i < mazeB.length ;i++){
                bytes[i] = mazeB[i];
            }
            bytes[bytes.length-4] = (byte)(characterPositionRow/256);
            bytes[bytes.length-3] = (byte)(characterPositionRow%256);
            bytes[bytes.length-2] = (byte)(characterPositionColumn/256);
            bytes[bytes.length-1] = (byte)(characterPositionColumn%256);

            object.writeObject(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMaze(File file){
        try {
            InputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream object = new ObjectInputStream(fileInputStream);
            byte[] bytes =(byte[]) object.readObject();
            byte[] byteMaze = new byte[bytes.length-4];
            for(int i=0; i < byteMaze.length ;i++){
                byteMaze[i] = bytes[i];
            }
            characterPositionRow =(bytes[bytes.length-4]*256 + bytes[bytes.length-3]);

            characterPositionColumn = (bytes[bytes.length-2]*256 + bytes[bytes.length-1]);

            Maze m = new Maze(byteMaze);
            maze = m;

            setChanged();
            notifyObservers(3);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void move(){

        setChanged();
        notifyObservers(2);

    }

    public Maze getMove(){return this.maze;}

    public ArrayList<AState> getSolution(){
        return solutionPath;
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
}

