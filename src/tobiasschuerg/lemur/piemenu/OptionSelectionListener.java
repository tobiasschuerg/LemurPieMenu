/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.input.MouseInput;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.CursorButtonEvent;
import com.simsilica.lemur.event.CursorMotionEvent;
import com.simsilica.lemur.event.DefaultCursorListener;
import java.util.List;

/**
 *
 * @author Tobias
 */
public class OptionSelectionListener extends DefaultCursorListener {

    private final PieMenu pieMenu;
    private boolean isOptionSelected;
    private Geometry previousClosest;

    OptionSelectionListener(PieMenu menu) {
        this.pieMenu = menu;
        isOptionSelected = false;
    }

    @Override
    public void cursorButtonEvent(CursorButtonEvent event, Spatial target, Spatial capture) {
        if (event.getButtonIndex() != MouseInput.BUTTON_LEFT) {
            return;
        }

        if (!event.isPressed()) {
            System.out.println("Selected: " + closest.getName());
            pieMenu.close();
        }
    }

    @Override
    public void cursorMoved(CursorMotionEvent event, Spatial target, Spatial capture) {
        Vector3f point = event.getCollision().getContactPoint();

        Geometry closest = findClosestOption(point);
        // closest.getMaterial().setColor("Color", ColorRGBA.randomColor());

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.Yellow);
        closest.addLight(ambientLight);
        closest.getMaterial().setColor("GlowColor", ColorRGBA.Yellow);

        if (previousClosest != null && previousClosest != closest) {
            // remove glowing
            previousClosest.getMaterial().clearParam("GlowColor");
        }
        previousClosest = closest;


    }

    @Override
    public void cursorExited(CursorMotionEvent event, Spatial target, Spatial capture) {
        // pieMenu.close();
    }

    // @Override
    public void onSpatialSelected(Spatial spatial) {
        if (!isOptionSelected) { // to only trigger selection once!
            isOptionSelected = true;
            System.out.println("Selected: " + spatial.getName());
            // onOptionSelected();
            pieMenu.close();
        }
    }
    private Geometry closest;

    private Geometry findClosestOption(Vector3f point) {
        List<Geometry> options = pieMenu.getOptions();

        float closestDistance = 1000;

        for (Geometry option : options) {
            float distance = point.distance(option.getWorldTranslation());
            if (closest == null || distance < closestDistance) {
                closest = option;
                closestDistance = distance;
            }
        }
        return closest;
    }
}
