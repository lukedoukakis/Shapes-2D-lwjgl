/***************************************************************
* file: Program1.java
* author: Luke Doukakis
* class: CS 4450 – Computer Graphics
*
* assignment: program 1
* date last modified: 2/9/2020
*
* NOTE: takes coordinates.txt from src folder ("src/coordinates.txt")
*
****************************************************************/ 


package program1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author Luke
 */
public class Program1 {

    /**
     * @param args the command line arguments
     */
    
    
    
    public static void main(String[] args) {
        
        Program1 main = new Program1();
        main.start();
        
        /*
        Scanner s;
        File coords = new File("src/coordinates.txt");
        
        try{
            s = new Scanner(coords);
        }
        catch(FileNotFoundException e){
            System.out.println("File not found. Make sure coordinates.txt is in src.");
        }
        */ 
    }
    
    public void start(){
        
        try {
            
            createWindow();
            initGL();
            
            // render until user presses ESC
            while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                render();    
            }
            Display.destroy();  
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void createWindow() throws Exception {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Program 1");
        Display.create();
    }
    
    void initGL(){
        glClearColor(.0f, .0f, .0f, .0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 0, 480, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    void render(){
        
        try{

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            glPointSize(1);
            glBegin(GL_POINTS);
            
            // get file and call draw function
            File coords = new File("src/coordinates.txt");
            drawFromFile(coords);
              
            glEnd();
            Display.update();
            Display.sync(60);
        } catch(Exception e){
            e.printStackTrace();
        }   
    }
    
    // draw line using midpoint algorithm
    void drawLine(float arg_x1, float arg_y1, float arg_x2, float arg_y2){
        
        float x1;
        float x2;
        float y1;
        float y2;
        
        if(arg_x1 > arg_x2){
            x1 = arg_x2;
            y1 = arg_y2;
            x2 = arg_x1;
            y2 = arg_y1;
        }else{
            x1 = arg_x1;
            y1 = arg_y1;
            x2 = arg_x2;
            y2 = arg_y2;
        }

        float slope;
        if((x2-x1) == 0){
            slope = y2-y1;
        }else{
            slope = (y2-y1)/(x2-x1);
        }
        
        float dx;
        float dy;
        if(Math.abs(slope) < 1){
            dx = x2 - x1;
            dy = y2 - y1;
        }else{
            dy = x2 - x1;
            dx = y2 - y1;
        }
        if(dy < 0){
            dy = -dy;
        }
        
        float d = 2*dy - dx;
        float curX = x1;
        float curY = y1;
        
        while(curX < x2){
            
            glVertex2f(curX, curY);
            
            if(d > 0){
                if(Math.abs(slope) < 1){
                    if(slope > 0){
                        curY += 1f;
                    }
                    if(slope < 0){
                        curY -= 1f;
                    }
                }else{
                    curX += 1f;
                }
                d = d - 2*dx;
            }
            d = d + 2*dy;
            if(Math.abs(slope) < 1){
                curX += 1f;
            }else{
                if(slope > 0){
                    curY += 1f;
                }
                if(slope < 0){
                    curY -= 1f;
                }
            }
        }
    }
    
    
    // draw circle by plotting points and drawing lines between them
    void drawCircle(float centerX, float centerY, float radius){
        
        float theta = 0f;
        float pi = (float)Math.PI;
        
        /*
        while(theta < 2f*pi){
            glVertex2f(radius*(float)Math.cos(theta)+centerX, radius*(float)Math.sin(theta)+centerY);
            theta += (2f*pi)/(radius*7);
        }
        */
        float x1,y1,x2,y2;
        
        while(theta < 2f*pi){
           x1 = radius*(float)Math.cos(theta)+centerX;
           y1 = radius*(float)Math.sin(theta)+centerY;
           theta += (2f*pi)/(radius*1.5f);
           x2 = radius*(float)Math.cos(theta)+centerX;
           y2 = radius*(float)Math.sin(theta)+centerY;
           drawLine(x1,y1,x2,y2);
        }

    }
    
    // draw ellipse by plotting points and drawing lines between them
    void drawEllipse(float centerX, float centerY, float radiusX, float radiusY){
        
        float theta = 0f;
        float pi = (float)Math.PI;
        
         float x1,y1,x2,y2;
        
        /*
        while(theta < 2f*pi){
            glVertex2f(radiusX*(float)Math.cos(theta)+centerX, radiusY*(float)Math.sin(theta)+centerY);
            theta += (2f*pi)/(Math.max(radiusX, radiusY)*7);
        }
        */
         
        while(theta < 2f*pi){
            x1 = radiusX*(float)Math.cos(theta)+centerX;
            y1 = radiusY*(float)Math.sin(theta)+centerY;
            theta += (2f*pi)/(Math.max(radiusX, radiusY)*1.5f);
            x2 = radiusX*(float)Math.cos(theta)+centerX;
            y2 = radiusY*(float)Math.sin(theta)+centerY;
            drawLine(x1,y1,x2,y2);
        }
    }
    
    // draw shapes from text file
    void drawFromFile(File file){
        
        float[] lineParams = new float[4];
        float[] circleParams = new float[3];
        float[] ellipseParams = new float[4];
        
        Scanner s;
        try{
            
            s = new Scanner(file);
            String line;
            String[] components;
            
            while(s.hasNextLine()){
                
                line = s.nextLine();
                components = line.split(" ");
            
                switch (components [0]){
                    case "l" :
                        lineParams[0] = Float.parseFloat(components[1].split(",")[0]);
                        lineParams[1] = Float.parseFloat(components[1].split(",")[1]);
                        lineParams[2] = Float.parseFloat(components[2].split(",")[0]);
                        lineParams[3] = Float.parseFloat(components[2].split(",")[1]);
                        glColor3f(255f, 0f, 0f);
                        drawLine(lineParams[0], lineParams[1], lineParams[2], lineParams[3]);
                        break;
                    case "c" :
                        circleParams[0] = Float.parseFloat(components[1].split(",")[0]);
                        circleParams[1] = Float.parseFloat(components[1].split(",")[1]);
                        circleParams[2] = Float.parseFloat(components[2]);
                        glColor3f(0f, 0f, 255f);
                        drawCircle(circleParams[0], circleParams[1], circleParams[2]);
                        break;
                    case "e" :
                        ellipseParams[0] = Float.parseFloat(components[1].split(",")[0]);
                        ellipseParams[1] = Float.parseFloat(components[1].split(",")[1]);
                        ellipseParams[2] = Float.parseFloat(components[2].split(",")[0]);
                        ellipseParams[3] = Float.parseFloat(components[2].split(",")[1]);
                        glColor3f(0f, 255f, 0f);
                        drawEllipse(ellipseParams[0], ellipseParams[1], ellipseParams[2], ellipseParams[3]);
                        break;
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}