/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultMouseListener;

/**
 *
 * @author Tobias
 */
public abstract class SpatialSelectionListener extends DefaultMouseListener {

    private Spatial preSelected;

    @Override
    protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
        preSelected = target;
        // m.setColor("Color", ColorRGBA.Red);
        onSpatialSelected(target);
    }

    @Override
    public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {
        preSelected = target;
        //Material m = preSelected.getMaterial();
        //m.setColor("Color", ColorRGBA.Yellow);
    }

    @Override
    public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {
        // Material m = ((Geometry) target).getMaterial();
        //m.setColor("Color", ColorRGBA.Blue);
        //preSelected = null;
    }

    abstract public void onSpatialSelected(Spatial spatial);
}
