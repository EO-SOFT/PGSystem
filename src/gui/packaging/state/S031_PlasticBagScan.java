/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.state;

import helper.Helper;
import entity.BaseEngineLabel;
import entity.ConfigBarcode;
import helper.HQLHelper;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTextField;
import org.hibernate.Query;

/**
 *
 * @author ezou1001
 */
public class S031_PlasticBagScan implements State {

    private ImageIcon imgIcon = new ImageIcon(Helper.PROP.getProperty("IMG_PATH") + "S031_EngineLabelScan.jpg");

    //
    private int numberOfPatterns = 0;
    //
    private int patternIndex = 0;

    public S031_PlasticBagScan(int numberOfPatterns, String[] patternList) {
        this.numberOfPatterns = numberOfPatterns;
        Helper.Packaging_Gui.setIconLabel(this.imgIcon);
        //Reload container table content
        Helper.Packaging_Gui.reloadDataTable();

        //this.loadPlasticBagPattern();

        if (this.numberOfPatterns != 0 && this.patternIndex == 0) {
            Helper.Packaging_Gui.setRequestedPallet_txt(String.format("Scanner le code à barre sachet N° %d / %d ", this.patternIndex + 1, this.numberOfPatterns));
        }

    }

    public void loadPlasticBagPattern() {
        //PLASTICBAG_BARCODE_PATTERN_LIST
        System.out.println("Loading PLASTICBAG_BARCODE_PATTERN_LIST pattern list ... ");
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_HARNESSPART);
        query.setParameter("harnessPart", Helper.context.getBaseContainerTmp().getHarnessPart().substring(1));
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        System.out.println(String.format("%d pattern found for part number %s ", query.list().size(), Helper.context.getBaseContainerTmp().getHarnessPart().substring(1)));
        if (query.list().size() != 0) {
            Helper.PLASTICBAG_BARCODE_PATTERN_LIST = new String[query.list().size()];
            //Allouer de l'espace pour la liste des code à barre getBaseEngineLabelTmp
            Helper.context.getBaseEngineLabelTmp().setLabelCode(new String[query.list().size()]);
            int i = 0;
            for (Iterator it = resultList.iterator(); it.hasNext();) {
                this.numberOfPatterns++;
                ConfigBarcode object = (ConfigBarcode) it.next();
                Helper.PLASTICBAG_BARCODE_PATTERN_LIST[i] = object.getBarcodePattern();
                i++;

            }
            System.out.println(String.format("PLASTICBAG_BARCODE_PATTERN_LIST %d pattern(s) successfuly loaded 100% ! ", numberOfPatterns));
        }else{//No pattern found ! Allez directement au choix de la palette !
            this.doAction(new Context());
            
            S040_PalletChoice state = new S040_PalletChoice();
            Helper.context.setState(state);            
        }
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
            //Boucler sur le nombre des codes à barre pour cette référence.
            if (this.patternIndex + 1 < this.numberOfPatterns) {
                this.clearScanBox(scan_txtbox);
                Helper.context.getBaseEngineLabelTmp().setLabelCode(this.patternIndex, engineLabel);
                Helper.log.info("First Valid Engine Label scanned [" + engineLabel + "] OK.");
                this.patternIndex++;
                Helper.Packaging_Gui.setRequestedPallet_txt(String.format("Scanner le code à barre sachet N° %d / %d ", this.patternIndex + 1, this.numberOfPatterns));
            } else { // Touts les patternes se sont scannés
                Helper.context.getBaseEngineLabelTmp().setLabelCode(this.patternIndex, engineLabel);
                Helper.Packaging_Gui.setRequestedPallet_txt("");
                this.clearScanBox(scan_txtbox);
                S040_PalletChoice state = new S040_PalletChoice();
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
        Helper.Packaging_Gui.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
