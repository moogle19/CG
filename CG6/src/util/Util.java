package util;

import static opengl.GL.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Sascha Kolodzey, Nico Marniok
 */
public class Util {
    public static final FloatBuffer MAT_BUFFER = BufferUtils.createFloatBuffer(16);
    public static final float PI = (float)Math.PI;
    public static final float PI_DIV2 = 0.5f * (float)Math.PI;
    public static final float PI_DIV4 = 0.25f * (float)Math.PI;
    public static final float PI_MUL2 = 2.0f * (float)Math.PI;
    
    /**
     * Erzeugt eine Viewmatrix aus Augenposition und Fokuspunkt.
     * @param eye Die Position des Auges
     * @param at Anvisierter Punkt
     * @param up Up Vektor des Auges
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f lookAtRH(Vector3f eye, Vector3f at, Vector3f up, Matrix4f dst)
    {
        Vector3f diffAtEye = new Vector3f();
        Vector3f viewDir = new Vector3f();
        Vector3f negViewDir = new Vector3f();
        Vector3f side = new Vector3f();
        Vector3f crossDirUp = new Vector3f();
        Vector3f newUp = new Vector3f();
        Vector3f crossSideDir = new Vector3f();
                
        //view direction
        Vector3f.sub(at, eye, diffAtEye);
        viewDir.setX( (float) (diffAtEye.getX() / diffAtEye.length()) );
        viewDir.setY( (float) (diffAtEye.getY() / diffAtEye.length()) );
        viewDir.setZ( (float) (diffAtEye.getZ() / diffAtEye.length()) );
        viewDir.normalise();
        viewDir.negate(negViewDir);
        
        //side
        Vector3f.cross(viewDir, up, crossDirUp);
        side.setX( (float) (crossDirUp.getX() / crossDirUp.length()));
        side.setY( (float) (crossDirUp.getY() / crossDirUp.length()));
        side.setZ( (float) (crossDirUp.getZ() / crossDirUp.length()));
        side.normalise();

        //newUp
        Vector3f.cross(side, viewDir, crossSideDir);
        newUp.setX( (float) (crossSideDir.getX() / crossSideDir.length()));
        newUp.setY( (float) (crossSideDir.getY() / crossSideDir.length()));
        newUp.setZ( (float) (crossSideDir.getZ() / crossSideDir.length()));
        
        if (dst == null)
        {
            dst = new Matrix4f();
        }
        
        dst.m00 = side.getX(); dst.m10 = side.getY(); dst.m20 = side.getZ(); dst.m30 = - Vector3f.dot(eye, side);
        dst.m01 = newUp.getX(); dst.m11 = newUp.getY(); dst.m21 = newUp.getZ(); dst.m31 = - Vector3f.dot(eye, newUp);
        dst.m02 = - viewDir.getX();  dst.m12 = - viewDir.getY(); dst.m22 = - viewDir.getZ(); dst.m32 = - Vector3f.dot(eye, negViewDir);
        dst.m03 = 0; dst.m13 = 0; dst.m23 = 0; dst.m33 = 1;
        
        return dst;
    }
    
    public static Matrix4f ortho(float l, float r, float b, float t, float n, float f, Matrix4f dst)
    {
        if(dst == null) dst = new Matrix4f();
        else dst.setIdentity();
    	dst.m00 = 2.0f/(r-l);
    	dst.m11 = 2.0f/(t-b);
    	dst.m22 = -(2.0f/(f-n));
    	dst.m33 = 1.0f;
    	dst.m30 = -((r+l)/(r-l));
    	dst.m31 = -((t+b)/(t-b));
    	dst.m32 = -((n+f)/(f-n));    	
    	return dst;
   }
    
    public static Matrix4f frustum(float l, float r, float b, float t, float n, float f, Matrix4f dst)
    {
        if(dst == null) dst = new Matrix4f();
        else dst.setIdentity();
    	dst.m00 = 2.0f*n/(r-l);
    	dst.m11 = 2.0f*n/(t-b);
    	dst.m20 = (r+l)/(r-l);
    	dst.m21 = (t+b)/(t-b);
    	dst.m22 = -((f+n)/(f-n));
    	dst.m23 = -1.0f;
    	dst.m32 = -((2.0f*n*f)/(f-n));
    	dst.m33 = 0.0f;
    	return dst;
    }
    
    
    /**
     * Erzeugt die orthogonale Projektionsmatrix, die dem klassichen Ansatz
     * entspricht.
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f orthographicRH(Matrix4f dst)
    {
        if(dst == null) dst = new Matrix4f();        
        dst.setIdentity();
        dst.m22 = 0;        
        return dst;
    }
    
    /**
     * Erzeugt eine Rotationsmatrix um die x-Achse.
     * @param angle Winkel in Bogenass
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f rotationX(float angle, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        dst.m11 = dst.m22 = (float)Math.cos(angle);
        dst.m21 = -(dst.m12 = (float)Math.sin(angle));
        return dst;
    }
    
    /**
     * Erzeugt eine Rotationsmatrix um die y-Achse.
     * @param angle Winkel in Bogenass
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f rotationY(float angle, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        
        dst.m00 = dst.m22 = (float)Math.cos(angle);
        dst.m02 = -(dst.m20 = (float)Math.sin(angle));
        
        return dst;
    }
    
    /**
     * Erzeugt eine Rotationsmatrix um die z-Achse.
     * @param angle Winkel in Bogenass
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */    
    public static Matrix4f rotationZ(float angle, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        
        dst.m00 = dst.m11 = (float)Math.cos(angle);
        dst.m10 = -(dst.m01 = (float)Math.sin(angle));
        
        return dst;
    }
    
