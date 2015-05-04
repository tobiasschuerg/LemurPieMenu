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
 * Listener for handling object selection.
 *
 * @author Tobias Schürg
 */
public abstract class OptionSelectionListener extends DefaultCursorListener {

    private final PieMenu pieMenu;
    private boolean isOptionSelected;
    private Geometry currentlyActive;

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
            System.out.println("Closest: " + currentlyActive.getName());
            // onSpatialSelected(currentlyActive);
        }
    }

    @Override
    public void cursorMoved(CursorMotionEvent event, Spatial target, Spatial capture) {
        Vector3f point = event.getCollision().getContactPoint();
        // processPoint(point);
    }

    @Override
    public void cursorExited(CursorMotionEvent event, Spatial target, Spatial capture) {
        clearGowing();
        // pieMenu.close();
    }

    private void onSpatialSelected(Spatial spatial) {
        if (!isOptionSelected) { // to only trigger selection once!
            isOptionSelected = true;
            onOptionSelected(spatial.getName());
            pieMenu.close();
        }
    }

    private Geometry findClosestOption(Vector3f point) {
        List<Geometry> options = pieMenu.getOptions();

        float closestDistance = 1000;
        Geometry closest = null;

        if (!pieMenu.isOpen()) {
            return null;
        }

        for (Geometry option : options) {
            float distance = point.distance(option.getWorldTranslation());
            if (closest == null || distance < closestDistance) {
                closest = option;
                closestDistance = distance;
            }
        }
        return closest;
    }

    abstract public void onOptionSelected(String name);

    private void highlightOption(Geometry closestOption) {
        if (currentlyActive != null && currentlyActive != closestOption) {
            clearGowing();
        }
        setActive(closestOption);
    }

    public Geometry processPoint(Vector3f center) {
        Geometry option = findClosestOption(center);
        if (option != null) {
            highlightOption(option);
        }
        return option;
    }

    /**
     * removes glowing from geometry.
     */
    private void clearGowing() {
        // remove glowing
        currentlyActive.getMaterial().clearParam("GlowColor");
    }

    /**
     * adds glowing to geometry.
     *
     * @param closestOption
     */
    private void setActive(Geometry closestOption) {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.Yellow);
        closestOption.addLight(ambientLight);
        closestOption.getMaterial().setColor("GlowColor", ColorRGBA.Yellow);
        currentlyActive = closestOption;
    }
}
