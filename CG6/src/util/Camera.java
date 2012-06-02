package util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Sascha Kolodzey, Nico Marniok
 */
public final class Camera {
    private float phi = 0, theta = 0;
    private final Vector3f viewDir = new Vector3f(0,0,1);
    private final Vector3f upDir = new Vector3f(0,1,0);
    private final Vector3f sideDir = new Vector3f(1,0,0);
    private final Vector3f camPos = new Vector3f(0,0,-1);
    private final Matrix4f view = new Matrix4f();
    private final Matrix4f projection = new Matrix4f();
    private boolean perspective = false;

    /**
     * Default Constructor.
     */
    public Camera() {
        this.updateView();
        this.updateProjection();
    }
    
    /**
     * Rotiert die Kamera horizontal und vertikal.
     * @param dPhi horizontale Rotation
     * @param dTheta vertikale Rotation
     */
    public void rotate(float dPhi, float dTheta) {
    	this.phi += dPhi;
    	this.theta += dTheta;
    	if(theta > Math.PI/2) //beschraenkt die bewegung der kamera nach unten auf 90grad
    		theta = (float) Math.PI/2;
    	else if(theta < -Math.PI/2) //beschraenkt sicht nach oben um 90grad
    		theta = (float)-Math.PI/2;
    	Matrix4f rotX = Util.rotationX(this.theta, null);
    	Matrix4f rotY = Util.rotationY(this.phi, null);
    	Matrix4f rot = Util.mul(null, rotY, rotX);
    	Util.transformDir(rot, new Vector3f(1,0,0), this.sideDir);
    	Util.transformDir(rot, new Vector3f(0,1,0), this.upDir);
    	Util.transformDir(rot, new Vector3f(0,0,1), this.viewDir);
    }
    
    /**
     * Bewegt die Kamera.
     * @param fb Bewegung in Sichtrichtung
     * @param lr Bewegung in seitliche Richtung
     * @param ud Bewegung nach oben/unten
     */
    public void move(float fb, float lr, float ud) {
    	this.camPos.x += this.sideDir.x * lr + this.viewDir.x * fb;
    	this.camPos.y += this.sideDir.y * lr + this.viewDir.y * fb + ud;
    	this.camPos.z += this.sideDir.z * lr + this.viewDir.z * fb;
    }
    
    /**
     * Aktualisiert die Viewmatrix.
     */
    public void updateView() {
    	Util.lookAtRH(this.camPos, Vector3f.add(this.camPos, this.viewDir, null), this.upDir, view);
    }
    
    /**
     * Aktualisiert die Projektionsmatrix.
     */
    public void updateProjection() {
        if(perspective) {
            Util.frustum(-1e-2f, 1e-2f, -1e-2f, 1e-2f, 1e-2f, 1e+2f, projection);
        } else {
            Util.ortho(-1.0f, 1.0f, -1.0f, 1.0f, 1e-2f, 1e+2f, projection);
        }
    }
    
    /**
     * Aendert die Projektion (perspektivisch vs. parellel).
     */
    public void changeProjection() {
        perspective ^= true;
    }

    /**
     * Getter fuer die Projektionsmatrix.
     * @return Projektionsmatrix
     */
    public Matrix4f getProjection() {
        this.updateProjection();
        return projection;
    }

    /**
     * Getter fuer die Viewmatrix.
     * @return Viewmatrix
     */
    public Matrix4f getView() {
        this.updateView();
        return view;
    }
}