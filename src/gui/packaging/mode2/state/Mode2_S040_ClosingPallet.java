/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __run__.Global;
import gui.packaging.Mode2_Context;
import helper.Helper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.ConfigUcs;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ezou1001
 */
public class Mode2_S040_ClosingPallet implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(Global.APP_PROP.getProperty("IMG_PATH") + "S050_ClosingPallet.jpg");

    public Mode2_S040_ClosingPallet() {

        //Charger l'image de l'état
        Helper.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);

        //Reload Data Table to display new pallet
        Helper.Packaging_Gui_Mode2.reloadDataTable();
    }

    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui_Mode2.getScanTxt();
        String barcode = scan_txtbox.getText().trim();

        if (!barcode.isEmpty() && barcode.equals(Global.CLOSING_PALLET_PREFIX + context.getBaseContainerTmp().getPalletNumber())) {
            //Update pallet state to CLOSED            
            BaseContainer bc = new BaseContainer().getBaseContainer(context.getBaseContainerTmp().getPalletNumber());

            try {
                bc.setContainerState(Global.PALLET_CLOSED);
                bc.setContainerStateCode(Global.PALLET_CLOSED_CODE);
                bc.setClosedTime(Helper.getTimeStamp(null));
                bc.setWorkTime(
                        Float.valueOf(
                                Helper.DiffInMinutes(Helper.getTimeStamp(null),
                                        bc.getCreateTime())
                        )
                );
                
                //Update the UCS Config lifes in ConfigUcs table
                ConfigUcs cs;
                if(bc.getUcsLifes() > 1){
                    cs = (ConfigUcs) new ConfigUcs().select(bc.getUcsId());
                    cs.setLifes(cs.getLifes()-1);
                    cs.update(cs);
                }else if(bc.getUcsLifes() == 1){ //Is the last one
                    cs = (ConfigUcs) new ConfigUcs().select(bc.getUcsId());
                    cs.delete(cs);
                }//else lifes = -1 must still alive

                bc.update(bc);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, e.getMessage() + "\n Pallet number not found " + scan_txtbox.getText(), "Pallet number not found", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage() + "\n Pallet number not found " + scan_txtbox.getText());
            }

            //Clear scann textbox
            clearScanBox(scan_txtbox);

            //Clear session vals in mode2_context
            clearContextSessionVals();

            //Clear requested closing pallet number in the main gui
            Helper.Packaging_Gui_Mode2.setAssistanceTextarea("");

            // Change go back to state HarnessPartScan            
            context.setState(new Mode2_S020_HarnessPartScan());
        } else {
            //Clear scann textbox
            clearScanBox(scan_txtbox);

            Helper.log.warning(String.format(Helper.ERR0011_INVALID_CLOSE_PALLET_BARCODE, barcode));
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0011_INVALID_CLOSE_PALLET_BARCODE, barcode), "Invalid Close Barcode", JOptionPane.ERROR_MESSAGE);
            context.setState(this);
        }
    }

    @Override
    public String toString() {
        return "S050_ClosingPallet{" + "imgIcon=" + imgIcon + '}';
    }

    public ImageIcon getImg() {
        return this.imgIcon;
    }

    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        Helper.Packaging_Gui_Mode2.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid
        Helper.mode2_context.setBaseContainerTmp(new BaseContainerTmp());
        Helper.mode2_context.setBaseHarnessAdditionalBarecodeTmp(new BaseHarnessAdditionalBarecodeTmp());
        Helper.mode2_context.setLabelCount(0);
    }

}
