/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __run__.Global;
import gui.packaging.Mode2_Context;
import helper.Helper;
import helper.PrinterHelper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecode;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.BaseHarness;
import entity.HisBaseHarness;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ezou1001
 */
public class Mode2_S030_NewPalletScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(Global.APP_PROP.getProperty("IMG_PATH") + "S041_NewPaletScan.jpg");

    public Mode2_S030_NewPalletScan() {

        Helper.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);

        //Reload Data Table to display new pallet
        Helper.Packaging_Gui_Mode2.reloadDataTable();

        Helper.Packaging_Gui_Mode2.setAssistanceTextarea(
                "Scanner la fiche Ouverture Palette N° "
                + Helper.mode2_context.getBaseContainerTmp().getPalletNumber());
    }

    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui_Mode2.getScanTxt();
        String barcode = scan_txtbox.getText().trim();

        System.out.println("Scanned Pallet Number " + barcode);

        if (barcode.equals(Helper.mode2_context.getBaseContainerTmp().getPalletNumber())) {
            Helper.log.info(String.format(Helper.INFO0008_CORRECT_PALLET_NUMBER, barcode));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);

            //##############################################################
            //Inserer les données de la nouvelle palette dans les 2 tables BaseContainer et BaseHarness            
            BaseContainer bc = new BaseContainer().setDefautlVals();
            bc.setPalletNumber(context.getBaseContainerTmp().getPalletNumber());
            bc.setHarnessPart(context.getBaseContainerTmp().getHarnessPart().substring(1));
            bc.setHarnessIndex(context.getBaseContainerTmp().getHarnessIndex());
            bc.setSupplierPartNumber(context.getBaseContainerTmp().getSupplierPartNumber());
            bc.setQtyExpected(context.getBaseContainerTmp().getQtyExpected());
            bc.setPackType(context.getBaseContainerTmp().getPackType());
            bc.setQtyRead(1);
            bc.setHarnessType(context.getBaseContainerTmp().getHarnessType());
            bc.setStdTime(context.getBaseContainerTmp().getStdTime());
            bc.setContainerState(Global.PALLET_OPEN);
            bc.setContainerStateCode(Global.PALLET_OPEN_CODE);
            bc.setPackWorkstation(Global.APP_HOSTNAME);
            //bc.setAssyWorkstation(mode2_context.getBaseContainerTmp().getAssyWorkstation());
            bc.setSegment(context.getBaseContainerTmp().getSegment());
            bc.setWorkplace(context.getBaseContainerTmp().getWorkplace());
            bc.setUcsId(context.getBaseContainerTmp().getUcsId());
            bc.setUcsLifes(context.getBaseContainerTmp().getUcsLifes());
            bc.setComment(context.getBaseContainerTmp().getComment());
            bc.setOrder_no(context.getBaseContainerTmp().getOrder_no());
            bc.setSpecial_order(context.getBaseContainerTmp().getSpecial_order());
            bc.create(bc);
            Helper.log.info(String.format("BaseContainer created %s ", bc.toString()));
            //##############################################################            

            //##############################################################
            //Inserer les données du BaseHarness
            BaseHarness bh = new BaseHarness().setDefautlVals();
            bh.setHarnessPart(context.getBaseContainerTmp().getHarnessPart());
            bh.setCounter(context.getBaseContainerTmp().getHernessCounter());
            bh.setPalletNumber(context.getBaseContainerTmp().getPalletNumber());
            bh.setHarnessType(context.getBaseContainerTmp().getHarnessType());
            bh.setStdTime(context.getBaseContainerTmp().getStdTime());
            bh.setPackWorkstation(Global.APP_HOSTNAME);
            bh.setAssyWorkstation(context.getBaseContainerTmp().getAssyWorkstation());
            bh.setSegment(context.getBaseContainerTmp().getSegment());
            bh.setWorkplace(context.getBaseContainerTmp().getWorkplace());
            bh.setContainer(bc);

            //Insert the harness into the container
            bc.getHarnessList().add(bh);
            //##############################################################

            //##############################################################
            //Save harness History Line
            HisBaseHarness hbh = new HisBaseHarness().parseHarnessData(bh, "New harness added to pallet.");
            hbh.create(hbh);
            //##############################################################

            //############### SET & SAVE ENGINE LABEL DATA #################       
            //if (Helper.context.getUser().getHarnessType().equals(Helper.ENGINE)) {
            if(Helper.mode2_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode().length != 0)
                for (String labelCode : Helper.mode2_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode()) {
                    BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();
                    bel.setDefautlVals();
                    bel.setLabelCode(labelCode);
                    bel.setHarness(bh);
                    bel.create(bel);
                }       
            //}
            //##############################################################       
            //}
            //##############################################################

            //Close connection 
            //Clear session vals in mode2_context
            clearContextSessionVals();

            //Clear requested pallet txt box
            clearRequestedPallet_txt();

            //############## Check if pallet should be closed ##############
            //############## UCS Contains just 1 harness ###################
            if (bc.getQtyExpected() == 1) {

                PrinterHelper.saveAndPrintClosingSheet(bc, false);
                Helper.startSession();
                bc.setContainerState(Global.PALLET_WAITING);
                bc.setContainerStateCode(Global.PALLET_WAITING_CODE);
                bc.update(bc);                 

                context.getBaseContainerTmp().setPalletNumber(bc.getPalletNumber());
                Helper.Packaging_Gui_Mode2.setAssistanceTextarea(
                        "N° "
                        + Global.CLOSING_PALLET_PREFIX + Helper.mode2_context.getBaseContainerTmp().getPalletNumber());

                context.setState(new Mode2_S040_ClosingPallet());
            } else {
                // Change go back to state HarnessPartScan            
                context.setState(new Mode2_S020_HarnessPartScan());
            }
        } else {
            Helper.log.warning(Mode2_S030_NewPalletScan.class.getSimpleName()+String.format(Helper.ERR0008_INCORRECT_PALLET_NUMBER, barcode));
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0008_INCORRECT_PALLET_NUMBER, barcode), "ERR0008 Invalid Barcode", JOptionPane.ERROR_MESSAGE);
            clearScanBox(scan_txtbox);
            context.setState(this);
        }
    }

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid
        Helper.mode2_context.setBaseContainerTmp(new BaseContainerTmp());
        Helper.mode2_context.setBaseHarnessAdditionalBarecodeTmp(new BaseHarnessAdditionalBarecodeTmp());
    }

    public String toString() {
        return "State S041 : S041_NewPaletScan";
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

    public void clearRequestedPallet_txt() {
        Helper.Packaging_Gui_Mode2.setAssistanceTextarea("");
    }

}
