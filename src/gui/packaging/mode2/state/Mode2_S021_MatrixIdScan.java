/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __run__.Global;
import gui.packaging.Mode2_Context;
import helper.Helper;
import entity.BaseHarness;
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
public class Mode2_S021_MatrixIdScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(Global.APP_PROP.getProperty("IMG_PATH") + "S03_QRCodeScan.jpg");

    //Combien de patterns de scane configurer pour ce part number.
    private int numberOfPatterns = 0;

    public Mode2_S021_MatrixIdScan() {
        //Set Image
        Helper.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);
        //Reload container table content
        Helper.Packaging_Gui_Mode2.reloadDataTable();
    }

    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui_Mode2.getScanTxt();
        String counter = scan_txtbox.getText().trim();
        BaseHarness bh = new BaseHarness();
        System.out.println("Harness part demander " + context.getBaseContainerTmp().getHarnessPart());
        //Tester le format et l'existance et si le counter concerne ce harness part
        if (!bh.checkDataMatrixFormat(counter)) {//Problème de format
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0005_HP_COUNTER_FORMAT, counter), "Counter error !", ERROR_MESSAGE);
            System.out.println(String.format(Helper.ERR0005_HP_COUNTER_FORMAT, counter));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else if (bh.isCounterExist(counter)) {//Problème de doublant            
            bh = bh.getHarnessByCounter(counter);
            JOptionPane.showMessageDialog(null, String.format(Helper.INFO0004_HP_COUNTER_FOUND, counter, bh.getPalletNumber()), "Counter error !", ERROR_MESSAGE);
            System.out.println(String.format(Helper.INFO0004_HP_COUNTER_FOUND, counter, bh.getPalletNumber()));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);

            // Problème de non conformité avec le harness part courrant          
            //Format 1 without prefix : 5101C861C;05.10.2017;10:05:01 
            //Format 2 with prefix P : P5101C861C;05.10.2017;10:05:01
            //Format 3 Harness Part with prefix P but removed to be compared with QR Code without P 5101C861C;05.10.2017;10:05:01
        } else if (!counter.startsWith(context.getBaseContainerTmp().getHarnessPart()) 
                && !counter.startsWith(Global.HARN_COUNTER_PREFIX + context.getBaseContainerTmp().getHarnessPart())
                && !counter.startsWith(context.getBaseContainerTmp().getHarnessPart().substring(1)) 
                ) {
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0009_COUNTER_NOT_MATCH_HP, counter, context.getBaseContainerTmp().getHarnessPart()), "Counter error !", ERROR_MESSAGE);
            System.out.println(String.format(Helper.ERR0009_COUNTER_NOT_MATCH_HP, counter, context.getBaseContainerTmp().getHarnessPart()));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else {//BINGO !! Matrix Id correct
            Helper.mode2_context.getBaseContainerTmp().setHernessCounter(counter);
            Helper.log.info("Valid Harness Counter scanned [" + counter + "] OK.");

            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //####################### Go to Palette Scann ###########################
            Global.PLASTICBAG_BARCODE_PATTERN_LIST = loadPlasticBagPattern();
            if (Global.PLASTICBAG_BARCODE_PATTERN_LIST.length != 0) {
                Mode2_S022_PlasticBagScan state = new Mode2_S022_PlasticBagScan(this.numberOfPatterns, Global.PLASTICBAG_BARCODE_PATTERN_LIST);
                context.setState(state);
            } else {
                // Pas de scanne de code à barre intermidiaire, passer
                // directement au choix de la palette.
                Mode2_S031_PalletChoice state = new Mode2_S031_PalletChoice();
                context.setState(state);
            }
        }

    }

    /**
     * Charger les
     * @return patterns de scanne configurer pour ce part number.
     *
     */
    public String[][] loadPlasticBagPattern() {

        //PLASTICBAG_BARCODE_PATTERN_LIST
        String PN = "";
        
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_HARNESSPART);
        if(Helper.mode2_context.getBaseContainerTmp().getHarnessPart().startsWith(Global.HARN_PART_PREFIX))
            PN = Helper.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1);
        else
            PN = Helper.mode2_context.getBaseContainerTmp().getHarnessPart();
        
        System.out.println("Loading Additional barecodes pattern list for PN %s... "+PN);
        query.setParameter("harnessPart", PN);
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        System.out.println(String.format("%d pattern found for part number %s ", query.list().size(), PN));
        if (!query.list().isEmpty()) {
            Global.PLASTICBAG_BARCODE_PATTERN_LIST = new String[query.list().size()][2];
            //Allouer de l'espace pour la liste des code à barre getBaseHarnessAdditionalBarecodeTmp
            Helper.mode2_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(new String[query.list().size()]);
            int i = 0;
            for (Iterator it = resultList.iterator(); it.hasNext();) {
                this.numberOfPatterns++;
                ConfigBarcode config = (ConfigBarcode) it.next();
                Global.PLASTICBAG_BARCODE_PATTERN_LIST[i][0] = config.getBarcodePattern();
                Global.PLASTICBAG_BARCODE_PATTERN_LIST[i][1] = config.getDescription();
                i++;
            }
            
            System.out.println("PLASTICBAG_BARCODE_PATTERN_LIST "+this.numberOfPatterns+" pattern(s) successfuly loaded 100% ! ");
        }
        else{
            Global.PLASTICBAG_BARCODE_PATTERN_LIST = new String[0][0];
            
        }
        //No pattern found ! Retourner une liste vide.
        return Global.PLASTICBAG_BARCODE_PATTERN_LIST;
    }

    public String toString() {
        return "State S03 : S03_QRCodeScan";
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
