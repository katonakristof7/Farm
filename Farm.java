/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konkurens;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Kristóf
 */
public class Farm {
    public final int width;
    public final int height;
    public Object[][] grid;
    public ArrayList<Runnable> animals = new ArrayList<>();
    public volatile boolean  simulationOver = false;
    private final int delay;
    ArrayList<Thread> threads = new ArrayList<>();
    
    
    
    public Farm(int width, int height, int sheepCount, int dogCount,int delay) {
        this.width = width;
        this.height=height;
        this.grid = new Object[height][width];
        this.delay=delay;
        
        initFarm();
        addGates();
        //System.out.println("initFarm");
        addSheep(sheepCount);
        //System.out.println("addSheep");
        addDogs(dogCount);
        //System.out.println("addDogs");
        startSimulation();
    }
    
     private void initFarm() {
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1)
                {
                    grid[i][j] = new Wall();
                } 
                else
                {
                    grid[i][j] = new Empty();
                }
            }
        }
    }
     
     private void addGates(){
         Random random = new Random();
         int randomnum =  random.nextInt(12) + 1;
         grid[0][randomnum] = new Gate();
         randomnum =  random.nextInt(12) + 1;
         grid[13][randomnum] = new Gate();
         randomnum =  random.nextInt(12) + 1;
         grid[randomnum][0] = new Gate();
         randomnum =  random.nextInt(12) + 1;
         grid[randomnum][13] = new Gate();
     }
     
     
     private void addSheep(int sheepnum){
        Random random = new Random();
        int i=0;
        while (i < sheepnum)
        {
            //x=random.nextInt(high-low) + low;
            int x=random.nextInt((1 + 2 * (height - 2) / 3 - 1)+1-(1 + (height - 2) / 3)) + (1 + (height - 2) / 3);
            int y=random.nextInt((1 + 2 * (width - 2) / 3 - 1)+1-(1 + (width - 2) / 3)) + (1 + (width - 2) / 3);
            if(grid[x][y].toString().equals(" "))
            {
                //System.out.println("belepett");
                grid[x][y]=new Sheep(this,delay,x,y);
                animals.add((Runnable)grid[x][y]);
                i++;
            }
        }
     }
     
     public boolean isMiddleRectangle(int x, int y){
         int middleRowEnd=(1 + 2 * (height - 2) / 3 - 1);
         int middleRowStart=(1 + (height - 2) / 3);
         
         int middleColEnd=(1 + 2 * (width - 2) / 3 - 1);
         int middleColStart=(1 + (width - 2) / 3);
         
         
         if((x>middleRowEnd || x<middleRowStart) ||(y>middleColEnd || y<middleColStart))
         {
             return false;
         }
         return true;
     }
     
     
     private void addDogs(int dognum){
         Random random = new Random();
         int i=0;
         
         while(i<dognum)
         {
             //x=random.nextInt(high-low) + low;
             int x=random.nextInt((height-2)-1)+1;
             int y=random.nextInt((width-2)-1)+1;
             if(grid[x][y].toString().equals(" ") && !isMiddleRectangle(x,y))
             {
                 grid[x][y]=new Dog(this,delay,x,y);
                 animals.add((Runnable)grid[x][y]);
                 i++;
             }
         }
     }
     
    public void printFarm(){
        System.out.println("\u001B[0;0H");
        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                System.out.print(grid[i][j].toString());
            }
             System.out.println();
        }
    }
    
    public void startSimulation(){
        System.out.println("\033[H\033[2J");
        for (Runnable animal : animals)
        {
            Thread thread = new Thread(animal);
            thread.start();
            threads.add(thread);
        }
        while (!simulationOver)
        {
            synchronized (this)
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                printFarm();
            }
            try 
            {
                Thread.sleep(delay);
            } 
            catch (InterruptedException e) 
            {
                Thread.currentThread().interrupt();
            }
        }
        
            for (Thread thread : threads) {
                thread.interrupt();  
        }
        
    }
    
    
    
    public synchronized void stopSimulation() {
        if (!simulationOver) {
        //System.out.println("Simulation has ended.");
        simulationOver = true;
    }
    }
    
    public synchronized boolean isSimulationOver(){
        return simulationOver;
    }
}
