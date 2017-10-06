/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging;

import helper.Helper;
import helper.HQLHelper;
import entity.HisBaseContainer;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;

/**
 *
 * @author user
 */
public final class PACKAGING_UI0014_PalletHistory extends javax.swing.JDialog {

    Vector<String> history_table_header = new Vector<String>();
    List<String> table_header = Arrays.asList(
            "Pallet Number",
            "Harness Type",
            "Harness Part",
            "Create Time",
            "Fifo Time",
            "Complete Time",
            "Supplier Part Number",
            "Pack Type",
            "Pack Size",
            "Qty Read",
            "Index",
            "User",
            "State",
            "State code",
            "Feedback"
    );
    Vector history_table_data = new Vector();
    private String palletNumber;

    public String getPalletNumber() {
        return palletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        this.palletNumber = palletNumber;
    }

    /**
     * Creates new form UI0010_PalletDetails
     *
     * @param parent
     * @param modal
     * @param PalletNumber : Requested container number to be displayed
     */
    public PACKAGING_UI0014_PalletHistory(java.awt.Frame parent, boolean modal, String PalletNumber) {
        super(parent, modal);
        initComponents();
        initGui();
        pallet_number_lbl.setText(PalletNumber);
        search_txtbox.setText(PalletNumber);
        this.setPalletNumber(palletNumber);
        this.searchForPallet(PalletNumber);
    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        disableEditingTable();

        //Load table header
        load_history_table_header();

        //Disable resizing
        this.setResizable(false);
    }

    public void disableEditingTable() {
        for (int c = 0; c < history_table.getColumnCount(); c++) {
            Class<?> col_class = history_table.getColumnClass(c);
            history_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        table_scroll = new javax.swing.JScrollPane();
        history_table = new javax.swing.JTable();
        pallet_number_lbl = new javax.swing.JLabel();
        refresh_btn = new javax.swing.JButton();
        search_txtbox = new javax.swing.JTextField();
        message_lbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Historique Palette");
        setBackground(new java.awt.Color(51, 51, 51));
        setType(java.awt.Window.Type.UTILITY);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Historique palette");

        history_table.setAutoCreateRowSorter(true);
        history_table.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        history_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        table_scroll.setViewportView(history_table);

        pallet_number_lbl.setBackground(new java.awt.Color(255, 255, 255));
        pallet_number_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        pallet_number_lbl.setForeground(new java.awt.Color(255, 255, 255));

        refresh_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refresh_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/refresh.png"))); // NOI18N
        refresh_btn.setText("Actualiser");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        search_txtbox.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        search_txtbox.setForeground(new java.awt.Color(0, 0, 153));
        search_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_txtboxActionPerformed(evt);
            }
        });
        search_txtbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_txtboxKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search_txtboxKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                search_txtboxKeyTyped(evt);
            }
        });

        message_lbl.setBackground(new java.awt.Color(255, 255, 255));
        message_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        message_lbl.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1117, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(pallet_number_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(search_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refresh_btn)
                        .addGap(18, 18, 18)
                        .addComponent(message_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pallet_number_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refresh_btn)
                            .addComponent(search_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(table_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(message_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void load_history_table_header() {
        this.reset_history_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            history_table_header.add(it.next());
        }

        history_table.setModel(new DefaultTableModel(history_table_data, history_table_header));

    }

    public void reset_history_table_content() {
        history_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(history_table_data, history_table_header);
        history_table.setModel(dataModel);
    }

    public void reload_history_table_data(List resultList) {
        this.reset_history_table_content();

        for (Object o : resultList) {
            HisBaseContainer bc = (HisBaseContainer) o;
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(bc.getPalletNumber());
            oneRow.add(bc.getHarnessType());
            oneRow.add(bc.getHarnessPart());
            oneRow.add(bc.getCreateTime());
            oneRow.add(bc.getFifoTime());
            oneRow.add(bc.getFifoTime());
            oneRow.add(bc.getSupplierPartNumber());
            oneRow.add(bc.getPackType());
            oneRow.add(bc.getQtyExpected());
            oneRow.add(bc.getQtyRead());
            oneRow.add(bc.getHarnessIndex());
            oneRow.add(bc.getUser() + " / " + bc.getCreateUser());
            oneRow.add(bc.getContainerState());
            oneRow.add(bc.getContainerStateCode());
            oneRow.add(bc.getFeedback());
            history_table_data.add(oneRow);
        }

        history_table.setModel(new DefaultTableModel(history_table_data, history_table_header));
        history_table.setAutoCreateRowSorter(true);
        //Desable table edition
        disableEditingTable();
    }

    private void searchForPallet(String palletNumber) {
        message_lbl.setText("");
        //################# Container Data ####################
        //Start transaction                
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_HIS_CONTAINER);
        query.setParameter("palletNumber", palletNumber.trim());
        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (!result.isEmpty()) {
            this.reload_history_table_data(result);
        }else{
            reset_history_table_content();
            message_lbl.setText(String.format(Helper.ERR0023_PALLET_NOT_FOUND, palletNumber));
        }
            
    }
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        pallet_number_lbl.setText(search_txtbox.getText());
        this.searchForPallet(pallet_number_lbl.getText());
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void search_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_search_txtboxActionPerformed

    private void search_txtboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_txtboxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pallet_number_lbl.setText(search_txtbox.getText());
            this.searchForPallet(search_txtbox.getText());
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        } else if (evt.getKeyCode() == KeyEvent.VK_CLEAR) {
            this.search_txtbox.setText("");
            this.reset_history_table_content();
        } else {
            if (!search_txtbox.getText().isEmpty()) {
                refresh_btn.setEnabled(true);
            } else {
                refresh_btn.setEnabled(false);
                message_lbl.setText("");
            }
        }
    }//GEN-LAST:event_search_txtboxKeyPressed

    private void search_txtboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_txtboxKeyReleased
        if (!search_txtbox.getText().isEmpty()) {
            refresh_btn.setEnabled(true);
        } else {
            refresh_btn.setEnabled(false);
            message_lbl.setText("");
        }
    }//GEN-LAST:event_search_txtboxKeyReleased

    private void search_txtboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_txtboxKeyTyped
        if (!search_txtbox.getText().isEmpty()) {
            refresh_btn.setEnabled(true);
        } else {
            refresh_btn.setEnabled(false);
            message_lbl.setText("");
        }
    }//GEN-LAST:event_search_txtboxKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable history_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel message_lbl;
    private javax.swing.JLabel pallet_number_lbl;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JTextField search_txtbox;
    private javax.swing.JScrollPane table_scroll;
    // End of variables declaration//GEN-END:variables

    void clearSearchBox(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