    /**
     * Erzeugt eine Translationsmatrix.
     * @param translation Der Translationsvektor
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f translation(Vector3f translation, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        
        dst.m30 = translation.x;
        dst.m31 = translation.y;
        dst.m32 = translation.z;
        
        return dst;
    }
    
    /**
     * Erzeugt eine Translationsmatrix in x-Richtung.
     * @param x Der Translationslaenge
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f translationX(float x, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        dst.m30 = x;
        return dst;
    }
    
    /**
     * Erzeugt eine Translationsmatrix in y-Richtung.
     * @param y Der Translationslaenge
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f translationY(float y, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        dst.m31 = y;
        return dst;
    }
    
    /**
     * Erzeugt eine Translationsmatrix in z-Richtung.
     * @param z Der Translationslaenge
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f translationZ(float z, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        dst.m32 = z;
        return dst;
    }
    
    /**
     * Erzeugt eine Skalierungsmatrix.
     * @param scale Skalierungskomponente
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f scale(Vector3f scale, Matrix4f dst) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        
        dst.m00 = scale.x;
        dst.m11 = scale.y;
        dst.m22 = scale.z;
        
        return dst;
    }
    
    /**
     * Erzeugt eine gleichmaessige Skalierungsmatrix.
     * @param scale Skalierungskomponente
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @return Ergebnismatrix
     */
    public static Matrix4f scale(float scale, Matrix4f dst) {
        return Util.scale(new Vector3f(scale, scale, scale), dst);
    }
    
    /**
     * Transformiert einen Vector3f mittels einer Matrix4f. Der Vektor wird um
     * die homogene Koordinate 1 erweitert und anschliessend homogenisiert.
     * @param left Trabsformationsmatrix
     * @param right Zu transformierender Vektor
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */
    public static Vector3f transformCoord(Matrix4f left, Vector3f right, Vector3f dst) {
        if(dst == null) dst = new Vector3f();
        Vector4f vec = Matrix4f.transform(left, new Vector4f(right.x, right.y, right.z, 1.0f), null);
        vec.scale(1.0f / vec.w);
        dst.set(vec.x, vec.y, vec.z);
        return dst;
    }
    
    /**
     * Transformiert einen Vector3f mittels einer Matrix4f. Der Vektor wird um
     * die homogene Koordinate 0 erweitert.
     * @param left Trabsformationsmatrix
     * @param right Zu transformierender Vektor
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */
    public static Vector3f transformDir(Matrix4f left, Vector3f right, Vector3f dst) {
        if(dst == null) dst = new Vector3f();
        Vector4f vec = Matrix4f.transform(left, new Vector4f(right.x, right.y, right.z, 0.0f), null);
        dst.set(vec.x, vec.y, vec.z);
        return dst;
    }
    
    /**
     * Multipliziert beliebig viele Matrizen miteinander.
     * @param dst Matrix, in die das Ergebnis gespeichert wird. Wenn null wird
     * eine neue erstellt.
     * @param factors Matrizen, die multipliziert werden sollen
     * @return Ergebnismatrix
     */
    public static Matrix4f mul(Matrix4f dst, Matrix4f ...factors) {
        if(dst == null) dst = new Matrix4f();
        dst.setIdentity();
        for(Matrix4f mat : factors) {
            Matrix4f.mul(dst, mat, dst);
        }
        return dst;
    }
    
    /**
     * Schneidet einen Wert zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @return Falls val &lt; min, dann min. Falls val &gt; max, dann max. sonst
     * val.
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(val, max));
    }
    
    /**
     * Schneidet einen Vektor komponentenweise zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */
    public static Vector2f clamp(Vector2f val, Vector2f min, Vector2f max, Vector2f dst) {
        if(dst == null) dst = new Vector2f();
        dst.x = clamp(val.x, min.x, max.x);
        dst.y = clamp(val.y, min.y, max.y);
        return dst;
    }
    
