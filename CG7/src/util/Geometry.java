package util;

import static opengl.GL.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Kapselt ein Vertexarray Object.
 * @author Sascha Kolodzey, Nico Marniok
 */
public class Geometry {
    private int vaid = -1;                  // vertex array id
    private FloatBuffer vertexValueBuffer;  // vertex buffer values
    private IntBuffer indexValueBuffer;     // index buffer values
    private int topology;                   // index topology
    private int indexCount;                 // number of indices
    private int vbid;                       // geometry vertex buffer
    private int ibid;                       // geometry index buffer

    /**
     * Setzt den IntBuffer, der die Indexdaten dieser Geometrie beinhaltet und
     * die zugehoerige Topologie.
     * <b>Obacht:</b> Diese Methode erzeugt <i>keinen</i> Buffer auf der GPU.
     * @param indices Buffer, der die Indexdaten beinhaltet
     * @param topology Zugehoerige Topologie
     */
    public void setIndexBuffer(IntBuffer indices, int topology) {
        // TODO: Aufgabe 7.1
    	this.indexValueBuffer = indices;
    	this.indexCount = indices.limit();
    	this.topology = topology;
    }

    /**
     * Setzt den FloatBuffer, der die Indexdaten dieser Geometrie beinhaltet.
     * <b>Obacht:</b> Diese Methode erzeugt <i>keinen</i> Buffer auf der GPU.
     * @param vertices 
     */
    public void setVertices(FloatBuffer vertices) {
        // TODO: Aufgabe 7.1
    	this.vertexValueBuffer = vertices;
    }
    
    /**
     * Erzeugt aus den gesetzten Vertex- und Indexdaten ein Vertexarray Object,
     * das die zugoerige Topologie beinhaltet.
     */
    public void construct() {
        if(vertexValueBuffer == null || indexValueBuffer == null) {
            throw new UnsupportedOperationException("Vertex- und Indexbuffer wurden noch nicht gesetzt!");
        }
        vaid = glGenVertexArrays();
        glBindVertexArray(vaid);

        ibid = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibid);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexValueBuffer, GL_STATIC_DRAW);
        
        vbid = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbid);
        glBufferData(GL_ARRAY_BUFFER, vertexValueBuffer, GL_STATIC_DRAW);
        
        // TODO: Aufgabe 7.1
    }
    
    /**
     * Loescht alle GPU Resourcen dieser Geometrie, die in
     * <code>construct()</code> generiert wurden (Indexbuffer, Vertexbuffer und 
     * das Vertexarray Object).
     */
    public void delete() {
        // TODO: Aufgabe 7.1
    	glDeleteBuffers(ibid);
    	glDeleteBuffers(vbid);
    	glDeleteVertexArrays(vaid);
    }
    
    /**
     * Erzeugt die Geometrie, falls noch nicht geschehen und zeichnet sie
     * anschließend.
     */
    public void draw() {
        // TODO: Aufgabe 7.1
    }
}
