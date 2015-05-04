package tobiasschuerg.lemur.piemenu.example;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import tobiasschuerg.lemur.piemenu.AbstractPieMenuCallback;
import tobiasschuerg.lemur.piemenu.PieMenu;

/**
 * test
 *
 * @author Tobias Schürg
 */
public class PieMenuExample extends SimpleApplication {

    public static void main(String[] args) {
        PieMenuExample app = new PieMenuExample();
        app.start();
    }
    private Geometry box;
    private PieMenu pieMenu;

    public PieMenuExample() {
        super(new StatsAppState(), new CameraMovementState(), new CameraToggleState());
    }

    @Override
    public void simpleInitApp() {
        // Initialize Lemur subsystems and setup the default
        // camera controls.
        GuiGlobals.initialize(this);
        CameraMovementFunctions.initializeDefaultMappings(GuiGlobals.getInstance().getInputMapper());

        Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot.move(0f, -0.25f, 0f);
        rootNode.attachChild(teapot);
        setUpPieMenu(teapot);

        Box b = new Box(0.5f, 0.5f, 0.5f);
        box = new Geometry("box", b);
        Material m = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Red);
        box.setMaterial(m);
        rootNode.attachChild(box);

        box.move(-4f, -4f, 0f);


        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

    }

    @Override
    public void simpleUpdate(float tpf) {
        tpf = tpf * 0.3f;

        box.move(tpf, tpf, 0f);

        if (pieMenu != null) {
            Geometry selection = pieMenu.checkSelection(box);
            if (selection != null) {
                System.out.println("got " + selection.getName());
            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void setUpPieMenu(Spatial spatial) {
        pieMenu = new PieMenu(this, spatial); // create a new pieMenu

        pieMenu.setCallback(new AbstractPieMenuCallback() {
            @Override
            public void onOptionSelected(String name) {
                System.out.println("Selected option is: " + name);
            }
        });

        pieMenu.addOption("PLACE", "interface/icons/increase10.png");
        pieMenu.addOption("MAGNET", "interface/icons/left3.png");

        pieMenu.addOption("SCALE", "interface/icons/circle110.png");

        pieMenu.addOption("ROTATE_Y", "interface/icons/refresh57.png");
        // pieMenu.addOption("ROTATE_HANDLE", "interface/icons/empty40.png");

        pieMenu.addOption("CLOSE", "interface/icons/delete31.png");
    }
}
