package tobiasschuerg.lemur.piemenu.example;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.Styles;
import tobiasschuerg.lemur.piemenu.PieMenu;
import tobiasschuerg.lemur.piemenu.SpatialSelectionListener;

/**
 * test
 *
 * @author normenhansen
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
        styles.getSelector( Slider.THUMB_ID, "glass" ).set( "text", "[]", false );
        styles.getSelector( Panel.ELEMENT_ID, "glass" ).set( "background",
                                new QuadBackgroundComponent(new ColorRGBA(0, 0.25f, 0.25f, 0.5f)) );
        styles.getSelector( Checkbox.ELEMENT_ID, "glass" ).set( "background",
                                new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f)) );
        styles.getSelector( "spacer", "glass" ).set( "background",
                                new QuadBackgroundComponent(new ColorRGBA(1, 0.0f, 0.0f, 0.0f)) );
        styles.getSelector( "header", "glass" ).set( "background",
                                new QuadBackgroundComponent(new ColorRGBA(0, 0.75f, 0.75f, 0.5f)) );
        styles.getSelector( "header", "glass" ).set( "shadowColor",
                                                     new ColorRGBA(1, 0f, 0f, 1) );

        
        // Now construct some HUD panels in the "glass" style that
        // we just configured above.
        Container hudPanel = new Container("glass");
        hudPanel.setLocalTranslation( 5, cam.getHeight() - 20, 0 );
        guiNode.attachChild(hudPanel);

        // Create a top panel for some stats toggles.
        Container panel = new Container("glass");
        hudPanel.addChild(panel);

        panel.setBackground(new QuadBackgroundComponent(new ColorRGBA(0,0.5f,0.5f,0.5f),5,5, 0.02f, false));
        panel.addChild( new Label( "Stats Settings", new ElementId("header"), "glass" ) );
        panel.addChild( new Panel( 2, 2, ColorRGBA.Cyan, "glass" ) ).setUserData( LayerComparator.LAYER, 2 );

        // Adding components returns the component so we can set other things
        // if we want.
        Checkbox temp = panel.addChild( new Checkbox( "Show Stats" ) );
        temp.setChecked(true);

        temp = panel.addChild( new Checkbox( "Show FPS" ) );
        temp.setChecked(true);
      
        

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);


        PieMenu pieMenu = new PieMenu(); // create a new pieMenu
        getStateManager().attach(pieMenu); // attach to get access to the update loop
        MouseEventControl.addListenersToSpatial(geom, pieMenu); // add menu to spatial
        rootNode.attachChild(pieMenu.menu);
        
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
