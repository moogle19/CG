package opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;


/**
 * Veraenderungen erfolgen auf eigene Gefahr! Wenn Sie Probleme bekommen und
 * diese Datei nicht in ihrem Originalzustand ist, koennen wir Ihnen dabei nicht
 * helfen.
 * @author Sascha Kolodzey, Nico Marniok
 */
public class GL {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    private static boolean initialized = false;
    private static boolean checkForErrors = true;

    public static void init() throws LWJGLException {
        if(!GL.initialized) {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            boolean supported = true;
            try {
                Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
            } catch (LWJGLException e) {
                Display.create();
                supported = false;
            }
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
            Mouse.create();
            Keyboard.create();  
            
            String vendor = GL.glGetString(GL.GL_VENDOR);
            String version = GL.glGetString(GL.GL_VERSION);
            String renderer = GL.glGetString(GL.GL_RENDERER);
            String shadinglang = GL.glGetString(GL.GL_SHADING_LANGUAGE_VERSION);
            String driverversion = Display.getVersion();
            String os = System.getProperty("os.name") + " (" + System.getProperty("os.version") + "), " + System.getProperty("os.arch");
            String java = System.getProperty("java.vm.name") + ", runtime version: " + System.getProperty("java.runtime.version");

            String infoLines[] = {
                renderer + ", " + vendor + ", Driver: " + driverversion,
                "OpenGL " + version + " - Shading Language " + shadinglang,
                "Operating system: " + os,
                "Java: " + java
            };
            String info = GL.pack("OpenGL info", infoLines);
            System.out.println(info);          
            
            if(!supported) {
                throw new RuntimeException("Die Grafikkarte unterstuetzt, die von uns geforderte OpenGL Version 3.2 nicht. Bitte kopiere den Inhalt der Box 'OpenGL info' und schicke ihn an nmarniok@uos.de.");
            }
            
            GL.initialized = true;
        }
    }
    
    /**
     * GL15.GL_ARRAY_BUFFER
     */  
    public static final int GL_ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER;
    
    /**
     * GL11.GL_COLOR_BUFFER_BIT
     */
    public static final int GL_COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
    
    /**
     * GL11.GL_DEPTH_BUFFER_BIT
     */
    public static final int GL_DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
    
    /**
     * GL15.GL_ELEMENT_ARRAY_BUFFER
     */  
    public static final int GL_ELEMENT_ARRAY_BUFFER = GL15.GL_ELEMENT_ARRAY_BUFFER;  
    
    /**
     * GL11.GL_FLOAT
     */      
    public static final int GL_FLOAT = GL11.GL_FLOAT;
    
    /**
     * GL20.GL_FRAGMENT_SHADER
     */
    public static final int GL_FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;  
    
    /**
     * GL11.GL_LINE_LOOP
     */
    public static final int GL_LINE_LOOP = GL11.GL_LINE_LOOP;  
    
    /**
     * GL11.GL_POINTS
     */
    public static final int GL_POINTS = GL11.GL_POINTS;     
    
    /**
     * GL11.GL_RENDERER
     */
    public static final int GL_RENDERER = GL11.GL_RENDERER;    
    
    /**
     * GL20.GL_SHADING_LANGUAGE_VERSION
     */
    public static final int GL_SHADING_LANGUAGE_VERSION = GL20.GL_SHADING_LANGUAGE_VERSION;    
    
    /**
     * GL15.GL_STATIC_DRAW
     */      
    public static final int GL_STATIC_DRAW = GL15.GL_STATIC_DRAW;    
    
    /**
     * GL11.GL_STENCIL_BUFFER_BIT
     */
    public static final int GL_STENCIL_BUFFER_BIT = GL11.GL_STENCIL_BUFFER_BIT;
    
    /**
     * GL11.GL_TRIANGLE_STRIP
     */
    public static final int GL_TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;
    
    /**
     * GL11.GL_TRIANGLES
     */    
    public static final int GL_TRIANGLES = GL11.GL_TRIANGLES;
    
    /**
     * GL11.GL_UNSIGNED_INT
     */
    public static final int GL_UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
    
    /**
     * GL11.GL_VENDOR
     */
    public static final int GL_VENDOR = GL11.GL_VENDOR;
    
    /**
     * GL11.GL_VERSION
     */
    public static final int GL_VERSION = GL11.GL_VERSION;    
    
