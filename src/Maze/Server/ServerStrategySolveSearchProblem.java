package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.BestFirstSearch;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;




import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private  static int counter=0;

    @Override
    public void ServerStrategy(InputStream input, OutputStream output) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(input);
            ObjectOutputStream toClient = new ObjectOutputStream(output);
            toClient.flush();

            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            Maze maze = (Maze)fromClient.readObject();
            byte[] mazeByte = maze.toByteArray();
            int nameMaze = existMaze(mazeByte);

            File size = new File(tempDirectoryPath );
            File[] listOfAllFiles = size.listFiles();
            File allMazes=null;
            for(File file : listOfAllFiles){ // looking for old mazes
                if(file.getName().contains(".counter")){
                    allMazes=file;
                    FileInputStream fileSize = new FileInputStream(allMazes);
                    ObjectInputStream ob = new ObjectInputStream(fileSize);
                    counter=(int)ob.readObject();
                    break;
                }
            }


            if( nameMaze!=-1){ // exist maze
                File folder = new File(tempDirectoryPath );
                File[] listOfFiles = folder.listFiles();
                String str = nameMaze+".Sol";
                File sol;
                for(File file : listOfFiles){ // all the mazes
                    if(file.getName().contains(str)){
                        sol = file;
                        FileInputStream fileIn = new FileInputStream(sol);
                        ObjectInputStream obj = new ObjectInputStream(fileIn);
                        Solution ans = (Solution)obj.readObject();
                        toClient.writeObject(ans);
                        break;
                    }
                }



            }
            else{ // need to solve the maze

                FileOutputStream fileMaze = new FileOutputStream(tempDirectoryPath+counter+".maze");
                fileMaze.write(maze.toByteArray());
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                ISearchingAlgorithm algorithm = new BestFirstSearch();
                Solution solution = algorithm.solve(searchableMaze);
                FileOutputStream fileSol = new FileOutputStream(tempDirectoryPath+counter+".Sol");
                ObjectOutputStream fileOb = new ObjectOutputStream(fileSol);
                fileOb.writeObject(solution);
                toClient.writeObject(solution);
                if(counter==0){
                    FileOutputStream numOfMazes = new FileOutputStream(tempDirectoryPath+".counter");
                    ObjectOutputStream fileSize = new ObjectOutputStream(numOfMazes);
                    fileSize.writeObject(counter+1);
                    counter++;

                }
                else{
                    if(allMazes!=null) {
                        allMazes.delete();
                        FileOutputStream numOfMazes = new FileOutputStream(tempDirectoryPath + ".counter");
                        ObjectOutputStream fileSize = new ObjectOutputStream(numOfMazes);
                        fileSize.writeObject(counter + 1);
                        counter++;
                    }
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private int existMaze (byte [] maze){
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        File folder = new File(tempDirectoryPath );
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> newList = new ArrayList<>();

        for(File file : listOfFiles){ // all the mazes
            if(file.getName().contains(".maze")){
                newList.add(file);
            }
        }

        for (int i=0; i<newList.size();i++){
            try {
                FileInputStream file = new FileInputStream(newList.get(i));
                byte [] temp= new byte [maze.length];
                file.read(temp);
                if (Arrays.equals(temp,maze)){
                    String filename =  newList.get(i).getName();
                    String[] parts = filename.split("\\."); // String array, each element is text between dots
                    String beforeFirstDot = parts[0];
                    return Integer.parseInt(beforeFirstDot);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;

    }
}
