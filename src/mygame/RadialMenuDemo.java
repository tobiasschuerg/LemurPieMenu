package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.MouseEventControl;

/**
 * test
 *
 * @author normenhansen
 */
public class RadialMenuDemo extends SimpleApplication {

    public static void main(String[] args) {
        RadialMenuDemo app = new RadialMenuDemo();
        app.start();
    }

    public RadialMenuDemo() {
        super(new StatsAppState());

    }

    @Override
    public void simpleInitApp() {
        // Initialize Lemur subsystems and setup the default
        // camera controls.   
        GuiGlobals.initialize(this);
        CameraMovementFunctions.initializeDefaultMappings(GuiGlobals.getInstance().getInputMapper());


        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);


        SpatialSelectionListener selectionListener = new PieMenu();
        MouseEventControl.addListenersToSpatial(geom, selectionListener);
        getStateManager().attach(selectionListener);
    }

    @Override
    public void simpleUpdate(float tpf) {
     
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
