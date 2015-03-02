/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Spatial;

/**
 *
 * @author Tobias
 */
public class OptionSelectionListener extends SpatialSelectionListener {

    @Override
    public void onSpatialSelected(Spatial spatial) {
        spatial.removeFromParent();
    }

    @Override
    boolean isReadyToSelect() {
        return true;
    }
    
}
