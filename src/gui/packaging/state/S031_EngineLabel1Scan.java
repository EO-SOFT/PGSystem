/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.state;

import helper.Helper;
import entity.BaseEngineLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTextField;

/**
 *
 * @author ezou1001
 */
public class S031_EngineLabel1Scan implements State {

    private ImageIcon imgIcon = new ImageIcon(Helper.PROP.getProperty("IMG_PATH") + "S031_EngineLabel1Scan.jpg");

    public S031_EngineLabel1Scan() {
        Helper.Packaging_Gui.setIconLabel(this.imgIcon);
        //Reload container table content
        Helper.Packaging_Gui.reloadDataTable();
    }

    public void doAction(Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui.getScanTxt();
        String engineLabel = scan_txtbox.getText().trim();
        BaseEngineLabel bel = new BaseEngineLabel();
        
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
            Helper.context.getBaseEngineLabelTmp().setLabelCode(0, engineLabel);
            Helper.log.info("First Valid Engine Label scanned [" + engineLabel + "] OK.");
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            System.out.println("Helper.context.getLabelCount() "+Helper.context.getLabelCount());
             //Increase label scan counter
            Helper.context.setLabelCount(Helper.context.getLabelCount() + 1);
            //Passer à l état 4. choix entre nouvelle palette ou palette ouverte.            
            S032_EngineLabel2Scan state = new S032_EngineLabel2Scan();
            context.setState(state);
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
        Helper.Packaging_Gui.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