    /**
     * Schneidet einen Vektor zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */    
    public static Vector2f clamp(Vector2f val, float min, float max, Vector2f dst) {
        return clamp(val, new Vector2f(min, min), new Vector2f(max, max), dst);
    }
    
    /**
     * Schneidet einen Vektor komponentenweise zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */    
    public static Vector3f clamp(Vector3f val, Vector3f min, Vector3f max, Vector3f dst) {
        if(dst == null) dst = new Vector3f();
        dst.x = clamp(val.x, min.x, max.x);
        dst.y = clamp(val.y, min.y, max.y);
        dst.z = clamp(val.z, min.z, max.z);
        return dst;
    }
    
    /**
     * Schneidet einen Vektor zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */    
    public static Vector3f clamp(Vector3f val, float min, float max, Vector3f dst) {
        return clamp(val, new Vector3f(min, min, min), new Vector3f(max, max, max), dst);
    }
    
    /**
     * Schneidet einen Vektor komponentenweise zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */    
    public static Vector4f clamp(Vector4f val, Vector4f min, Vector4f max, Vector4f dst) {
        if(dst == null) dst = new Vector4f();
        dst.x = clamp(val.x, min.x, max.x);
        dst.y = clamp(val.y, min.y, max.y);
        dst.z = clamp(val.z, min.z, max.z);
        dst.w = clamp(val.w, min.w, max.w);
        return dst;
    }
    
    /**
     * Schneidet einen Vektor zurecht.
     * @param val Wert
     * @param min minimaler Wert
     * @param max maximaler Wert
     * @param dst Vektor, in den das Ergebnis gespeichert wird. Wenn null wird
     * ein neuer erstellt.
     * @return Ergebnisvektor
     */    
    public static Vector4f clamp(Vector4f val, float min, float max, Vector4f dst) {
        return clamp(val, new Vector4f(min, min, min, min), new Vector4f(max, max, max, max), dst);
    }
    
    /**
     * Attribut Index von vs_in_pos
     */
    public static final int ATTR_POS = 0;

    /**
     * Attribut Index von vs_in_normal
     */
    public static final int ATTR_NORMAL = 1;

    /**
     * Attribut Index von vs_in_color
     */
    public static final int ATTR_COLOR = 2;
    
    /**
     * Erzeugt ein gleichmaessiges 2D n-Eck in der xy-Ebene. (n Indizes, als
     * GL_LINE_LOOP)
     * @param n Anzahl der Ecken
     * @return VertexArrayObject ID
     */
    public static int createNGon(int n) {        
        int vaid = glGenVertexArrays();
        glBindVertexArray(vaid);        
        
        // indexbuffer
        IntBuffer indexData = BufferUtils.createIntBuffer(n);
        for(int i=0; i < n; ++i) {
            indexData.put(i);
        }
        indexData.flip();
        
        int indexBufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW); 
        
        // vertexbuffer
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(3*n + 3*n); // world coords + normal coords
        double phi = 0;
        double deltaPhi = 2.0*Math.PI / (double)n;
        for(int i=0; i < n; ++i) {
            vertexData.put(0.5f*(float)Math.cos(phi));   // position x
            vertexData.put(0.5f*(float)Math.sin(phi));   // position y
            vertexData.put(0.5f*0.0f);                   // position z
            vertexData.put((float)Math.cos(phi));   // normal x
            vertexData.put((float)Math.sin(phi));   // normal y
            vertexData.put(0.0f);                   // normal z
            phi += deltaPhi;
        }
        vertexData.position(0);
                
