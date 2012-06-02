package test;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import util.Util;

/**
 *
 * @author Sascha Kolodzey, Nico Marniok
 */
public class Test512 {
    public static void main(String argv[]) {
        Matrix4f view = new Matrix4f();
        view.m03 = 1.0f;
        if(view != Util.lookAtRH(new Vector3f(0.0f, 2.0f, 4.0f), new Vector3f(0.0f, 2.0f, 2.0f), new Vector3f(0.0f, 1.0f, -1.0f), view)) {
            throw new RuntimeException("FEHLER: Parameter dst in lookAt(...) wird nicht zurueckgegeben");
        }
        Matrix4f projection = new Matrix4f();
        projection.m03 = 1.0f;
        if(projection != Util.orthographicRH(projection)) {
            throw new RuntimeException("FEHLER: Parameter dst in orthographicProjection(...) wird nicht zurueckgegeben");
        }
        Vector3f coords[] = new Vector3f[] {
            new Vector3f(1.0f, 2.0f, 3.0f),
            new Vector3f(-1.0f, -4.0f, 2.0f),
            new Vector3f(-1.0f, 3.0f, 1.0f),
            new Vector3f(2.0f, -2.0f, 0.0f),
        };
        Vector3f viewTarget[] = new Vector3f[] {
            new Vector3f(1.0f, 0.0f, -1.0f),
            new Vector3f(-1.0f, -6.0f, -2.0f),
            new Vector3f(-1.0f, 1.0f, -3.0f),
            new Vector3f(2.0f, -4.0f, -4.0f),
        };
        Vector3f projectionTarget[] = new Vector3f[] {
            new Vector3f(1.0f, 0.0f, 0.0f),
            new Vector3f(-1.0f, -6.0f, 0.0f),
            new Vector3f(-1.0f, 1.0f, 0.0f),
            new Vector3f(2.0f, -4.0f, 0.0f),
        };
        for(int i=0; i < coords.length; ++i) {
            Vector3f viewVec = Util.transformCoord(view, coords[i], null);
            if(Vector3f.sub(viewVec, viewTarget[i], null).lengthSquared() > 1e-3f) {
                throw new RuntimeException("FEHLER: View Matrix fehlerhaft!");
            }
            System.out.println("KORREKT: view * coords[" + i + "]");
            Vector3f projVec = Util.transformCoord(projection, viewVec, null);
            if(Vector3f.sub(projVec, projectionTarget[i], null).lengthSquared() > 1e-3f) {
                throw new RuntimeException("FEHLER: Projection Matrix fehlerhaft!");
            }
            System.out.println("KORREKT: projection * view * coords[" + i + "]");
        }
        System.out.println("KORREKT: Alle Berechnungen :D");
    }
}
