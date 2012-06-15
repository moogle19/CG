package util;

import static opengl.GL.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

/**
 * Stellt Methoden zur Erzeugung von Geometrie bereit.
 * @author Sascha Kolodzey, Nico Marniok
 */
public class GeometryFactory {
    
    /**
     * Erzeugt eine Kugel.
     * @param r Radius der Kugel
     * @param n Anzahl der vertikalen Streifen
     * @param k Anzahl der horizontalen Streifen
     * @param imageFile Pfad zu einer Bilddatei
     * @return Geometrie der Kugel
     */
    public static Geometry createSphere(float r, int n, int k, String imageFile, String nightImageFile) {
        float[][][] image = Util.getImageContents(imageFile);
        float[][][] nightImage;
        if(nightImageFile == null) {
        	nightImage = image;
        }
        else {
        	nightImage = Util.getImageContents(nightImageFile);
        }
        
        
        FloatBuffer fb = BufferUtils.createFloatBuffer((3+3+4+4) * (n+1)*(k+1));
        
        float dTheta = Util.PI / (float)k;
        float dPhi = Util.PI_MUL2 / (float)n;
        float theta = 0;
        Vector3f norm;
        for(int j=0; j <= k; ++j) {
            float sinTheta = (float)Math.sin(theta);
            float cosTheta = (float)Math.cos(theta);
            float phi = 0;
            for(int i=0; i <= n; ++i) {
                float sinPhi = (float)Math.sin(phi);
                float cosPhi = (float)Math.cos(phi);
                
                fb.put(r*sinTheta*cosPhi);
                fb.put(r*cosTheta);
                fb.put(r*sinTheta*sinPhi);
                
                norm = new Vector3f(r*sinTheta*cosPhi, r*cosTheta, r*sinTheta*sinPhi);
                norm.normalise();
                				// TODO: sinnvoll??
                fb.put(norm.x); // + r*sinTheta*cosPhi);
                fb.put(norm.y); // + r*cosTheta);
                fb.put(norm.z); // + r*sinTheta*sinPhi);
                
                fb.put(image[(int)((theta / Util.PI) * (float)image.length) % image.length]
                            [(int)(phi / Util.PI_MUL2 * (float)image[0].length) % image[0].length]);
                
                fb.put(nightImage[(int)((theta / Util.PI) * (float)image.length) % image.length]
                        		 [(int)(phi / Util.PI_MUL2 * (float)image[0].length) % image[0].length]);
                
                phi += dPhi;
            }
            theta += dTheta;
        }
        fb.position(0);
        
        IntBuffer ib = BufferUtils.createIntBuffer(k*(2*(n+1)+1));
        for(int j=0; j < k; ++j) {
            for(int i=0; i <= n; ++i) {
                ib.put((j+1)*(n+1) + i);
                ib.put(j*(n+1) + i);
            }
            ib.put(RESTART_INDEX);
        }
        ib.position(0);
        
        Geometry sphere = new Geometry();
        sphere.setIndices(ib, GL_TRIANGLE_STRIP);
        sphere.setVertices(fb);
        return sphere;
    }
    
    /**
     * Erzeugt ein Vierexk in der xy-Ebene. (4 Indizes)
     * @return VertexArrayObject ID
     */
    public static int createQuad() {        
        int vaid = glGenVertexArrays();
        glBindVertexArray(vaid);        
        
        // vertexbuffer
        FloatBuffer vertexData = BufferUtils.createFloatBuffer((3+4)*4); // world coords, color
        vertexData.put(new float[] {
            -1.0f, -1.0f, 0.0f,  1.0f, 1.0f, 0.4f, 1.0f,
            +1.0f, -1.0f, 0.0f,  0.4f, 1.0f, 0.4f, 1.0f,
            +1.0f, +1.0f, 0.0f,  1.0f, 1.0f, 0.4f, 1.0f,
            -1.0f, +1.0f, 0.0f,  0.4f, 1.0f, 0.4f, 1.0f,
        });
        vertexData.position(0);
                
        int vertexBufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);       
        
         // vs_in_pos  
        glEnableVertexAttribArray(Util.ATTR_POS);
        glVertexAttribPointer(Util.ATTR_POS, 3, GL_FLOAT, false, (3+4)*4, 0);       
        // vs_in_color
        glEnableVertexAttribArray(Util.ATTR_COLOR);
        glVertexAttribPointer(Util.ATTR_COLOR, 4, GL_FLOAT, false, (3+4)*4, 3*4);
        
        return vaid;
    }
    
    /**
     * Erzeugt ein Dreieck in der xy-Ebene. (3 Indizes)
     * @return VertexArrayObject ID
     */
    public static int createTriangle() {
        int vaid = glGenVertexArrays();
        glBindVertexArray(vaid);        
        
        // vertexbuffer
        FloatBuffer vertexData = BufferUtils.createFloatBuffer((3+4)*3); // color, world coords
        vertexData.put(new float[] {
            0.4f, 1.0f, 1.0f, 1.0f,  -1.0f, -1.0f, 0.0f,
            0.4f, 1.0f, 1.0f, 1.0f,  +1.0f, -1.0f, 0.0f,
            0.4f, 0.4f, 1.0f, 1.0f,   0.0f, +1.0f, 0.0f,
        });
        vertexData.position(0);
                
        int vertexBufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);       
        
        // vs_in_color
        glEnableVertexAttribArray(Util.ATTR_COLOR);
        glVertexAttribPointer(Util.ATTR_COLOR, 4, GL_FLOAT, false, (3+4)*4, 0);
         // vs_in_pos  
        glEnableVertexAttribArray(Util.ATTR_POS);
        glVertexAttribPointer(Util.ATTR_POS, 3, GL_FLOAT, false, (3+4)*4, 4*4);
        
        return vaid;        
    }
    
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
        glEnableVertexAttribArray(Util.ATTR_POS);
        glVertexAttribPointer(Util.ATTR_POS, 3, GL_FLOAT, false, 24, 0);
         // vs_in_normal
        glEnableVertexAttribArray(Util.ATTR_NORMAL);
        glVertexAttribPointer(Util.ATTR_NORMAL, 3, GL_FLOAT, false, 24, 12);        
        
        return vaid;
    }
}
