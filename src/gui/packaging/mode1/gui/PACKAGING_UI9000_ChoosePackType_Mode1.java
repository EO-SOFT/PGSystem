/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.gui;

import entity.BaseContainer;
import helper.Helper;
import helper.HQLHelper;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.ConfigUcs;
import gui.packaging.mode1.state.Mode1_S020_PalletChoice;
import gui.packaging.mode1.state.Mode1_S041_NewPalletScan;
import helper.PrinterHelper;
import helper.UIHelper;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Administrator
 */
public final class PACKAGING_UI9000_ChoosePackType_Mode1 extends javax.swing.JDialog {

    Vector result_table_data = new Vector();
    Vector<String> ucs_result_table_header = new Vector<String>();
    private List<Object[]> resultList;
    int SPECIAL_COMMANDE_INDEX = 12;
    List<String> table_header = Arrays.asList(
            "ID",
            "CPN",
            "LPN",
            "INDEX",
            "PACK TYPE",
            "STD PACK",
            "STD TIME",
            "WORKSTATION",
            "SEGMENT",
            "WORKPLACE",
            "UCS LIFES",
            "COMMENT",
            "SPECIAL ORDER",
            "ORDER NO"
    );

    /**
     * Creates new form NewJDialog
     */
    public PACKAGING_UI9000_ChoosePackType_Mode1(java.awt.Frame parent, boolean modal, String harness_part) {
        super(parent, modal);
        initComponents();
        initGui();
        System.out.println("HP " + harness_part);
        Helper.startSession();

        //Récupérer la list des données UCS qui corresponds au harness_part
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_ACTIVE);
        query.setParameter("hp", harness_part.trim());

        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pas d'UCS configuré pour le PN " + harness_part);
            //Clear session vals in mode1_context
            clearContextSessionVals();
            // Change go back to state HarnessPartScan                    
            Mode1_S020_PalletChoice state = new Mode1_S020_PalletChoice();
            Helper.mode1_context.setState(state);
        }
        this.reload_result_table_data(result);

        this.setVisible(true);

    }

    public void reset_table_content() {
        result_table_data = new Vector();
        ucs_jtable.setModel(new DefaultTableModel(result_table_data, ucs_result_table_header));
    }

    public void reload_result_table_data(List resultList) {

        for (Object obj : resultList) {
            ConfigUcs c = (ConfigUcs) obj;
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(c.getId())); // ID     
            oneRow.add(String.valueOf(c.getHarnessPart())); // CPN
            oneRow.add(String.valueOf(c.getSupplierPartNumber())); // LPN
            oneRow.add(String.valueOf(c.getHarnessIndex())); // INDEX
            oneRow.add(String.valueOf(c.getPackType())); // PACK TYPE
            oneRow.add(String.valueOf(c.getPackSize())); // STD PACK
            oneRow.add(String.valueOf(c.getStdTime())); // STD TIME
            oneRow.add(String.valueOf(c.getAssyWorkstationName())); // WORKSTATION
            oneRow.add(String.valueOf(c.getSegment())); // SEGMENT                     
            oneRow.add(String.valueOf(c.getWorkplace())); // WORKPLACE    
            oneRow.add(String.valueOf(c.getLifes())); // UCS LIFES  
            oneRow.add(String.valueOf(c.getComment())); // COMMENT  
            oneRow.add(String.valueOf(c.getSpecialOrder())); // SPECIAL ORDER 
            oneRow.add(String.valueOf(c.getOrderNo())); // ORDER NO  

            result_table_data.add(oneRow);
        }
        ucs_jtable.setModel(new DefaultTableModel(result_table_data, ucs_result_table_header));
        ucs_jtable.setFont(new Font(String.valueOf(Helper.PROP.getProperty("JTABLE_FONT")), Font.BOLD, 16));
        ucs_jtable.setRowHeight(Integer.valueOf(Helper.PROP.getProperty("JTABLE_ROW_HEIGHT")));
        setContainerTableRowsStyle();
    }

    public void disableEditingTables() {
        //UIHelper.disableEditingJtable(ucs_jtable);
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            ucs_result_table_header.add(it.next());
        }
    }

    public void setContainerTableRowsStyle() {
        ucs_jtable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                Integer status = Integer.valueOf(table.getModel().getValueAt(row, SPECIAL_COMMANDE_INDEX).toString());
                if (status == 1) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                setHorizontalAlignment(JLabel.CENTER);

                return this;
            }
        });
    }

    private void initGui() {

        //Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        disableEditingTables();

        //Load table header
        load_table_header();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ucs_jtable = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        setModal(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Choisissez le type du packaging");

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("puis appuyer sur");

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("ENTRER");

        ucs_jtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        ucs_jtable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ucs_jtableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(ucs_jtable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(444, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addGap(352, 352, 352))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mappingValsToContext() {
        //58	22216200	26C06970A	P01	2RV	120	0.377	SMALL_SB	SMALLS_MDEP	SMALL	-1	null	false	null
        Helper.mode1_context.getBaseContainerTmp().setUcsId(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 0).toString().toString()));
        Helper.mode1_context.getBaseContainerTmp().setHarnessPart(Helper.HARN_PART_PREFIX + (String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 1));
        Helper.mode1_context.getBaseContainerTmp().setSupplierPartNumber((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 2));
        Helper.mode1_context.getBaseContainerTmp().setHarnessIndex((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 3));
        Helper.mode1_context.getBaseContainerTmp().setPackType((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 4));
        Helper.mode1_context.getBaseContainerTmp().setQtyExpected(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 5).toString()));
        Helper.mode1_context.getBaseContainerTmp().setStdTime(Double.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 6).toString()));
        Helper.mode1_context.getBaseContainerTmp().setAssyWorkstation((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 7));
        Helper.mode1_context.getBaseContainerTmp().setSegment((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 8));
        Helper.mode1_context.getBaseContainerTmp().setWorkplace((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 9));
        Helper.mode1_context.getBaseContainerTmp().setUcsLifes(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 10).toString()));
        Helper.mode1_context.getBaseContainerTmp().setComment((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 11));
        Helper.mode1_context.getBaseContainerTmp().setSpecial_order(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 12).toString()));
        Helper.mode1_context.getBaseContainerTmp().setOrder_no((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 13));
        Helper.mode1_context.getBaseContainerTmp().setChoosen_pack_type((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 4));

        System.out.println(Helper.mode1_context.getBaseContainerTmp().toString());
    }

    public void loadConfigUcs() {
        Helper.startSession();
        //Getting ConfigUCS data who match the choice criteria
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_AND_SUPPLIER_PART_AND_INDEX_PACKTYPE_AND_PACKSIZE);
        query.setParameter("harnessPart", Helper.mode1_context.getBaseContainerTmp().getHarnessPart().substring(1)); //items[0] = harnessPart
        query.setParameter("supplierPartNumber", Helper.mode1_context.getBaseContainerTmp().getSupplierPartNumber()); //items[1] = supplierPartNumber
        query.setParameter("harnessIndex", Helper.mode1_context.getBaseContainerTmp().getHarnessIndex()); //items[2] = harnessIndex                        
        query.setParameter("packType", Helper.mode1_context.getBaseContainerTmp().getPackType()); //items[3] = packType
        query.setInteger("packSize", Helper.mode1_context.getBaseContainerTmp().getQtyExpected()); //items[4] = packSize

        Helper.sess.getTransaction().commit();
        List result = query.list();
        ConfigUcs configUcs = null;
        try {
            configUcs = (ConfigUcs) result.get(0);
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, String.format("Failed to found UCS matching criteria %s AND %s ",
                    Helper.mode1_context.getBaseContainerTmp().getSupplierPartNumber(), Helper.mode1_context.getBaseContainerTmp().getHarnessIndex()), "Database Query Error!", ERROR_MESSAGE);
        }

        int palletId = -1;
        palletId = PrinterHelper.saveAndPrintOpenSheet(
                Helper.mode1_context,
                configUcs.getHarnessPart(),
                configUcs.getHarnessIndex(),
                configUcs.getSupplierPartNumber(),
                configUcs.getPackType(),
                configUcs.getPackSize(),
                Helper.mode1_context.getUser().getLogin());
        System.out.println("palletId" + palletId);
