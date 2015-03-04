/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import tobiasschuerg.lemur.piemenu.example.PieMenuExample;

/**
 *
 * @author Tobias
 */
public class PieMenu extends AbstractAppState {

    private Spatial target;
    private List<Geometry> options = new ArrayList<Geometry>();
    private List<BitmapText> texts = new ArrayList<BitmapText>();
    private float radius = 3f;
    private Geometry disk;
    public Node menu;
    private boolean areOptionsShowing = false;
    private final Application app;
    private final SpatialSelectionListener selectionListener;

    public PieMenu(Application app, Spatial spatial) {
        this.app = app;
        menu = new Node("pie menu");
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

        if (optionCount < 7) {
            degreesperoption = FastMath.PI / (optionCount - 1);
        } else {
            degreesperoption = 2 * FastMath.PI / (optionCount - 1);
        }

        Quaternion q = new Quaternion();
        q.fromAngleAxis(degreesperoption, Vector3f.UNIT_X);

        int i = 0;
        for (Geometry option : options) {


            q.fromAngleAxis(degreesperoption * i++ - FastMath.PI / 2, Vector3f.UNIT_Z);

            Vector3f positionVector = Vector3f.UNIT_Y.mult(radius);
            q.multLocal(positionVector);

            option.move(positionVector);

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
        target = null;
        areOptionsShowing = false;
    }

    public void addOption(String name, String interfaceLogoMonkeyjpg) {

        float length = 0.5f;
        Box b = new Box(length, length, length / 10);
        Geometry geom = new Geometry(name, b);

        Material cube1Mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture cube1Tex = app.getAssetManager().loadTexture(
                "Interface/Logo/Monkey.jpg");
        cube1Mat.setTexture("ColorMap", cube1Tex);
        geom.setMaterial(cube1Mat);




        BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.2f);
        helloText.setText(geom.getName());
        //helloText.setVerticalAlignment(BitmapFont.VAlign.Center);
        //helloText.setAlignment(BitmapFont.Align.Center);
        helloText.setLocalTranslation(geom.getLocalTranslation().add(new Vector3f(-length, 0f, length)));


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

        CursorEventControl.addListenersToSpatial(disk, new OptionSelectionListener(this));
    }
}
