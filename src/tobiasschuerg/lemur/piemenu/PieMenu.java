package tobiasschuerg.lemur.piemenu;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.MouseEventControl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tobias Schürg
 */
public class PieMenu extends AbstractAppState {

    private List<Geometry> options = new ArrayList<Geometry>();
    private List<BitmapText> texts = new ArrayList<BitmapText>();
    private float radius;
    private Geometry disk;
    public Node menu;
    private boolean areOptionsShowing = false;
    private final Application app;
    private final SpatialSelectionListener selectionListener;
    private AbstractPieMenuCallback callback;
    private float buttonSize = 0.5f;

    public PieMenu(Application app, Spatial spatial) {
        this.app = app;
        menu = new Node("pie menu");
        spatial.getParent().attachChild(menu);
        menu.setLocalTranslation(spatial.getLocalTranslation());
        this.radius = 3f;

        // for the object glowing
        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        app.getViewPort().addProcessor(fpp);

        selectionListener = new SpatialSelectionListener() {
            @Override
            public void onSpatialSelected(Spatial spatial) {

                if (!areOptionsShowing) {
                    showOptions();
                    addSelectionPlane();
                    areOptionsShowing = true;
                } else {
                    close();
                }

            }
        };

        app.getStateManager().attach(this); // attach to get access to the update loop
        MouseEventControl.addListenersToSpatial(spatial, selectionListener); // add menu to spatial
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        menu.lookAt(app.getCamera().getLocation(), app.getCamera().getUp());
    }

    private void showOptions() {
        int optionCount = options.size();
        float degreesperoption;

        float offset;
        if (optionCount < 4) {
            degreesperoption = FastMath.PI / 2 / (optionCount - 1);
            offset = -FastMath.PI / 4;
        } else if (optionCount < 6) {
            degreesperoption = FastMath.PI / (optionCount - 1);
            offset = -FastMath.PI / 2;
        } else {
            degreesperoption = 2 * FastMath.PI / optionCount;
            offset = 0;
        }

        Quaternion q = new Quaternion();
        q.fromAngleAxis(degreesperoption, Vector3f.UNIT_X);

        int i = 0;
        for (Geometry option : options) {

            q.fromAngleAxis(offset + degreesperoption * i++, Vector3f.UNIT_Z);

            Vector3f positionVector = Vector3f.UNIT_Y.mult(getRadius());
            q.multLocal(positionVector);

            option.move(positionVector);
            // option.move(new Vector3f(0f, 0f, 0.5f));


            // option.


            //OptionSelectionListener optionSelectedListener = new OptionSelectionListener(this);
            //MouseEventControl.addListenersToSpatial(geom, optionSelectedListener);
            //app.getStateManager().attach(optionSelectedListener);
            //options.add(geom);
            menu.attachChild(option);
            // menu.attachChild(helloText);
        }
    }

    private void removeOptions() {
        for (Geometry option : options) {
            option.removeFromParent();
            option.setLocalTranslation(new Vector3f());
        }

        for (BitmapText text : texts) {
            text.removeFromParent();
        }
        texts.clear();
    }

    private void removeSelectionPlane() {
        if (disk != null) {
            disk.removeFromParent();
            disk = null;
        }
    }

    public List<Geometry> getOptions() {
        return options;
    }

    void close() {
        removeOptions();
        removeSelectionPlane();
        areOptionsShowing = false;
    }

    public void addOption(String name, String texture) {

        Box b = new Box(buttonSize, buttonSize, buttonSize / 10);
        Geometry geom = new Geometry(name, b);

        Material cube1Mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture cube1Tex = app.getAssetManager().loadTexture(texture);
        cube1Mat.setTexture("ColorMap", cube1Tex);
        geom.setMaterial(cube1Mat);




        // BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        //BitmapText helloText = new BitmapText(guiFont, false);
        //helloText.setSize(0.2f);
        //helloText.setText(geom.getName());
        //helloText.setVerticalAlignment(BitmapFont.VAlign.Center);
        //helloText.setAlignment(BitmapFont.Align.Center);
        //helloText.setLocalTranslation(geom.getLocalTranslation().add(new Vector3f(-3 * buttonSize, 0f, buttonSize)));


        //OptionSelectionListener optionSelectedListener = new OptionSelectionListener(this);
        //MouseEventControl.addListenersToSpatial(geom, optionSelectedListener);
        //app.getStateManager().attach(optionSelectedListener);
        options.add(geom);
    }

    private void addSelectionPlane() {
        removeSelectionPlane();
        Cylinder c = new Cylinder(8, 32, 5f, 0.01f, true);
        disk = new Geometry("disk", c);

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.randomColor());
        disk.setMaterial(mat);
        disk.setCullHint(Spatial.CullHint.Always);
        menu.attachChild(disk);

        CursorEventControl.addListenersToSpatial(disk, new OptionSelectionListener(this) {
            @Override
            public void onOptionSelected(String name) {
                System.out.println("Selected option: " + name);
            }
        });
    }

    /**
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setCallback(AbstractPieMenuCallback abstractPieMenuCallback) {
        this.callback = abstractPieMenuCallback;
    }

    public void setButtonSize(float f) {
        this.buttonSize = f;
    }
}
