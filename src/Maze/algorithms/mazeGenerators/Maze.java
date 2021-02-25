package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Maze implements Serializable {
    private int rows;
    private int columns;
    private Position start;
    private  Position goal;
    private int [][] maze;

    /**constractor*/
    public Maze(int rows , int columns) {
        if(rows>0 && columns>0) {
            this.maze = new int[rows][columns];
            this.rows = rows;
            this.columns = columns;
        }
        else{ // difault size in case the input is not valid
            this.rows=10;
            this.columns=10;
            this.maze=new int [this.rows][this.columns];
        }
        for(int i=0;i<this.rows ; i++){
            for (int j=0;j<this.columns;j++){
                maze[i][j] =0;
            }
        }
        start = new Position(0,0);
        goal = new Position(this.rows,this.columns);
    }
    public Maze(byte[] byteMaze) {
        rows = ((byteMaze[0]*256) + checkByte(byteMaze[1]));
        columns = ((byteMaze[2]*256) + checkByte(byteMaze[3]));
       int startR = ((byteMaze[byteMaze.length-8]*256)+ checkByte(byteMaze[byteMaze.length-7]));
       int startC = (byteMaze[byteMaze.length-6]*256)+ (checkByte(byteMaze[byteMaze.length-5]));
       start = new Position(startR,startC);
        int goalR = (byteMaze[byteMaze.length-4]*256)+ (checkByte(byteMaze[byteMaze.length-3]));
        int goalC = (byteMaze[byteMaze.length-2]*256)+ (checkByte(byteMaze[byteMaze.length-1]));
        goal = new Position(goalR,goalC);
        int i=4;
        maze = new int[rows][columns];
        for(int r=0;r<rows;r++){
            for(int c=0; c<columns; c++){
                if(byteMaze[i] == 1) {
                    maze[r][c] = 1;
                    i++;
                }else{
                    maze[r][c] = 0;
                    i++;
                }
            }
        }

    }
    private int checkByte(int num){
        if(num < 0 ){
            return (256+num);
        }else{
            return num;
        }
    }
        public Position getStartPosition(){
        return  start;
    }

    public Position getGoalPosition(){
        return goal;
    }
    public void setValue(int row,int column,int value){
        maze[row][column]=value;
    }

    /**find the first cube contains zero*/
    private Position findStart(){
        for(int i=0;i<rows;i++){ //find the first cube contain zero
            for(int j=0;j<columns;j++) {
                if (maze[i][j] == 0) {
                    start.setPosition(i, j);
                    return start;
                }
            }
        }
        return null;
    }

    /**turning walls into a valid path*/
    public void brakeWalls (){
        start = findStart();
        int r = start.getRowIndex();
        int c = start.getColumnIndex();
        while(r != rows-1 && c != columns-1){
            if(maze[r][c+1] == 0){
                c++;
            }else if(maze[r+1][c] == 0){
                r++;
            }else{
                maze[r][c+1] = 0;
                c++;
            }
        }
        goal.setPosition(r,c);

    }


    public void print(){
        for(int i=0;i<rows;i++) {
            System.out.print("{");
            for(int j=0;j<columns;j++){
                if(i == start.getRowIndex() && j== start.getColumnIndex()){
                    System.out.print("S"+" ,");
                }
                else if(i == goal.getRowIndex() && j== goal.getColumnIndex()){
                    System.out.print("E"+" ,");

                }else {
                    System.out.print(maze[i][j] + " ,");
                }
            }
            System.out.print("} \n");

        }
    }

    /**puts wall in every cube*/
    public void fullMaze(){
        for(int i=0;i<rows;i++){
            for(int j=0; j < columns; j++){
                maze[i][j] = 1;
            }
        }
    }

    /** creating a maze according to DFS algorithm*/
    public void DFS(){
        int rowS = (int) (Math.random() * rows); //random start point
        int columnS = (int) (Math.random() * columns);
        start.setPosition(rowS, columnS);//set start point
        maze[rowS][columnS] = 0;
        Stack<Position> path = new Stack<>();
        Position last = start;
        path.push(last);
        while (!path.empty()){
            int row = last.getRowIndex();
            int col = last.getColumnIndex();
            int[] shuffle = shuffle();
            boolean find = false;
            for(int i=0; i<shuffle.length && !find; i++) {
                switch (shuffle[i]) {
                    case 1: {//up
                        last =  moveFunc(row-1,col,row-2,col,path);
                        if(last != null)
                            find = true;
                        break;
                    }
                    case 2: {//right
                        last = moveFunc(row,col+1,row,col+2,path);
                        if(last != null)
                            find = true;
                        break;
                    }
                    case 3: {//down
                        last = moveFunc(row+1,col,row+2,col,path);
                        if(last != null)
                            find = true;
                        break;
                    }
                    case 4: {//down
                        last = moveFunc(row,col-1,row,col-2,path);
                        if(last != null)
                            find = true;
                        break;
                    }
                }
            }
            if(!find){
                last = path.pop();
            }
        }
    }

    /**check valid moves*/
    private Position moveFunc(int row1,int col1,int row2,int col2, Stack path){
        if (checkCells(row2, col2) && checkCells(row1, col1)) {
            maze[row1][col1] = 0;
            maze[row2][col2] = 0;
            goal.setPosition(row2, col2);
            Position temp = new Position(row2, col2);
            path.push(temp);
            return temp;
        }
        return null;
    }

    /** choose a random direction*/
    private int[] shuffle (){
        ArrayList<Integer> shuffleArr =new ArrayList<>();
        for(int i=0;i<4;i++){
            shuffleArr.add(i+1);
        }
        Collections.shuffle(shuffleArr);
        int[] array = new int[4];
        for(int i=0;i<4;i++){
            array[i]=shuffleArr.get(i);
        }
        return array;
    }

    /**check if there is a wall in a specific cube*/
    public boolean checkCells(int row,int column){
        if(row < rows && column < columns && row >= 0 && column>=0){
            if(maze[row][column] == 1){
                return true;
            }
        }

        return false;
    }

    /**check if there is a path in a specific cube*/
    public boolean checkAdj(int row, int column) {
        if (row < rows && column < columns && row >= 0 && column >= 0) {
            if (maze[row][column] == 0) {
                return true;
            }
        }
        return  false;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public byte[] toByteArray(){

        byte[] byteMaze = new byte[(rows*columns+12)];
        byteMaze[0] = (byte)(rows/256);
        byteMaze[1] = (byte)(rows%256);
        byteMaze[2] = (byte)(columns/256);
        byteMaze[3] = (byte)(columns%256);
        int i=4;
        for (int r = 0; r < rows; r++) {
            for(int c=0;c<columns;c++) {
                        if (maze[r][c] == 1) {
                            byteMaze[i] = 1;
                            i++;
                        } else {
                            byteMaze[i] = 0;
                            i++;
                        }

                    }
                }

        byteMaze[byteMaze.length-8] = (byte)(start.getRowIndex()/256);
        byteMaze[byteMaze.length-7] = (byte)(start.getRowIndex()%256);
        byteMaze[byteMaze.length-6] = (byte)(start.getColumnIndex()/256);
        byteMaze[byteMaze.length-5] = (byte)(start.getColumnIndex()%256);
        byteMaze[byteMaze.length-4] = (byte)(goal.getRowIndex()/256);
        byteMaze[byteMaze.length-3] = (byte)(goal.getRowIndex()%256);
        byteMaze[byteMaze.length-2] = (byte)(goal.getColumnIndex()/256);
        byteMaze[byteMaze.length-1] = (byte)(goal.getColumnIndex()%256);

        return byteMaze;
    }


}