//        try {
//        
//        } catch (NullPointerException e) {
//            System.out.println("PACKAGING_UI9000_ChooseContainerType Error : " + e.getMessage());
//        }

        if (palletId != -1) {

            //############# PASSE TO S041 STATE ###############
            Helper.log.info(String.format("Openning new container for first harness part [%s].", Helper.mode1_context.getBaseContainerTmp().getHarnessPart().substring(1)));

            Helper.Packaging_Gui_Mode1.setAssistanceTextarea(
                    "Scanner la fiche Ouverture Palette N° "
                    + palletId);

            //Change state to S041
            Mode1_S041_NewPalletScan state = new Mode1_S041_NewPalletScan();
            Helper.mode1_context.setState(state);
            this.dispose();
        }

    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        clearContextSessionVals();
        // Change go back to state HarnessPartScan            
        Helper.mode1_context.setState(new Mode1_S020_PalletChoice());
        Helper.Packaging_Gui_Mode1.setAssistanceTextarea("");
    }//GEN-LAST:event_formWindowClosing

    private void ucs_jtableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ucs_jtableKeyPressed
        System.out.println("ucs_jtableKeyPressed" + evt.getKeyCode());
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_SPACE) {
            // User has pressed Carriage return button
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                //Set mode1_context session vals with the choosen valus after split them
                System.out.println("Selected Row " + ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 0).toString());
                Integer ucs_id = Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 0).toString());
                this.mappingValsToContext();
                List result;
                Query query;
                /**
                 * -------------------------------
                 */
                //Checker si des palettes du même type se sont ouvertes sur
                //la meme poste emballage.                        
                System.out.println("startSession");
                Helper.startSession();
                System.out.println("start ok!");
                //Check s'il n'y a pas une palette ouverte pour la même référence avec le même type packaging.
                query = Helper.sess.createQuery(
                        "FROM BaseContainer bc WHERE "
                        + "bc.harnessPart = :harnessPart AND bc.containerState = :containerState AND bc.packType = :packType");
                query.setParameter("harnessPart", Helper.mode1_context.getBaseContainerTmp().getHarnessPart().substring(1))
                        .setParameter("containerState", Helper.PALLET_OPEN)
                        .setParameter("packType", Helper.mode1_context.getBaseContainerTmp().getPackType());
                Helper.sess.getTransaction().commit();
                result = query.list();

                if (result.isEmpty()) {
                    if (Integer.valueOf(Helper.PROP.getProperty("UNIQUE_PALLET_PER_PACK_TYPE")) == 1) {
                        result = null;
                        System.out.println("startSession for UNIQUE_PALLET_PER_PACK_TYPE");

                        Helper.startSession();
                        System.out.println("start ok !");
                        //Check s'il n'y a pas de palette ouverte pour le même 
                        //packaging et en cours dans le même workstation.
                        query = Helper.sess.createQuery(
                                "FROM BaseContainer bc WHERE "
                                + "bc.packType = :packType AND bc.containerState = :containerState"
                                + " AND bc.packWorkstation = :packWorkstation");
                        query.setParameter("packType", Helper.mode1_context.getBaseContainerTmp().getPackType())
                                .setParameter("containerState", Helper.PALLET_OPEN)
                                .setParameter("packWorkstation", Helper.HOSTNAME);

                        Helper.sess.getTransaction().commit();
                        result = query.list();

                        if (result.isEmpty()) {
                            this.loadConfigUcs();
                        } else {
                            // Test sur une seule palette par type de packaging.
                            BaseContainer bc = (BaseContainer) result.get(0);
                            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0025_PACKTYPE_ALREADY_OPEN_IN_THE_SAME_WORKSTATION,
                                    Helper.mode1_context.getBaseContainerTmp().getPackType(),
                                    Helper.HOSTNAME,
                                    bc.getHarnessPart(),
                                    bc.getPalletNumber(),
                                    Helper.mode1_context.getBaseContainerTmp().getPackType()),
                                    "Palette déjà ouverte du même type !", JOptionPane.ERROR_MESSAGE);

                            Helper.log.info(String.format(Helper.ERR0025_PACKTYPE_ALREADY_OPEN_IN_THE_SAME_WORKSTATION,
                                    Helper.mode1_context.getBaseContainerTmp().getPackType(),
                                    Helper.HOSTNAME,
                                    bc.getHarnessPart(),
                                    Helper.mode1_context.getBaseContainerTmp().getHarnessPart().substring(1),
                                    bc.getPalletNumber(),
                                    Helper.mode1_context.getBaseContainerTmp().getPackType()));

                            Mode1_S020_PalletChoice state = new Mode1_S020_PalletChoice();
                            Helper.mode1_context.setState(state);
                            this.dispose();
                        }
                    } else { // Le test sur unique pack type est désactivé. passer directement au chargement UCS.
                        this.loadConfigUcs();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, String.format(Helper.ERR0024_PALLET_ALREADY_OPEN, Helper.mode1_context.getBaseContainerTmp().getHarnessPart().substring(1)), "Palette déjà ouverte !", JOptionPane.ERROR_MESSAGE);
                    Helper.log.info(String.format(Helper.ERR0024_PALLET_ALREADY_OPEN, Helper.mode1_context.getBaseContainerTmp().getHarnessPart().substring(1)));
                    //this.clearScanBox(Helper.scan_txtbox);
                    Mode1_S020_PalletChoice state = new Mode1_S020_PalletChoice();
                    Helper.mode1_context.setState(state);
                    this.dispose();
                }
                /*--------------------------------*/
            }
        }
    }//GEN-LAST:event_ucs_jtableKeyPressed

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid
        Helper.mode1_context.setBaseContainerTmp(new BaseContainerTmp());
        Helper.mode1_context.setBaseHarnessAdditionalBarecodeTmp(new BaseHarnessAdditionalBarecodeTmp());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable ucs_jtable;
    // End of variables declaration//GEN-END:variables
}
