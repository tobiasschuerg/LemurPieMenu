/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultMouseListener;

/**
 *
 * @author Tobias
 */
public abstract class SpatialSelectionListener extends DefaultMouseListener implements AppState {

    private Geometry preSelected;
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

    @Override
    protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
        Material m = ((Geometry) target).getMaterial();
        m.setColor("Color", ColorRGBA.Red);
        onSpatialSelected(target);
    }

    @Override
    public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {
        preSelected = ((Geometry) target);
        Material m = preSelected.getMaterial();
        m.setColor("Color", ColorRGBA.Yellow);
    }

    @Override
    public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {
        Material m = ((Geometry) target).getMaterial();
        m.setColor("Color", ColorRGBA.Blue);
        preSelected = null;
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
        if (preSelected != null) {
            counter += tpf;

            if (isReadyToSelect() && counter > 2) {
                onSpatialSelected(preSelected);
            }

        } else {
            counter = 0;
        }
    }

    public void render(RenderManager rm) {
    }

    public void postRender() {
    }

    public void cleanup() {
        initialized = false;
    }

    abstract public void onSpatialSelected(Spatial spatial);

    abstract boolean isReadyToSelect();
}
