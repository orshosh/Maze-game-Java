package Server;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurations {

    private static Properties prop = new Properties();
    private static InputStream ip;


    public static int setPool(){
        try {
            ip = Configurations.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(ip);
            int num = Integer.parseInt(prop.getProperty("pool"));
            return num;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


public static IMazeGenerator setGenerate(){
    try {
        ip = Configurations.class.getClassLoader().getResourceAsStream("config.properties");
        prop.load(ip);
        String generate = prop.getProperty("build");
        if(generate.equals("MyMazeGenerator")){
            return (new MyMazeGenerator());
        }
        if(generate.equals("EmptyMazeGenerator")){
            return (new EmptyMazeGenerator());
        }
        if(generate.equals("SimpleMazeGenerator")){
            return (new SimpleMazeGenerator());
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

    public static ISearchingAlgorithm setSearch(){
        try {
            ip = Configurations.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(ip);
            String generate = prop.getProperty("solve");
            if(generate.equals("BestFirstSearch")){
                return (new BestFirstSearch());
            }
            if(generate.equals("BreadthFirstSearch")){
                return (new BreadthFirstSearch());
            }
            if(generate.equals("DepthFirstSearch")){
                return (new DepthFirstSearch());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
