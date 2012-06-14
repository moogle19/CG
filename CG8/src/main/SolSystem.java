/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import static opengl.GL.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import util.Camera;
import util.Geometry;
import util.GeometryFactory;
import util.Util;

/**
 *
 * @author Sascha Kolodzey, Nico Marniok
 */
public class SolSystem {
    // shader programs
    private static int shaderProgram;
    
    // geometries
    private static Geometry earth = null;
    private static Geometry moon = null;
    private static int earthFineness = 0;

    // current configurations
    private static boolean bContinue = true;
    private static boolean culling = true;
    private static boolean wireframe = true;
    
    // control
    private static final Vector3f moveDir = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Camera cam = new Camera(); 
    
    // animation params
    private static float ingameTime = 0;
    private static float ingameTimePerSecond = 1.0f;
    
    // uniform locations
    private static int modelLoc;
    private static int viewProjLoc;
    
    // uniform data
    private static final Matrix4f earthModelMatrix = new Matrix4f();
    private static final Matrix4f moonModelMatrix = new Matrix4f();
    private static final Matrix4f viewProjMatrix = new Matrix4f();
    
    // temp data
    private static final Matrix4f moonRotation = new Matrix4f();
    private static final Matrix4f moonTilt = new Matrix4f();
    private static final Matrix4f moonTranslation = new Matrix4f();
    
