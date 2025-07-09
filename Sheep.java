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
public class Sheep implements Runnable {
    
    private int x;
    private int y;
    private final Farm farm;
    private final int delay;
    //boolean wasItGate = false;
    
    public Sheep(Farm farm,int delay,int x, int y){
        this.farm=farm;
        this.delay=delay;
        this.x=x;
        this.y=y;
    }
    
    
    @Override
    public String toString(){
        return "S";
    }
    
    
    
    
    public boolean noPossibleMoves(){
        //boolean res=false;
        for(int i=-1;i<2;i++)
        {
            for(int j=-1;j<2;j++)
            {
                 if (x + i >= 0 && x + i < farm.width && y + j >= 0 && y + j < farm.height &&
                    farm.grid[x + i][y + j] instanceof Empty)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void move (){
        
        
        
        boolean dogNearby = false;
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (i == 0 && j == 0) continue; 
                if (x + i >= 0 && x + i < farm.width && y + j >= 0 && y + j < farm.height &&
                    farm.grid[x+i][y+j] instanceof Dog)
                {
                    dogNearby=true;
                    if(farm.grid[x-i][y-j].toString().equals(" "))
                    {
                        farm.grid[x][y]=new Empty();
                        if(farm.grid[x-i][y-j] instanceof Gate)
                        {
                            //wasItGate = true;
                            farm.stopSimulation();
                        }
                        farm.grid[x-i][y-j]=this;
                        x=x-i;
                        y=y-j;
                    }
                }
            }
        }
        
        
        if(!dogNearby)
        {
            Random random = new Random();
            boolean moved = false;
            int newX=x;
            int newY=y;
            while(!moved)
            {
                if(noPossibleMoves())
                {
                    break;
                }
            newX = x + random.nextInt(3) - 1;
            newY = y + random.nextInt(3) - 1;
            if(newX>=0 && newY>=0 && newY <farm.width && newX <farm.height && farm.grid[newX][newY].toString().equals(" "))
            {
                moved=true;
            }
            }
            if(farm.grid[newX][newY] instanceof Gate)
            {
                //wasItGate = true;
                farm.stopSimulation();
            }
            farm.grid[x][y]=new Empty();
            farm.grid[newX][newY]=this;
            x=newX;
            y=newY; 
            //if(wasItGate)
            //{
                //System.out.println("Sheep has escaped!");
                //farm.stopSimulation();
                //Thread.currentThread().interrupt();
            //}
        }
         
    }
    
    
    
    @Override
    public void run() {
         try 
         {
            while (!farm.isSimulationOver())
            {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                Thread.sleep(delay);

                
                synchronized (farm) 
                {
                    if (farm.isSimulationOver()) {
                    break;
                    }

                    move();
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }
   
}
