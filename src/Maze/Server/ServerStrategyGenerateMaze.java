package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void ServerStrategy(InputStream input, OutputStream output) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(input);
            ObjectOutputStream toClient = new ObjectOutputStream(output);
            toClient.flush();
           // MyCompressorOutputStream out = new MyCompressorOutputStream(output);

            int [] size  = (int[])(fromClient.readObject());
            IMazeGenerator mg = Configurations.setGenerate();
            Maze maze = mg.generate(size[0], size[1]);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            MyCompressorOutputStream out = new MyCompressorOutputStream(byteArray);
            out.write(maze.toByteArray());
            byte[] ans = byteArray.toByteArray();

            toClient.writeObject(ans);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