        int vertexBufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);       
        
         // vs_in_pos  
        glEnableVertexAttribArray(ATTR_POS);
        glVertexAttribPointer(ATTR_POS, 3, GL_FLOAT, false, 24, 0);
         // vs_in_normal
        glEnableVertexAttribArray(ATTR_NORMAL);
        glVertexAttribPointer(ATTR_NORMAL, 3, GL_FLOAT, false, 24, 12);        
        
        return vaid;
    }
    
    /**
     * Erzeugt ein Dreieck in der xy-Ebene. (3 Indizes)
     * @return VertexArrayObject ID
     */
    public static int createTriangle() {
        int valCnt = 21;
        float[] vertices_in = {  1f,  0f, 0f, 1f, -1f, -1f, 0f,
                                 0f,  1f, 0f, 1f, 1f, -1f, 0f,
                                 0f,  0f, 1f, 1f, 0f,  1f, 0f
                              };
        
        FloatBuffer vertices_in_fb = BufferUtils.createFloatBuffer(valCnt);
        for(int i = 0; i < valCnt; i++)
        {
            vertices_in_fb.put(i, vertices_in[i]);
        }
        vertices_in_fb.position(0);
        
        int idBO = glGenBuffers();
        int dataState = glGenVertexArrays();
        
        glBindBuffer(GL_ARRAY_BUFFER, idBO);
        glBufferData(GL_ARRAY_BUFFER, vertices_in_fb, GL_STATIC_DRAW);
        
        glBindVertexArray(dataState);

        glBindBuffer(GL_ARRAY_BUFFER, idBO);
        glEnableVertexAttribArray(ATTR_COLOR);
        glEnableVertexAttribArray(ATTR_POS);
        glVertexAttribPointer(ATTR_COLOR, 4, GL_FLOAT, false, 28,  0);   // Stride: (4+3)*4
        glVertexAttribPointer(ATTR_POS,   3, GL_FLOAT, false, 28, 16);   // Offset: 4*4
        
        return dataState; 
    }
    
    /**
     * Erzeugt ein Viereck in der xy-Ebene. (4 Indizes)
     * @return VertexArrayObject ID
     */
    public static int createQuad()
    {        

        int valCnt = (3*4)+(4*4);
        float[] vertices_in = { -1f, -1f, 0f, 0f,  1f, 1f, 1f,
                                 1f, -1f, 0f, 1f,  0f, 1f, 1f,
                                 1f,  1f, 0f, 1f,  1f, 0f, 1f,
                                -1f,  1f, 0f, 1f,  1f, 1f, 1f   
                              };
        
        FloatBuffer vertices_in_fb = BufferUtils.createFloatBuffer(valCnt);
        for(int i = 0; i < valCnt; i++)
        {
            vertices_in_fb.put(i, vertices_in[i]);
        }
        vertices_in_fb.position(0);
        
        int idBO = glGenBuffers();
        int dataState = glGenVertexArrays();
        
        glBindBuffer(GL_ARRAY_BUFFER, idBO);
        glBufferData(GL_ARRAY_BUFFER, vertices_in_fb, GL_STATIC_DRAW);
        
        glBindVertexArray(dataState);

        glBindBuffer(GL_ARRAY_BUFFER, idBO);
        glEnableVertexAttribArray(ATTR_POS);
        glEnableVertexAttribArray(ATTR_COLOR);
        glVertexAttribPointer(ATTR_POS,   3, GL_FLOAT, false, 28,  0);   // Stride: (3+4)*4 ATTR_POS laenge 3 + ATTR_COLOR laenge 4 * 4bit
        glVertexAttribPointer(ATTR_COLOR, 4, GL_FLOAT, false, 28, 12);   // Offset: (3*4) da ATTR_POS laenge 3 * 4bit
        
        return dataState;
    }
    
    /**
     * Erzeugt ein ShaderProgram aus einem Vertex- und Fragmentshader.
     * @param vs Pfad zum Vertexshader
     * @param fs Pfad zum Fragmentshader
     * @return ShaderProgram ID
     */
    public static int createShaderProgram(String vs, String fs) {
        int programID = glCreateProgram();
        
        int vsID = glCreateShader(GL_VERTEX_SHADER);
        int fsID = glCreateShader(GL_FRAGMENT_SHADER);
        
        glAttachShader(programID, vsID);
        glAttachShader(programID, fsID);
        
        String vertexShaderContents = Util.getFileContents(vs);
        String fragmentShaderContents = Util.getFileContents(fs);
        
        glShaderSource(vsID, vertexShaderContents);
        glShaderSource(fsID, fragmentShaderContents);
        
        glCompileShader(vsID);
        glCompileShader(fsID);
        
        String log;
        log = glGetShaderInfoLog(vsID, 1024);
        System.out.print(log);
        log = glGetShaderInfoLog(fsID, 1024);
        System.out.print(log);
        
        glBindAttribLocation(programID, ATTR_POS, "vs_in_pos");
        glBindAttribLocation(programID, ATTR_NORMAL, "vs_in_normal");        
        glBindAttribLocation(programID, ATTR_COLOR, "vs_in_color");        
        
        glLinkProgram(programID);        
        
        log = glGetProgramInfoLog(programID, 1024);
        System.out.print(log);
                
        return programID;
    }
    
    /**
     * Liest den Inhalt einer Datei und liefert ihn als String zurueck.
     * @param filename Pfad der Datei
     * @return Inhalt der Datei
     */
    private static String getFileContents(String filename) {
        BufferedReader reader = null;
        String source = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            source = "";
            String line;
            while((line = reader.readLine()) != null) {
                source += line + "\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return source;
    }    
}
