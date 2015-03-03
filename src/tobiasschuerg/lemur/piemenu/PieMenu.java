/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.simsilica.lemur.event.CursorEventControl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tobias
 */
public class PieMenu extends SpatialSelectionListener {

    private Spatial target;
    private List<Geometry> options = new ArrayList<Geometry>();
    private float radius = 3f;
    private Geometry disk;
    public Node menu;
    private boolean isSpacialShowing;

    public PieMenu() {
        menu = new Node("pie menu");
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        menu.lookAt(app.getCamera().getLocation(), app.getCamera().getUp());
    }

    @Override
    public void onSpatialSelected(Spatial spatial) {
        this.target = spatial;
        Material m = ((Geometry) spatial).getMaterial();
        m.setColor("Color", ColorRGBA.Red);

        if (!isSpacialShowing) {
            addSelectionPlane();
            showOptions(3);
            isSpacialShowing = true;
        } else {
            close();
        }
    }

    private void showOptions(int count) {
        float degreesperoption;

        if (count < 7) {
            degreesperoption = FastMath.PI / (count - 1);
        } else {
            degreesperoption = 2 * FastMath.PI / (count - 1);
        }

        Quaternion q = new Quaternion();
        q.fromAngleAxis(degreesperoption, Vector3f.UNIT_X);

        for (int i = 0; i < count; i++) {
            float length = 0.4f;
            Box b = new Box(length, length, length / 10);
            Geometry geom = new Geometry("Option" + i, b);

            Material mat = ((Geometry) target).getMaterial().clone();
            mat.setColor("Color", ColorRGBA.randomColor());
            geom.setMaterial(mat);

            q.fromAngleAxis(degreesperoption * i - FastMath.PI / 2, Vector3f.UNIT_Z);

            Vector3f positionVector = Vector3f.UNIT_Y.mult(radius);
            q.multLocal(positionVector);
            geom.move(positionVector);

            menu.attachChild(geom);
            options.add(geom);

            //OptionSelectionListener optionSelectedListener = new OptionSelectionListener(this);
            //MouseEventControl.addListenersToSpatial(geom, optionSelectedListener);
            //app.getStateManager().attach(optionSelectedListener);
        }
    }

    @Override
    boolean isReadyToSelect() {
        boolean ready = target == null;
        // System.out.println("Ready? " + ready);
        return ready;
    }

    private void removeOptions() {
        for (Geometry option : options) {
            option.removeFromParent();
        }
        options.clear();
    }

    private void addSelectionPlane() {
        removeSelectionPlane();
        Cylinder c = new Cylinder(8, 32, 5f, 0.01f, true);        
        disk = new Geometry("disk", c);

        Material mat = ((Geometry) target).getMaterial().clone();
        mat.setColor("Color", ColorRGBA.randomColor());
        disk.setMaterial(null);
        disk.setCullHint(Spatial.CullHint.Always);
        menu.attachChild(disk);

        CursorEventControl.addListenersToSpatial(disk, new OptionSelectionListener(this));
    }

    private void removeSelectionPlane() {
        if (disk != null) {
            disk.removeFromParent();
            disk = null;
        }
    }

    public List<Geometry> getOptions() {
        return options;
    }

    void close() {
        removeOptions();
        removeSelectionPlane();
        target = null;
        isSpacialShowing = false;
    }
}
