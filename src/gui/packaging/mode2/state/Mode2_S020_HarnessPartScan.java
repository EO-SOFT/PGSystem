/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import helper.Helper;
import entity.BaseHarness;
import entity.ConfigUcs;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ezou1001
 */
public class Mode2_S020_HarnessPartScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(Helper.PROP.getProperty("IMG_PATH") + "S02_HarnessPartScan.jpg");

    public Mode2_S020_HarnessPartScan() {
        
        //Charger l'image de l'état
        Helper.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);

        //Reload container table content
        Helper.Packaging_Gui_Mode2.reloadDataTable();

        //Reload Session Harness Type
        Helper.mode2_context.getBaseContainerTmp().setHarnessType(String.valueOf(Helper.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem()));
    }

    @Override
    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui_Mode2.getScanTxt();
        String harnessPart = scan_txtbox.getText().trim();

        //################## Check harness Part Format #####################
        if (!BaseHarness.checkPartNumberFormat(harnessPart)) {
            Helper.log.warning(String.format(Helper.ERR0003_HP_FORMAT, harnessPart));
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0003_HP_FORMAT, harnessPart), "Invalid Harness Part format !", JOptionPane.ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);

        } //################## Check harness Part in UCS #####################
        else if (!ConfigUcs.isHarnessPartExist(harnessPart, Helper.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem().toString())) {
            Helper.log.warning(String.format(Helper.ERR0016_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessPart, Helper.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem().toString()));
            JOptionPane.showMessageDialog(
                    null,
                    String.format(Helper.ERR0016_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessPart, Helper.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem().toString()), "Invalid Harness Part format !", JOptionPane.ERROR_MESSAGE);

            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } //################## Harness part exist and format OK ##################
        else {//BINGO !!!!!
            Helper.log.info("Harness Part scanned [" + harnessPart + "] OK.");
            Helper.mode2_context.getBaseContainerTmp().setHarnessPart(harnessPart);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);

            //Passer à l état 3. Scan du compteur, code QR
            Mode2_S021_MatrixIdScan state = new Mode2_S021_MatrixIdScan();
            context.setState(state);
        }
    }

    @Override
    public String toString() {
        return "S020_HarnessPartScan{" + "imgIcon=" + imgIcon + '}';
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}