    /**
     * GL20.GL_VERTEX_SHADER
     */        
    public static final int GL_VERTEX_SHADER = GL20.GL_VERTEX_SHADER;    

    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glAttachShader.xml">glAttachShader</a>
     * @param program
     * @param shader 
     */
    public static void glAttachShader(int program, int shader) {
        GL20.glAttachShader(program, shader);
        GL.checkError("glAttachShader");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glBindAttribLocation.xml">glBindAttribLocation</a>
     * @param program
     * @param index
     * @param name 
     */
    public static void glBindAttribLocation(int program, int index, String name) {
        GL20.glBindAttribLocation(program, index, name);
        GL.checkError("glBindAttribLocation");
    }
    
    /**
     * OpenGL 1.5
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glBindBuffer.xml">glBindBuffer</a>
     * @param target
     * @param buffer 
     */
    public static void glBindBuffer(int target, int buffer) {
        GL15.glBindBuffer(target, buffer);
        GL.checkError("glBindBuffer");
    }
    
    /**
     * OpenGL 3.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glBindVertexArray.xml">glBindVertexArray</a>
     * @param array 
     */
    public static void glBindVertexArray(int array) {
        GL30.glBindVertexArray(array);
        GL.checkError("glBindVertexArray");
    }
    
    /**
     * OpenGL 1.5
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glBufferData.xml">glBufferData</a>
     * @param target
     * @param data
     * @param usage 
     */
    public static void glBufferData(int target, FloatBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
        GL.checkError("glBufferData");
    }
    
    /**
     * OpenGL 1.5
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glBufferData.xml">glBufferData</a>
     * @param target
     * @param data
     * @param usage 
     */
    public static void glBufferData(int target, IntBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
        GL.checkError("glBufferData");
    }

    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glClear.xml">glClear</a>
     * @param mask 
     */
    public static void glClear(int mask) {
        GL11.glClear(mask);
        GL.checkError("glClear");
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glClearColor.xml">glClearColor</a>
     * @param red
     * @param green
     * @param blue
     * @param alpha 
     */
    public static void glClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
        GL.checkError("glClearColor");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glCompileShader.xml">glCompileShader</a>
     * @param shader 
     */
    public static void glCompileShader(int shader) {
        GL20.glCompileShader(shader);
        GL.checkError("glCompileShader");        
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glCreateProgram.xml">glCreateProgram</a>
     * @return 
     */
    public static int glCreateProgram() {
        int id = GL20.glCreateProgram();
        GL.checkError("glCreateProgram");
        return id;
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glCreateShader.xml">glCreateShader</a>
     * @param type
     * @return 
     */
    public static int glCreateShader(int type) {
        int id = GL20.glCreateShader(type);
        GL.checkError("glCreateShader");
        return id;
    }

    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glDisable.xml">glDisable</a>
     * @param cap 
     */
    public static void glDisable(int cap) {
        GL11.glDisable(cap);
        GL.checkError("glDisable");
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glDrawArrays.xml">glDrawArrays</a>
     * @param mode
     * @param first
     * @param count 
     */
    public static void glDrawArrays(int mode, int first, int count) {
        GL11.glDrawArrays(mode, first, count);
        GL.checkError("glDrawArrays");
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glDrawElements.xml">glDrawElements</a>
     * @param mode
     * @param indices_count
     * @param type
     * @param indices_buffer_offset 
     */
    public static void glDrawElements(int mode, int indices_count, int type, int indices_buffer_offset) {
        GL11.glDrawElements(mode, indices_count, type, indices_buffer_offset);
        GL.checkError("glDrawElements");
    }

    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glEnable.xml">glEnable</a>
     * @param cap 
     */
    public static void glEnable(int cap) {
        GL11.glEnable(cap);
        GL.checkError("glEnable");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glEnableVertexAttribArray.xml">glEnableVertexAttribArray</a>
     * @param index 
     */
    public static void glEnableVertexAttribArray(int index) {
        GL20.glEnableVertexAttribArray(index);
        GL.checkError("glEnableVertexAttribArray");
    }
    
    /**
     * OpenGL 1.5
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGenBuffers.xml">glGenBuffers</a>
     * @return 
     */
    public static int glGenBuffers() {
        int id = GL15.glGenBuffers();
        GL.checkError("glGenBuffers");
        return id;
    }
    
    /**
     * OpenGL 3.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGenVertexArrays.xml">glGenVertexArrays</a>
     * @return 
     */
    public static int glGenVertexArrays() {
        int id = GL30.glGenVertexArrays();
        GL.checkError("glGenVertexArrays");
        return id;
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGetError.xml">glGetError</a>
     * @return 
     */
    public static int glGetError() {
        return GL11.glGetError();
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGetProgramInfoLog.xml">glGetProgramInfoLog</a>
     * @param program
     * @param maxLength
     * @return 
     */
    public static String glGetProgramInfoLog(int program, int maxLength) {
        String log = GL20.glGetProgramInfoLog(program, maxLength);
        GL.checkError("glGetProgramInfoLog");
        return log;
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGetShaderInfoLog.xml">glGetShaderInfoLog</a>
     * @param shader
     * @param maxLength
     * @return 
     */
    public static String glGetShaderInfoLog(int shader, int maxLength) {
        String log = GL20.glGetShaderInfoLog(shader, maxLength);
        GL.checkError("glGetShaderInfoLog");
        return log;
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGetString.xml">glGetString</a>
     * @param name
     * @return 
     */
    public static String glGetString(int name) {
        String string = GL11.glGetString(name);
        GL.checkError("glGetString");
        return string;
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glGetUniformLocation.xml">glGetUniformLocation</a>
     * @param program
     * @param name
     * @return 
     */
    public static int glGetUniformLocation(int program, String name) {
        int location = GL20.glGetUniformLocation(program, name);
        GL.checkError("glGetUniformLocation");
        if(location == -1) {
            System.err.println("WARNUNG: Uniform location von " + name + " ist -1! (Diese Meldung ist ein Service Ihres CG-Teams ;)");
        }
        return location;
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glLineWidth.xml">glLineWidth</a>
     * @param width 
     */
    public static void glLineWidth(float width) {
        GL11.glLineWidth(width);
        GL.checkError("glLineWidth");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glLinkProgram.xml">glLinkProgram</a>
     * @param program 
     */
    public static void glLinkProgram(int program) {
        GL20.glLinkProgram(program);
        GL.checkError("glLinkProgram");
    }
    
    /**
     * OpenGL 1.1
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glPointSize.xml">glPointSize</a>
     * @param size 
     */
    public static void glPointSize(float size) {
        GL11.glPointSize(size);
        GL.checkError("glPointSize");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glShaderSource.xml">glShaderSource</a>
     * @param shader
     * @param string 
     */
    public static void glShaderSource(int shader, String string) {
        GL20.glShaderSource(shader, string);
        GL.checkError("glShaderSource");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glUniform3f.xml">glUniform3f</a>
     * @param location
     * @param v0
     * @param v1
     * @param v2 
     */
    public static void glUniform3f(int location, float v0, float v1, float v2) {
        GL20.glUniform3f(location, v0, v1, v2);
        GL.checkError("glUniform3f");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glUniformMatrix4.xml">glUniformMatrix4</a>
     * @param location
     * @param transpose
     * @param matrices 
     */
    public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
        GL20.glUniformMatrix4(location, transpose, matrices);
        GL.checkError("glUniform3f");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glUseProgram.xml">glUseProgram</a>
     * @param program 
     */
    public static void glUseProgram(int program) {
        GL20.glUseProgram(program);
        GL.checkError("glUseProgram");
    }
    
    /**
     * OpenGL 2.0
     * @see <a href="http://www.opengl.org/sdk/docs/man4/xhtml/glVertexAttribPointer.xml">glVertexAttribPointer</a>
     * @param index
     * @param size
     * @param type
     * @param normalized
     * @param stride
     * @param buffer_buffer_offset 
     */
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
        GL20.glVertexAttribPointer(index, size, type, normalized, stride, buffer_buffer_offset);
        GL.checkError("glVertexAttribPointer");
    }
    
    private static void checkError(String source) {
        if(checkForErrors) {
            int errorcode = GL11.glGetError();
            String errorstring = null;
            switch(errorcode) {
                case GL11.GL_NO_ERROR: return;
                case GL11.GL_INVALID_ENUM: errorstring = "GL_INVALID_ENUM"; break;
                case GL11.GL_INVALID_OPERATION: errorstring = "GL_INVALID_OPERATION"; break;
                case GL11.GL_INVALID_VALUE: errorstring = "GL_INVALID_VALUE"; break;
                case GL30.GL_INVALID_FRAMEBUFFER_OPERATION: errorstring = "GL_INVALID_FRAMEBUFFER_OPERATION"; break;
                case GL11.GL_OUT_OF_MEMORY: errorstring = "GL_OUT_OF_MEMORY"; break;
            }
            throw new RuntimeException(source + ": " + errorstring);
        }
    }
    
    /**
     * Packs some lines into a box.
     * @param head The heading of the box
     * @param lines The lines in the box
     * @return A nicely formated string ;)
     */
    private static String pack(String head, String lines[]) {
        int maxLength = head.length() + 6;
        for(String line : lines) {
            maxLength = Math.max(maxLength, line.length());
        }
        String info = "";
        String footer = "+-";
        String header = "+- " + head + ' ';
        for(int i=0; i < maxLength; i++) {
            footer += '-';
            if(i > head.length() + 1) {
                header += '-';
            }
        }
        footer += "-+";
        header += "-+\n";
        for(String line : lines) {
            info += String.format("| %-" + maxLength + "s |\n", line);
        }
        return header + info + footer;
    }    
}