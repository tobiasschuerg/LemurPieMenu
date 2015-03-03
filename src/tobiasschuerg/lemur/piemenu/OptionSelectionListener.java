/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
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
public class OptionSelectionListener extends DefaultCursorListener implements AppState{

    private final PieMenu pieMenu;
    private boolean isOptionSelected;
    
        /**
     * <code>initialized</code> is set to true when the method
     * {@link AbstractAppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application) }
     * is called. When {@link AbstractAppState#cleanup() } is called,
     * <code>initialized</code> is set back to false.
     */
    protected boolean initialized = false;
    private boolean enabled = true;
    private float counter;
    protected Application app;

    OptionSelectionListener(PieMenu menu) {
        this.pieMenu = menu;
        isOptionSelected = false;
    }
    
    @Override
    public void cursorButtonEvent( CursorButtonEvent event, Spatial target, Spatial capture ) {
        if( event.getButtonIndex() != MouseInput.BUTTON_LEFT )
            return;

        if( event.isPressed() ) {
            System.out.println("RAYCAST");
        } else {
            System.out.println("SELECTION");
        }
    }

    @Override
    public void cursorMoved(CursorMotionEvent event, Spatial target, Spatial capture) {
        Vector3f point = event.getCollision().getContactPoint();
        Geometry closest = findClosestOption(point);
    }
    
    

    // @Override
    public void onSpatialSelected(Spatial spatial) {
        if (!isOptionSelected) { // to only trigger selection once!
            isOptionSelected = true;
            System.out.println("Selected: " + spatial.getName());
            // onOptionSelected();
            pieMenu.removeOptions();
        }
    }

        public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        this.app = app;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void stateAttached(AppStateManager stateManager) {
    }

    public void stateDetached(AppStateManager stateManager) {
    }

    public void update(float tpf) {
 
    }

    public void render(RenderManager rm) {
    }

    public void postRender() {
    }

    public void cleanup() {
        initialized = false;
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
        System.out.println("Distance: " + closest.getName());
        return closest;
    }

}
