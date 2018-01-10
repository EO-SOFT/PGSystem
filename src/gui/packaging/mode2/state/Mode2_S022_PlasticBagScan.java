/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __run__.Global;
import gui.packaging.Mode2_Context;
import helper.Helper;
import entity.BaseHarnessAdditionalBarecode;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTextField;

/**
 *
 * @author ezou1001
 */
public class Mode2_S022_PlasticBagScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(Global.APP_PROP.getProperty("IMG_PATH") + "S031_EngineLabelScan.jpg");

    //
    private int numberOfPatterns = 0;
    //
    private int patternIndex = 0;

    public Mode2_S022_PlasticBagScan(int numberOfPatterns, String[][] patternList) {
        this.numberOfPatterns = numberOfPatterns;
        Helper.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);
        //Reload container table content
        Helper.Packaging_Gui_Mode2.reloadDataTable();

        //this.loadPlasticBagPattern();

        if (this.numberOfPatterns != 0 && this.patternIndex == 0) {
            Helper.Packaging_Gui_Mode2.setAssistanceTextarea(String.format("Scanner code à barre N° %d / %d. %s ", this.patternIndex + 1, this.numberOfPatterns, patternList[this.patternIndex][1]));
        }

    }


    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui_Mode2.getScanTxt();
        String engineLabel = scan_txtbox.getText().trim();
        BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();

        //Tester le format et l'existance et si le engineLabel concerne ce harness part
        if (!bel.checkLabelFormat(engineLabel)) {//Problème de format
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0015_ENGINE_LABEL_FORMAT, engineLabel), "Counter error !", ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else if (bel.isLabelCodeExist(engineLabel)) {//Problème de doublant            
            JOptionPane.showMessageDialog(null, String.format(Helper.INFO0011_DUPLICAT_ENGINE_LABEL, engineLabel), "Engine Label error !", ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else {//BINGO !! Label Code OK            
            //Boucler sur le nombre des codes à barre pour cette référence.
            if (this.patternIndex + 1 < this.numberOfPatterns) {
                this.clearScanBox(scan_txtbox);
                Helper.mode2_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(this.patternIndex, engineLabel);
                Helper.log.info("First Valid Engine Label scanned [" + engineLabel + "] OK.");
                this.patternIndex++;
                Helper.Packaging_Gui_Mode2.setAssistanceTextarea(String.format("Scanner le code à barre sachet N° %d / %d. %s ", this.patternIndex + 1, this.numberOfPatterns, Global.PLASTICBAG_BARCODE_PATTERN_LIST[this.patternIndex][1]));
            } else { // Touts les patternes se sont scannés
                Helper.mode2_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(this.patternIndex, engineLabel);
                Helper.Packaging_Gui_Mode2.setAssistanceTextarea("");
                this.clearScanBox(scan_txtbox);
                Mode2_S031_PalletChoice state = new Mode2_S031_PalletChoice();
                context.setState(state);
            }           
        }

    }

    @Override
    public String toString() {
        return "S031_EngineLabelScan{" + "imgIcon=" + imgIcon + '}';
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
