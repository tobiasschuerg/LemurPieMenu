/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.lemur.piemenu;

import com.jme3.scene.Spatial;

/**
 *
 * @author Tobias
 */
public class OptionSelectionListener extends SpatialSelectionListener {
    private final PieMenu pieMenu;

    OptionSelectionListener(PieMenu menu) {
        this.pieMenu = menu;
    }

    @Override
    public void onSpatialSelected(Spatial spatial) {
        // onOptionSelected();
        pieMenu.removeOptions();
    }

    @Override
    boolean isReadyToSelect() {
        return true;
    }
    
}
