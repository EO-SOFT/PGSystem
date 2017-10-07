/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.state;

import helper.Helper;
import helper.PrinterHelper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecode;
import entity.BaseHarness;
import gui.packaging.PACKAGING_UI9000_ChoosePackType;
import helper.HQLHelper;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.hibernate.Query;

/**
 *
 * @author ezou1001
 */
public class S040_PalletChoice implements State {

    private ImageIcon imgIcon = new ImageIcon(Helper.PROP.getProperty("IMG_PATH") + "S040_PaletChoice.jpg");

    public S040_PalletChoice() {
        Helper.Packaging_Gui.setIconLabel(this.imgIcon);
        //Reload container table content
        Helper.Packaging_Gui.reloadDataTable();
    }

    public void doAction(Context context) {
        JTextField scan_txtbox = Helper.Packaging_Gui.getScanTxt();
        String barcode = scan_txtbox.getText().trim();

        //Textbox is not empty
        if (!barcode.isEmpty()) {
            BaseContainer bc = new BaseContainer().getBaseContainer(barcode);

            //######################### OPEN NEW PALLET ########################
            Helper.log.info("Is it a new pallet ?");
            if (barcode.equals(Helper.OPEN_PALLET_PATTERN)) {//NEWP barcode
                Helper.log.info(" [Yes]");

                //Vide le scan box
                this.clearScanBox(scan_txtbox);
                //Afficher le popup du choix du type contenaire du harness_part
                if (Helper.context.getBaseContainerTmp().getHarnessPart().startsWith(Helper.HARN_PART_PREF)) {
                    new PACKAGING_UI9000_ChoosePackType(null, true, context.getBaseContainerTmp().getHarnessPart().substring(1));
                } else {
                    new PACKAGING_UI9000_ChoosePackType(null, true, context.getBaseContainerTmp().getHarnessPart());
                }

            } //####################################################
            else if (!bc.getPackWorkstation().equals(Helper.HOSTNAME)) {
                Helper.log.warning(String.format(Helper.ERR0025_WORKSTATION_PALLET, Helper.HOSTNAME, bc.getPackWorkstation()));
                JOptionPane.showMessageDialog(null, String.format(Helper.ERR0025_WORKSTATION_PALLET, Helper.HOSTNAME, bc.getPackWorkstation()), "Invalid Workstation", JOptionPane.ERROR_MESSAGE);
            } //# 1- If container exist 
            //# 2- State is Open
            //# 3- Max Quantity not reached
            //# 4- Container Harness Part = Context Harness Part  
            //# 5- Container Harness Type = Context Harness Type
            else if (bc != null
                    && bc.getContainerState().equals(Helper.PALLET_OPEN)
                    && bc.getQtyRead() < bc.getQtyExpected()
                    && bc.getHarnessPart().equals(Helper.context.getBaseContainerTmp().getHarnessPart().substring(1))
                    && bc.getHarnessType().equals(Helper.context.getBaseContainerTmp().getHarnessType())) {

                Helper.log.info(" [No]");
                Helper.log.info("Pallet values ");
                Helper.log.info(String.format("State           :   [%s]", bc.getContainerState().equals(Helper.PALLET_OPEN)));
                Helper.log.info(String.format("Qty Expected    :   [%s]", bc.getQtyExpected()));
                Helper.log.info(String.format("Qty Read        :   [%s]", bc.getQtyRead()));
                Helper.log.info(String.format("Std Time        :   [%s]", bc.getStdTime()));
                Helper.log.info(String.format("Harness Part [%s] = Context Harness Part [%s]",
                        bc.getHarnessPart(),
                        Helper.context.getBaseContainerTmp().getHarnessPart().substring(1)));
                Helper.log.info(String.format("Harness Type [%s] = Context Harness Type [%s]",
                        bc.getHarnessType(), Helper.context.getBaseContainerTmp().getHarnessType()));

                Helper.sess.beginTransaction();
                Helper.sess.persist(bc);
                bc.setWriteId(Helper.context.getUser().getId());
                bc.setFifoTime(Helper.getTimeStamp(null));
                bc.setHarnessType(context.getBaseContainerTmp().getHarnessType());

                //#################### SET HARNESS DATA  #######################                                
                //- Set harness data from current context.                
                BaseHarness bh = new BaseHarness().setDefautlVals();
                bh.setHarnessPart(context.getBaseContainerTmp().getHarnessPart());
                bh.setCounter(context.getBaseContainerTmp().getHernessCounter());
                bh.setPalletNumber(barcode);
                bh.setHarnessType(context.getBaseContainerTmp().getHarnessType());
                bh.setStdTime(bc.getStdTime());
                bh.setPackWorkstation(Helper.HOSTNAME);
                bh.setSegment(bc.getSegment());
                bh.setWorkplace(bc.getWorkplace());
                bh.setContainer(bc);
                //##############################################################

                //############### SET & SAVE ALL ENGINE LABELS DATA #################     
                //Si ce part number contient des code à barre pour sachet
                if (Helper.context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode().length != 0) {
                    for (String labelCode : Helper.context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode()) {
                        BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();
                        bel.setDefautlVals();
                        bel.setLabelCode(labelCode);
                        bel.setHarness(bh);
                        bel.create(bel);
                    }
                }
                //##############################################################

                //############## ADD THE HARNESS TO THE CONTAINER ##############    
                //Insert the harness into the container
                bc.getHarnessList().add(bh);

                int newQty = bc.getQtyRead() + 1;
                //Incrémenter la taille du contenaire                
                Query query = Helper.sess.createQuery(HQLHelper.SET_CONTAINER_QTY_READ);
                query.setParameter("qtyRead", newQty);
                query.setParameter("id", bc.getId());
                query.executeUpdate();

                //bc.update(bc); 
                clearScanBox(scan_txtbox);
                //##############################################################

                //####### CHECK IF THE HARNESS EXISTS IN THE DROP TABLE ########
                //Yes                
                System.out.println(String.format("Harness %s to be removed from drop table ", context.getBaseContainerTmp().getHernessCounter()));
                query = Helper.sess.createQuery("DELETE DropBaseHarness WHERE counter = :COUNTER");
                query.setParameter("COUNTER", bh.getCounter());

                int result = query.executeUpdate();
                System.out.println("Deletion result %s " + result);
                //##############################################################

                //- Set harness data from drop table 
                //- Remouve it from drop table (use a flag var to drop it in the end 
                // of this condition)
                //- Create a history mouvement line in base_harness history
                //##############################################################
                //############## Check if pallet should be closed ##############
                //############## UCS Contains just 1 harness ###################
                if (bc.getQtyExpected() == newQty || bc.getQtyExpected() == 1) {
                    Helper.log.info(String.format("Quantité terminée %s", bc.toString()));

                    PrinterHelper.saveAndPrintClosingSheet(bc, false);
                    //Helper.startSession();
                    bc.setContainerState(Helper.PALLET_WAITING);
                    bc.setContainerStateCode(Helper.PALLET_WAITING_CODE);
                    bc.update(bc);

                    //Incrémenter la taille du contenaire                
                    query = Helper.sess.createQuery(HQLHelper.SET_CONTAINER_QTY_READ);
                    query.setParameter("qtyRead", newQty);
                    query.setParameter("id", bc.getId());
                    query.executeUpdate();

                    context.getBaseContainerTmp().setPalletNumber(bc.getPalletNumber());
                    //Set requested closing pallet number in the main gui
                    Helper.Packaging_Gui.setRequestedPallet_txt(
                            "N° "
                            + Helper.CLOSE_PAL_PREF + bc.getPalletNumber());
                    context.setState(new S050_ClosingPallet());

                } else { //QtyExpected not reached yet ! Pallet will still open.

                    //Clear session vals in context
                    clearContextSessionVals();
                    // Change go back to state HarnessPartScan                    
                    context.setState(new S020_HarnessPartScan());
                }
            } //############################### INVALID PALLET CODE #############
            else {
                Helper.log.warning(String.format(Helper.ERR0006_INVALID_OPEN_PALLET_BARCODE, barcode));
                JOptionPane.showMessageDialog(null, String.format(Helper.ERR0006_INVALID_OPEN_PALLET_BARCODE, barcode), "Invalid Barcode", JOptionPane.ERROR_MESSAGE);
                //Vide le scan box
                this.clearScanBox(scan_txtbox);
                //Retourner l'état actuel
                context.setState(this);
            }
        } //############################### INVALID PALLET CODE #############
        else {
            Helper.log.warning(String.format(Helper.ERR0006_INVALID_OPEN_PALLET_BARCODE, barcode));
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0006_INVALID_OPEN_PALLET_BARCODE, barcode), "Invalid Barcode", JOptionPane.ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        }
    }

    @Override
    public String toString() {
        return "State S04 : S040_PaletChoice";
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
        Helper.context.setBaseContainerTmp(new BaseContainerTmp());
        Helper.context.setLabelCount(0);
        Helper.PLASTICBAG_BARCODE_PATTERN_LIST = new String[0][];
    }

}
