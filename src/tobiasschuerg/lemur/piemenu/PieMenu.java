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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.event.MouseEventControl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tobias
 */
public class PieMenu extends SpatialSelectionListener {

    private Spatial target;
    private List<Geometry> options = new ArrayList<Geometry>();

    @Override
    public void onSpatialSelected(Spatial spatial) {
        this.target = spatial;
        Material m = ((Geometry) spatial).getMaterial();
        m.setColor("Color", ColorRGBA.Red);

        
        showOptions(20);
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
            Geometry geom = new Geometry("Option", b);

            Material mat = ((Geometry) target).getMaterial().clone();
            mat.setColor("Color", ColorRGBA.randomColor());
            geom.setMaterial(mat);

            q.fromAngleAxis(degreesperoption * i - FastMath.PI / 2, Vector3f.UNIT_Z);

            Vector3f positionVector = Vector3f.UNIT_Y.mult(2.5f);
            q.multLocal(positionVector);
            geom.move(positionVector);

            target.getParent().attachChild(geom);
            options.add(geom);
            
            OptionSelectionListener optionSelectedListener = new OptionSelectionListener(this);
            MouseEventControl.addListenersToSpatial(geom, optionSelectedListener);
            app.getStateManager().attach(optionSelectedListener);
        }
    }

    @Override
    boolean isReadyToSelect() {
        boolean ready = target == null;
        // System.out.println("Ready? " + ready);
        return ready;
    }

    public void removeOptions() {
        for (Geometry option : options) {
            option.removeFromParent();
        }
        options.clear();
        target = null;
    }
}
