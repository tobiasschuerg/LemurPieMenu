package tobiasschuerg.lemur.piemenu.example;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.Styles;
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

    public PieMenuExample() {
        super(new StatsAppState(), new CameraMovementState(), new CameraToggleState());
    }

    @Override
    public void simpleInitApp() {
        // Initialize Lemur subsystems and setup the default
        // camera controls.
        GuiGlobals.initialize(this);
        CameraMovementFunctions.initializeDefaultMappings(GuiGlobals.getInstance().getInputMapper());

        Styles styles = GuiGlobals.getInstance().getStyles();
        styles.getSelector(Slider.THUMB_ID, "glass").set("text", "[]", false);
        styles.getSelector(Panel.ELEMENT_ID, "glass").set("background",
                new QuadBackgroundComponent(new ColorRGBA(0, 0.25f, 0.25f, 0.5f)));
        styles.getSelector(Checkbox.ELEMENT_ID, "glass").set("background",
                new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f)));
        styles.getSelector("spacer", "glass").set("background",
                new QuadBackgroundComponent(new ColorRGBA(1, 0.0f, 0.0f, 0.0f)));
        styles.getSelector("header", "glass").set("background",
                new QuadBackgroundComponent(new ColorRGBA(0, 0.75f, 0.75f, 0.5f)));
        styles.getSelector("header", "glass").set("shadowColor",
                new ColorRGBA(1, 0f, 0f, 1));


        // Now construct some HUD panels in the "glass" style that
        // we just configured above.
        Container hudPanel = new Container("glass");
        hudPanel.setLocalTranslation(5, cam.getHeight() - 20, 0);
        // TODO: temporary guiNode.attachChild(hudPanel);

        // Create a top panel for some stats toggles.
        Container panel = new Container("glass");
        hudPanel.addChild(panel);

        panel.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f), 5, 5, 0.02f, false));
        panel.addChild(new Label("Stats Settings", new ElementId("header"), "glass"));
        panel.addChild(new Panel(2, 2, ColorRGBA.Cyan, "glass")).setUserData(LayerComparator.LAYER, 2);

        // Adding components returns the component so we can set other things
        // if we want.
        Checkbox temp = panel.addChild(new Checkbox("Show Stats"));
        temp.setChecked(true);

        temp = panel.addChild(new Checkbox("Show FPS"));
        temp.setChecked(true);

        Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot.move(0f, -0.25f, 0f);
        rootNode.attachChild(teapot);

        //Box b = new Box(1, 1, 1);
        //Geometry geom = new Geometry("Box", b);

        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        //geom.setMaterial(mat);
        //rootNode.attachChild(geom);
        setUpPieMenu(teapot);

        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void setUpPieMenu(Spatial spatial) {
        PieMenu pieMenu = new PieMenu(this, spatial); // create a new pieMenu

        pieMenu.setCallback(new AbstractPieMenuCallback() {
            @Override
            public void onOptionSelected(String name) {
                System.out.println("Selected option: " + name);
            }
        });

        pieMenu.addOption("RESIZE", "interface/icons/increase10.png");
        pieMenu.addOption("TRANSLATE", "interface/icons/left3.png");
        pieMenu.addOption("ROTATE", "interface/icons/refresh57.png");

//        pieMenu.addOption("SQAURE", "interface/icons/empty40.png");
//        pieMenu.addOption("CIRCLE", "interface/icons/circle110.png");

//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");
//        pieMenu.addOption("EMPTY", "interface/icons/delete31.png");

    }
}
