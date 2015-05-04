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
    private OptionSelectionListener optionSelectionListener = new OptionSelectionListener(this) {
        @Override
        public void onOptionSelected(String name) {
            callback.onOptionSelected(name);
        }
    };

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

            menu.attachChild(option);
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

    public void addOption(String name, ColorRGBA color) {

        Box b = new Box(buttonSize, buttonSize, buttonSize / 10);
        Geometry geom = new Geometry(name, b);
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        geom.setMaterial(material);

        options.add(geom);
    }

    public void addOption(String name, Texture texture) {

        Box b = new Box(buttonSize, buttonSize, buttonSize / 10);
        Geometry geom = new Geometry(name, b);
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", texture);
        geom.setMaterial(material);

        options.add(geom);
    }

    public void addOption(String name, String texturePath) {
        Texture texture = app.getAssetManager().loadTexture(texturePath);
        addOption(name, texture);
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

        CursorEventControl.addListenersToSpatial(disk, optionSelectionListener);
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

    public void setCallback(AbstractPieMenuCallback callback) {
        this.callback = callback;
    }

    public void setButtonSize(float f) {
        this.buttonSize = f;
    }

    public Geometry checkSelection(Geometry box) {
        Vector3f center = box.getWorldTranslation();
        Geometry option = optionSelectionListener.processPoint(center);
        return option;
    }

    boolean isOpen() {
        return areOptionsShowing;
    }
}