    public static void main(String[] argv) {
        try {
            init();
            
            glEnable(GL_CULL_FACE);
            glFrontFace(GL_CCW);
            glCullFace(GL_BACK);
            glEnable(GL_DEPTH_TEST);
            
            shaderProgram = Util.createShaderProgram("./shader/Main_VS.glsl", "./shader/VertexColor_FS.glsl");

            cam.move(-5.0f, 0.0f, 0.0f);
            changeFineness(32);
            
            Util.translationX(5.0f, moonTranslation);
            Util.rotationX((float)Math.toRadians(15.0), moonTilt);
                        
            render();
            destroy();
        } catch (LWJGLException ex) {
            Logger.getLogger(SolSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void render() throws LWJGLException {
        glClearColor(0.1f, 0.0f, 0.0f, 1.0f); // background color: dark red
        
        long last = System.currentTimeMillis();
        long now, millis;
        long frameTimeDelta = 0;
        int frames = 0;
        while(bContinue && !Display.isCloseRequested()) {
            // time handling
            now = System.currentTimeMillis();
            millis = now - last;
            last = now;     
            frameTimeDelta += millis;
            ++frames;
            if(frameTimeDelta > 1000) {
                System.out.println(1e3f * (float)frames / (float)frameTimeDelta + " FPS");
                frameTimeDelta -= 1000;
                frames = 0;
            }
            
            // input and animation
            handleInput(millis);
            animate(millis);
            
            // clear screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            setActiveProgram(shaderProgram);
                        
            // earth
            matrix2uniform(earthModelMatrix, modelLoc);
            earth.draw();
                                    
            // moon
            matrix2uniform(moonModelMatrix, modelLoc);
            moon.draw();

            // present screen
            Display.update();
            Display.sync(60);
        }
    }
    
    /**
     * Behandelt Input und setzt die Kamera entsprechend.
     * @param millis Millisekunden seit dem letzten Aufruf
     */
    public static void handleInput(long millis) {
        float moveSpeed = 2e-3f*(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 2.0f : 1.0f)*(float)millis;
        float camSpeed = 5e-3f;
        
        while(Keyboard.next()) {
            if(Keyboard.getEventKeyState()) {
                switch(Keyboard.getEventKey()) {
                    case Keyboard.KEY_W: moveDir.z += 1.0f; break;
                    case Keyboard.KEY_S: moveDir.z -= 1.0f; break;
                    case Keyboard.KEY_A: moveDir.x += 1.0f; break;
                    case Keyboard.KEY_D: moveDir.x -= 1.0f; break;
                    case Keyboard.KEY_SPACE: moveDir.y += 1.0f; break;
                    case Keyboard.KEY_C: moveDir.y -= 1.0f; break;
                }
            } else {
                switch(Keyboard.getEventKey()) {
                    case Keyboard.KEY_W: moveDir.z -= 1.0f; break;
                    case Keyboard.KEY_S: moveDir.z += 1.0f; break;
                    case Keyboard.KEY_A: moveDir.x -= 1.0f; break;
                    case Keyboard.KEY_D: moveDir.x += 1.0f; break;
                    case Keyboard.KEY_SPACE: moveDir.y -= 1.0f; break;
                    case Keyboard.KEY_C: moveDir.y += 1.0f; break;
                    case Keyboard.KEY_F1: cam.changeProjection(); break;
                    case Keyboard.KEY_UP: changeFineness(2 * earthFineness); break;
                    case Keyboard.KEY_DOWN: changeFineness(earthFineness / 2); break;
                    case Keyboard.KEY_LEFT:
                        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                            ingameTimePerSecond = 0.0f;
                        } else {
                            ingameTimePerSecond = Math.max(1.0f / 64.0f, 0.5f * ingameTimePerSecond);
                        }
                        break;
                    case Keyboard.KEY_RIGHT:
                        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                            ingameTimePerSecond = 1.0f;
                        } else {
                            ingameTimePerSecond = Math.min(64.0f, 2.0f * ingameTimePerSecond);
                        }
                        break;
                    case Keyboard.KEY_F2: glPolygonMode(GL_FRONT_AND_BACK, (wireframe ^= true) ? GL_FILL : GL_LINE); break;
                    case Keyboard.KEY_F3: if(culling ^= true) glEnable(GL_CULL_FACE); else glDisable(GL_CULL_FACE); break;
                }
            }
        }
        
        cam.move(moveSpeed * moveDir.z, moveSpeed * moveDir.x, moveSpeed * moveDir.y);
        
        while(Mouse.next()) {
            if(Mouse.getEventButton() == 0) {
                Mouse.setGrabbed(Mouse.getEventButtonState());
            }
            if(Mouse.isGrabbed()) {
                cam.rotate(-camSpeed*Mouse.getEventDX(), -camSpeed*Mouse.getEventDY());
            }
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) bContinue = false;
        
        Matrix4f.mul(cam.getProjection(), cam.getView(), viewProjMatrix);        
    }
    
    /**
     * Hilfsmethode, um eine Matrix in eine Uniform zu schreiben. Das
     * zugehoerige Programmobjekt muss aktiv sein.
     * @param matrix Quellmatrix
     * @param uniform Ziellocation
     */
    private static void matrix2uniform(Matrix4f matrix, int uniform) {
        matrix.store(Util.MAT_BUFFER);
        Util.MAT_BUFFER.position(0);
        glUniformMatrix4(uniform, false, Util.MAT_BUFFER);
    }
    
    /**
     * Aktualisiert Model Matrizen der Erde und des Mondes.
     * @param millis Millisekunden, die seit dem letzten Aufruf vergangen sind.
     */
    private static void animate(long millis) {
        // update ingame time properly
        ingameTime += ingameTimePerSecond * 1e-3f * (float)millis;
        
        // earth
        float earthRotationAngle = Util.PI_MUL2 * ingameTime;
        Util.rotationY(earthRotationAngle, earthModelMatrix);
        
        // moon
        float moonRotationAngle = earthRotationAngle / 27.0f;
        Util.rotationY(moonRotationAngle, moonRotation);
        Util.mul(moonModelMatrix, moonTilt, moonRotation, moonTranslation);
    }
    
    /**
     * Aendert die Feinheit der Kugelannaeherung der Erde und des Mondes.
     * @param newFineness die neue Feinheit
     */
    private static void changeFineness(int newFineness) {
        if(newFineness >= 4 && newFineness <= 1024) {
            if(earth != null) {
                earth.delete();
            }
            if(moon != null) {
                moon.delete();
            }
            earth = GeometryFactory.createSphere(1.0f, newFineness, newFineness/2, "textures/earth.jpeg", "textures/earth_night.jpeg");
            moon = GeometryFactory.createSphere(0.5f, newFineness/2, newFineness/4, "textures/moon.jpeg", "textures/moon.jpeg");
            earthFineness = newFineness;
        }
    }
    
    /**
     * Aendert das aktuelle Programm und alle zugehoerigen Uniform locations.
     * @param program das neue aktuelle Programm
     */
    private static void setActiveProgram(int program) {
        glUseProgram(program);        
        modelLoc = glGetUniformLocation(program, "model");
        viewProjLoc = glGetUniformLocation(program, "viewProj");
        matrix2uniform(viewProjMatrix, viewProjLoc);      
    }
}
