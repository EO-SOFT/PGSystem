/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch;

import entity.LoadPlanDestination;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author Administrator
 */
public class WAREHOUSE_DISPATCH_UI0006_LIST extends javax.swing.JDialog {

    Vector plan_result_table_data = new Vector();
    Vector<String> plan_result_table_header = new Vector<String>();
    List<String> plan_table_header = Arrays.asList(
            "ID",
            "Create ID",
            "Create Time",
            "Delivery Date",
            "User",
            "Destination",
            "State"
    );

    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public WAREHOUSE_DISPATCH_UI0006_LIST(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initGui();
        //refresh();

    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        disableEditingTables();

        //Load table header
        load_table_header();

        //Init projects filter
        initTimeSpinners();

        //Load Destinations
        loadDestination();
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = plan_table_header.iterator(); it.hasNext();) {
            plan_result_table_header.add(it.next());
        }
    }

    /**
     * Charge les destinations dans le jBox
     */
    public final void loadDestination() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_FINAL_DESTINATIONS);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        this.loadDestsInJbox(result, destinationsBox);
    }

    public static void loadDestsInJbox(List result, JComboBox jbox) {

        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, Helper.ERR0014_NO_PROJECT_FOUND, "Projects Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0014_NO_PROJECT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                LoadPlanDestination lp = (LoadPlanDestination) o;
                jbox.addItem(new ComboItem(lp.getDestination(), lp.getDestination()));
            }
        }
    }

    private void initTimeSpinners() {

        startDatePicker.setDate(new Date());
        endDatePicker.setDate(new Date());

    }

    public void reset_table_content() {
        plan_result_table_data = new Vector();
        loadPlan_result_table.setModel(new DefaultTableModel(plan_result_table_data, plan_result_table_header));
    }

    public void disableEditingTables() {
        for (int c = 0; c < loadPlan_result_table.getColumnCount(); c++) {
            // remove editor   
            Class<?> col_class = loadPlan_result_table.getColumnClass(c);
            loadPlan_result_table.setDefaultEditor(col_class, null);
        }
        loadPlan_result_table.setAutoCreateRowSorter(true);

    }

    @SuppressWarnings("empty-statement")
    public void reload_result_table_data(List<Object[]> resultList) {
        reset_load_plan_lines_table_content();
        for (Object[] obj : resultList) {

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(obj[0])); // ID
            oneRow.add(String.valueOf(obj[1])); // "Create ID"
            oneRow.add(String.valueOf(obj[2])); // "Create Time"
            oneRow.add(String.valueOf(obj[3])); // "Delivery Date"
            oneRow.add(String.valueOf(obj[4])); // "User"
            oneRow.add(String.valueOf(obj[5])); // "Destination"                    
            oneRow.add(String.valueOf(obj[6])); // "State"

            plan_result_table_data.add(oneRow);
        }
        loadPlan_result_table.setModel(new DefaultTableModel(plan_result_table_data, plan_result_table_header));
        loadPlan_result_table.setFont(new Font(String.valueOf(Helper.PROP.getProperty("JTABLE_FONT")), Font.BOLD, 14));
        loadPlan_result_table.setRowHeight(Integer.valueOf(Helper.PROP.getProperty("JTABLE_ROW_HEIGHT")));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north_panel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        loadPlan_result_table = new javax.swing.JTable();
        refresh_btn = new javax.swing.JButton();
        startDatePicker = new org.jdesktop.swingx.JXDatePicker();
        endDatePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        destinationsBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        statutBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Plans du chargement");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        north_panel.setBackground(new java.awt.Color(51, 51, 51));
        north_panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                north_panelKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Liste des plans de chargement");

        loadPlan_result_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(loadPlan_result_table);

        refresh_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refresh_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/filter-icon.png"))); // NOI18N
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Date création");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Du");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Destination");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Au");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Statut");

        statutBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "OPEN", "CLOSED" }));

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(destinationsBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statutBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(refresh_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(destinationsBox)
                    .addComponent(jLabel3)
                    .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(statutBox)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(north_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(north_panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    private void north_panelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_north_panelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_north_panelKeyPressed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void refresh() {
        SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateDf.format(startDatePicker.getDate());
        String endDate = dateDf.format(endDatePicker.getDate());
        Helper.startSession();
        String str = "SELECT l.id, l.create_id, l.create_time, l.delivery_time, "
                + "l.m_user, w.destination, l.plan_state "
                + "FROM load_plan l, load_plan_destination_rel w "
                + "WHERE l.id = w.load_plan_id AND l.create_time BETWEEN '" + startDate + "' AND '" + endDate +"' ";
        if (!"".equals(destinationsBox.getSelectedItem().toString())) {
            str += "AND w.destination = '" + destinationsBox.getSelectedItem().toString()+ "'";
        }
        if (!"".equals(statutBox.getSelectedItem().toString())) {
            str += "AND l.plan_state = '" + statutBox.getSelectedItem().toString()+ "'";
        }
        SQLQuery query = Helper.sess.createSQLQuery(str);

        List result = query.list();

        Helper.sess.getTransaction().commit();

        this.reload_result_table_data(result);

        this.disableEditingTables();
    }
    
    public void reset_load_plan_lines_table_content() {
        plan_result_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(plan_result_table_data, plan_result_table_header);
        loadPlan_result_table.setModel(dataModel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox destinationsBox;
    private org.jdesktop.swingx.JXDatePicker endDatePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable loadPlan_result_table;
    private javax.swing.JPanel north_panel;
    private javax.swing.JButton refresh_btn;
    private org.jdesktop.swingx.JXDatePicker startDatePicker;
    private javax.swing.JComboBox statutBox;
    // End of variables declaration//GEN-END:variables

}
