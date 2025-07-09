/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konkurens;

import java.util.Random;

/**
 *
 * @author Kristóf
 */
public class Dog implements Runnable{
    private int x;
    private int y;
    private final Farm farm;
    private final int delay;
    
    public Dog(Farm farm,int delay,int x, int y){
        this.farm=farm;
        this.delay=delay;
        this.x=x;
        this.y=y;
    }
    
    
    
    public boolean noPossibleMoves(){
        //boolean res=false;
        for(int i=-1;i<2;i++)
        {
            for(int j=-1;j<2;j++)
            {
                if (farm.grid[x+i][y+j] instanceof Empty)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    
     public void move (){
        Random random = new Random(); 
        boolean moved = false;
        farm.grid[x][y]=new Empty();
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
            if(newX>=0 && newY>=0 && newY <farm.width && newX <farm.height && !farm.isMiddleRectangle(newX, newY) && farm.grid[newX][newY] instanceof Empty)
            {
                moved=true;
            }
        }
        farm.grid[newX][newY]=this;
        x=newX;
        y=newY;
     }

    
    
    
    @Override
    public void run() {
         try 
         {
            while (true)
            {
                Thread.sleep(delay);

                
                synchronized (farm) 
                {
                    move();
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
   
    
    @Override
    public String toString(){
        return "D";
    }
    
}
