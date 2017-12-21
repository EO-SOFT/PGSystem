/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch;

import entity.BaseContainer;
import gui.warehouse_dispatch.state.State;
import entity.HisLogin;
import entity.LoadPlan;
import entity.LoadPlanDestinationRel;
import entity.LoadPlanLine;
import entity.LoadPlanLinePackaging;
import entity.ManufactureUsers;
import entity.PackagingStockMovement;
import gui.packaging.reports.PACKAGING_UI0010_PalletDetails;
import helper.JDialogExcelFileChooser;
import gui.warehouse_dispatch.state.S020_PalletNumberScan;
import gui.warehouse_dispatch.state.WarehouseHelper;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import static java.awt.Event.DELETE;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.hibernate.Query;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author user
 */
public final class WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN extends javax.swing.JFrame implements ActionListener,
        PropertyChangeListener {

    State state = WarehouseHelper.warehouse_out_context.getState();
    @SuppressWarnings("UseOfObsoleteCollectionType")
    Vector<String> load_plan_lines_table_header = new Vector<String>();
    Vector<String> load_plan_table_header = new Vector<String>();
    Vector load_plan_lines_table_data = new Vector();
    Vector load_plan_table_data = new Vector();
    @SuppressWarnings("UseOfObsoleteCollectionType")
    Vector load_plan_lines_data = new Vector();
    @SuppressWarnings("UseOfObsoleteCollectionType")
    Vector load_plan_data = new Vector();
    String initTextValue = "...................";
    int DESTINATION_COMLUMN = 8;
    int LINE_ID_COMLUMN = 9;
    int PALLET_NUM_COLUMN = 1;

    private ReleasingJob task;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);

//            JOptionPane.showMessageDialog(null, String.format(
//                    "Completed %d%% of task.\n", task.getProgress()));
        }
    }

    class ReleasingJob extends SwingWorker<Void, Void> {

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            //release_plan_btn.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            JOptionPane.showMessageDialog(null, "Plan released !\n");

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Creates new form NewJFrame
     */
    public WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN() {
        initComponents();
    }

    public WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN(Object[] context, JFrame parent) {

        initComponents();
        //Initialiser les valeurs globales de test (Pattern Liste,...)
        Helper.startSession();

        initGui();

    }

    /**
     * Charge les destinations dans le jBox
     *
     * @param loadPlanId
     * @return
     */
    public boolean loadDestinations(int loadPlanId) {
        //System.out.println("Distination du " + loadPlanId);
        Helper.startSession();

        //System.out.println("NEw created" + WarehouseHelper.temp_load_plan.getId());
        Query query = Helper.sess.createQuery(HQLHelper.GET_FINAL_DESTINATIONS_OF_PLAN);
        query.setParameter("loadPlanId", loadPlanId);
        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0029_NO_DESTINATIONS_FOR_DISPATCH, "Destinations Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0029_NO_DESTINATIONS_FOR_DISPATCH);
            return false;
        } else {
            //destinations_box.setEnabled(true);
            destinations_box.removeAllItems();
            String[] destList = new String[result.size()];
            int i = 0;
            //Map project data in the list
            for (Object o : result) {
                LoadPlanDestinationRel lp = (LoadPlanDestinationRel) o;
                destinations_box.addItem(new ComboItem(lp.getDestination(), lp.getDestination()));
                destList[i++] = lp.getDestination();
            }
            return true;
        }
    }

    /**
     *
     * @return The selected destination value
     */
    public String getSelectedDestination() {
        try {
            return destinations_box.getSelectedItem().toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void initPlanTableDoubleClick() {

        this.load_plan_lines_table.addMouseListener(
                new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                    new PACKAGING_UI0010_PalletDetails(null,
                            rootPaneCheckingEnabled,
                            String.valueOf(
                                    load_plan_lines_table.getValueAt(
                                            load_plan_lines_table.getSelectedRow(),
                                            PALLET_NUM_COLUMN)), false, false, false
                    ).setVisible(true);
                }
            }
        }
        );

        this.load_plan_table.addMouseListener(
                new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadPlanDataInGui();
                }
            }

            public void loadPlanDataInGui() {
                String id = String.valueOf(load_plan_table.getValueAt(load_plan_table.getSelectedRow(), 0));
                Helper.startSession();
                Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_BY_ID);
                query.setParameter("id", Integer.valueOf(id));

                Helper.sess.getTransaction().commit();
                List result = query.list();
                LoadPlan plan = (LoadPlan) result.get(0);
                WarehouseHelper.temp_load_plan = plan;

                //Load destinations of the plan
                if (loadDestinations(Integer.valueOf(id))) {
                    loadPlanDataToLabels(plan, destinations_box.getItemAt(0).toString());
                    reloadPlanLinesData(Integer.valueOf(id), destinations_box.getItemAt(0).toString());

                    //Disable delete button if the plan is CLOSED
                    if (WarehouseHelper.LOAD_PLAN_STATE_CLOSED.equals(plan.getPlanState())) {
                        delete_plan_btn.setEnabled(false);
                        release_plan_btn.setEnabled(false);
                        export_plan_btn.setEnabled(true);
                        edit_plan_btn.setEnabled(false);
                        set_packaging_pile_btn.setEnabled(true);
                        destinations_box.setEnabled(true);
                        piles_box.setEnabled(true);
                        scan_txt.setEnabled(false);
                        txt_filter_part.setEnabled(true);
                    } else { // The plan still Open
                        delete_plan_btn.setEnabled(true);
                        release_plan_btn.setEnabled(true);
                        export_plan_btn.setEnabled(true);
                        edit_plan_btn.setEnabled(true);
                        set_packaging_pile_btn.setEnabled(true);
                        destinations_box.setEnabled(true);
                        destinations_box.setSelectedIndex(0);
                        piles_box.setEnabled(true);
                        scan_txt.setEnabled(true);
                        txt_filter_part.setEnabled(true);
                    }
                }

                filterPlanLines(false);
                filterPlanLines(false);
            }
        }
        );
    }

    public JTable getLoadPlan_lines_table() {
        return load_plan_lines_table;
    }

    public void setDispatch_lines_table(JTable load_plan_lines_table) {
        this.load_plan_lines_table = load_plan_lines_table;
    }

    /**
     * Load data in the UI from the given object
     *
     * @param p
     */
    public void loadPlanDataToLabels(LoadPlan p, String defaultDest) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        this.plan_num_label.setText("" + p.getId());
        this.create_user_label.setText(p.getUser());
        this.create_time_label.setText(sdf1.format(p.getCreateTime()));
        if (p.getDeliveryTime() != null) {
            this.dispatch_date_label.setText(sdf2.format(p.getDeliveryTime()));
        }
        if (p.getEndTime() != null) {
            this.release_date_label.setText(sdf1.format(p.getEndTime()));
        }
        this.state_label.setText(p.getPlanState());
        this.destination_label_help.setText("");

        //Select the last pile of the plan
        Helper.startSession();
        String query_str = String.format(HQLHelper.GET_PILES_OF_PLAN, Integer.valueOf(p.getId()));
        SQLQuery query = Helper.sess.createSQLQuery(query_str);

        query.addScalar("pile_num", StandardBasicTypes.INTEGER);
        List<Object[]> resultList = query.list();
        Helper.sess.getTransaction().commit();
        Integer[] arg = (Integer[]) resultList.toArray(new Integer[resultList.size()]);
        if (!resultList.isEmpty()) {
            for (int i = 1; i < arg.length; i++) {
                if (Integer.valueOf(piles_box.getItemAt(i).toString().trim()) == arg[i]) {
                    piles_box.setSelectedIndex(i);
                    pile_label_help.setText(arg[i].toString());
                }
            }
        } else {
            piles_box.setSelectedIndex(1);
            pile_label_help.setText(piles_box.getSelectedItem().toString());
        }
        this.destination_label_help.setText(defaultDest);

    }

    public void cleanDataLabels() {
        this.plan_num_label.setText("#");
        this.create_user_label.setText("-----");
        this.create_time_label.setText("--/--/---- --:--");
        this.dispatch_date_label.setText("--/--/----");
        this.release_date_label.setText("--/--/---- --:--");
        this.state_label.setText("-----");
        this.destination_label_help.setText("#");
        this.pile_label_help.setText("0");
    }

    public void setDestinationHelpLabel(String dest) {
        destination_label_help.setText(dest);
    }

    public void setTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        create_time_label.setText(sdf.format(date));
    }

    public void setPlanNumLabel(String text) {
        plan_num_label.setText(text);
    }

    public String getSelectedPileNum() {
        return String.valueOf(piles_box.getSelectedItem());
    }

    public String getPlanNum() {
        return plan_num_label.getText();
    }

    public String getPlanDispatchTime() {
        return dispatch_date_label.getText();
    }

    private void initGui() {

        this.scan_txt.setEnabled(false);
        this.txt_filter_part.setEnabled(false);

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        //Group radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(radio_btn_20);
        group.add(radio_btn_40);

        //Center the this dialog in the screen
        Helper.centerJFrame(this);

        //Desable load plans lines table edition
        disableLoadPlanLinesEditingTable();

        //Desable load plans table edition
        disableLoadPlanEditingTable();

        //Init JTable Key Listener
        initJTableKeyListener();

        //Load table header
        load_line_table_header();

        //Load table header
        load_plan_table_header();

        //Initialize double clique on table row
        initPlanTableDoubleClick();

        ///Charger les plan de la base
        reloadPlansData();

        //
        //this.loadDestinations(0);
        //Disable destinations Jbox
        destinations_box.setEnabled(false);
        piles_box.setEnabled(false);

        export_plan_btn.setEnabled(false);
        delete_plan_btn.setEnabled(false);
        release_plan_btn.setEnabled(false);
        edit_plan_btn.setEnabled(false);
        set_packaging_pile_btn.setEnabled(false);

        //Clean values form fields
        cleanDataLabels();

        //Show the jframe
        this.setVisible(true);

    }

    private void loadPiles() {
        piles_box.removeAllItems();
        piles_box.addItem("*");
        int len = 32;
        if (radio_btn_40.isSelected()) {
            len = 64;
        }
        for (int i = 1; i <= len; i++) {
            piles_box.addItem(i);
        }
    }

    public void initJTableKeyListener() {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = this.load_plan_lines_table.getInputMap(condition);
        ActionMap actionMap = this.load_plan_lines_table.getActionMap();

        // DELETE is a String constant that for me was defined as "Delete"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE);

        actionMap.put(DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!state_label.getText().equals(WarehouseHelper.LOAD_PLAN_STATE_CLOSED)) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Voulez-vous supprimer la ligne ?", "Suppression !",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmed == 0) {

                        Integer id = (Integer) load_plan_lines_table.getValueAt(load_plan_lines_table.getSelectedRow(), LINE_ID_COMLUMN);
                        System.out.println(id);
                        //Delete line from the database
                        Helper.startSession();
                        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_ID);
                        query.setParameter("id", id);

                        Helper.sess.getTransaction().commit();
                        List result = query.list();
                        LoadPlanLine line = (LoadPlanLine) result.get(0);
                        line.delete(line);
                        filterPlanLines(false);
                    }
                }
            }

        });

    }

    /**
     * Desactive l'édition du jTable load plan lines
     */
    public void disableLoadPlanLinesEditingTable() {
        for (int c = 0; c < load_plan_lines_table.getColumnCount(); c++) {
            Class<?> col_class = load_plan_lines_table.getColumnClass(c);
            load_plan_lines_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * Desactive l'édition du jTable load plan
     */
    public void disableLoadPlanEditingTable() {
        for (int c = 0; c < load_plan_table.getColumnCount(); c++) {
            Class<?> col_class = load_plan_table.getColumnClass(c);
            load_plan_table.setDefaultEditor(col_class, null);        // remove editor                 
        }
        load_plan_table.setAutoCreateRowSorter(true);
    }

    /**
     * Charge les entête du jTable load plan lines
     */
    public void load_line_table_header() {
        this.reset_load_plan_lines_table_content();
        load_plan_lines_table_header.add("PILE NUM");
        load_plan_lines_table_header.add("PALLET NUM");
        load_plan_lines_table_header.add("CUSTOMER PN");
        load_plan_lines_table_header.add("INDICE");
        load_plan_lines_table_header.add("LEONI PN");
        load_plan_lines_table_header.add("PACK TYPE");
        load_plan_lines_table_header.add("PACK SIZE");
        load_plan_lines_table_header.add("FORS LABEL NUM");
        load_plan_lines_table_header.add("DESTINATION");
        load_plan_lines_table_header.add("N° LINE");
        load_plan_lines_table_header.add("FAMILLE");

        load_plan_lines_table.setModel(new DefaultTableModel(load_plan_lines_data, load_plan_lines_table_header));
    }

    /**
     * Charge les entête du jTable load plan
     */
    public void load_plan_table_header() {
        this.reset_load_plan_table_content();
        load_plan_table_header.add("N° PLAN");
        load_plan_table_header.add("CREATE DATE");
        load_plan_table_header.add("DELIV DATE");
        load_plan_table_header.add("USER");
        load_plan_table_header.add("STATE");

        load_plan_table.setModel(new DefaultTableModel(load_plan_data, load_plan_table_header));
    }

    /**
     *
     * @param planId
     * @param destinationWh
     * @param harnessPart
     * @param pileNum
     */
    //completer la méthode pour filtrer sur les lignes
    public void filterPlanLines(int planId, String destinationWh, String harnessPart, int pileNum) {
        System.out.println("Destination filterPlanLines " + destinationWh);
        Helper.startSession();
        String GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_DEST_AND_PN_AND_PILE = "FROM LoadPlanLine lpl WHERE "
                + "lpl.loadPlanId = :loadPlanId "
                + "AND lpl.destinationWh LIKE :destinationWh "
                + "AND lpl.harnessPart LIKE :harnessPart ";
        if (pileNum != 0) {
            GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_DEST_AND_PN_AND_PILE += "AND lpl.pileNum = :pileNum ";
        }
        GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_DEST_AND_PN_AND_PILE += "ORDER BY "
                + "destinationWh ASC, "
                + "pileNum DESC,"
                + "id ASC";

        Query query = Helper.sess.createQuery(GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_DEST_AND_PN_AND_PILE);
        query.setParameter("loadPlanId", planId);
        query.setParameter("destinationWh", "%" + destinationWh + "%");
        query.setParameter("harnessPart", "%" + harnessPart + "%");

        if (pileNum != 0) {
            query.setParameter("pileNum", pileNum);
        }

        System.out.println("pileNum " + pileNum);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        txt_nbreLigne.setText(result.size() + "");
        this.reload_load_plan_lines_table_data(result);
    }

    /**
     * Charge les données du jTable load plan lines
     *
     * @param planId
     * @param destinationWh
     */
    public void reloadPlanLinesData(int planId, String destinationWh) {
        System.out.println("reloadPlanLinesData destination WH" + destinationWh);
        Helper.startSession();
        Query query = null;
        if (destinationWh != null && !destinationWh.isEmpty()) {
            query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_WH);
            query.setParameter("loadPlanId", planId);
            query.setParameter("destinationWh", destinationWh);
        } else {
            query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_PLAN_ID);
            query.setParameter("loadPlanId", planId);
        }

        Helper.sess.getTransaction().commit();
        List result = query.list();
        this.reload_load_plan_lines_table_data(result);
    }

    /**
     * Charge les données du jTable load plan lines
     */
    public void reloadPlansData() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_ALL_PLANS);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        this.reload_load_plan_table_data(result);
    }

    /**
     *
     * @param table : JTable element en entrée
     * @return Les donées du JTable sous forme de table 2 dimensions
     */
    public Object[][] getTableData(JTable table) {
        TableModel dtm = table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                tableData[i][j] = dtm.getValueAt(i, j);
            }
        }
        return tableData;
    }

    public void reset_load_plan_table_content() {
        load_plan_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(load_plan_table_data, load_plan_table_header);
        load_plan_table.setModel(dataModel);
    }

    public void reset_load_plan_lines_table_content() {
        load_plan_lines_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(load_plan_lines_table_data, load_plan_lines_table_header);
        load_plan_lines_table.setModel(dataModel);
    }

    /**
     * Mapping des donées dans le JTable load plan lines
     *
     * @param resultList
     */
    public void reload_load_plan_lines_table_data(List resultList) {
        this.reset_load_plan_lines_table_content();

        for (Object o : resultList) {
            LoadPlanLine lpl = (LoadPlanLine) o;
            Vector<Object> oneRow = new Vector<Object>();

            oneRow.add(String.format("%02d", lpl.getPileNum()));
            oneRow.add(lpl.getPalletNumber());
            oneRow.add(lpl.getHarnessPart());
            oneRow.add(lpl.getHarnessIndex());
            oneRow.add(lpl.getSupplierPart());
            oneRow.add(lpl.getPackType());
            oneRow.add(lpl.getQty());
            oneRow.add(lpl.getDispatchLabelNo());
            oneRow.add(lpl.getDestinationWh());
            oneRow.add(lpl.getId());
            oneRow.add(lpl.getHarnessType());

            load_plan_lines_table_data.add(oneRow);

        }
        load_plan_lines_table.setModel(new DefaultTableModel(load_plan_lines_table_data, load_plan_lines_table_header));

        //Initialize default style for table container
        setLoadPlanLinesTableRowsStyle();
    }

    /**
     * Mapping des donées dans le JTable load plan lines
     *
     * @param resultList
     */
    public void reload_load_plan_table_data(List resultList) {
        this.reset_load_plan_table_content();

        for (Object o : resultList) {
            LoadPlan lp = (LoadPlan) o;
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(lp.getId());
            oneRow.add(lp.getCreateTime());
            oneRow.add(lp.getDeliveryTime());
            oneRow.add(lp.getUser());
            oneRow.add(lp.getPlanState());

            load_plan_table_data.add(oneRow);

        }
        load_plan_table.setModel(new DefaultTableModel(load_plan_table_data, load_plan_table_header));

        //Initialize default style for table container
        setLoadPlanTableRowsStyle();
    }

    public void tableAddRow(Component oneRow) {
        load_plan_lines_table.add(oneRow);
    }

    /**
     * Réinitialise le style de la table load plan lines
     */
    public void setLoadPlanLinesTableRowsStyle() {
        //Initialize default style for table container

        //#######################
        load_plan_lines_table.setFont(new Font(String.valueOf(Helper.PROP.getProperty("JTABLE_FONT")), Font.BOLD, 12));
        load_plan_lines_table.setRowHeight(Integer.valueOf(Helper.PROP.getProperty("JTABLE_ROW_HEIGHT")));
        load_plan_lines_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(JLabel.LEFT);
                return this;
            }
        });
        //#######################
        this.disableLoadPlanLinesEditingTable();
    }

    /**
     * Réinitialise le style de la table load plan
     */
    public void setLoadPlanTableRowsStyle() {
        //Initialize default style for table container

        //#######################        
        load_plan_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                setBackground(Color.WHITE);
                setForeground(Color.BLACK);

                setHorizontalAlignment(JLabel.CENTER);

                return this;
            }
        });
        //#######################
        this.disableLoadPlanEditingTable();
    }

    /**
     *
     * @param msg : String to be displayed
     * @param type : 1 for OK , -1 for error, 0 to clean the label
     */
    public void setMessageLabel(String msg, int type) {

        switch (type) {
            case -1:
                message_label.setBackground(Color.red);
                message_label.setForeground(Color.white);
                message_label.setText(msg);
                break;
            case 1:
                message_label.setBackground(Color.green);
                message_label.setForeground(Color.black);
                message_label.setText(msg);
                break;
            case 0:
                message_label.setBackground(Color.WHITE);
                message_label.setText("");
                break;
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

        connectedUserName_label = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        scan_txt = new javax.swing.JTextField();
        table_scroll = new javax.swing.JScrollPane();
        load_plan_lines_table = new javax.swing.JTable();
        piles_box = new javax.swing.JComboBox();
        radio_btn_20 = new javax.swing.JRadioButton();
        radio_btn_40 = new javax.swing.JRadioButton();
        create_time_label = new javax.swing.JLabel();
        time_label1 = new javax.swing.JLabel();
        time_label2 = new javax.swing.JLabel();
        plan_num_label = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        new_plan_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        load_plan_table = new javax.swing.JTable();
        refresh_btn = new javax.swing.JButton();
        release_plan_btn = new javax.swing.JButton();
        time_label3 = new javax.swing.JLabel();
        create_user_label = new javax.swing.JLabel();
        time_label4 = new javax.swing.JLabel();
        dispatch_date_label = new javax.swing.JLabel();
        time_label5 = new javax.swing.JLabel();
        delete_plan_btn = new javax.swing.JButton();
        time_label6 = new javax.swing.JLabel();
        state_label = new javax.swing.JLabel();
        destinations_box = new javax.swing.JComboBox();
        set_packaging_pile_btn = new javax.swing.JButton();
        pile_label_help = new javax.swing.JLabel();
        destination_label_help = new javax.swing.JLabel();
        time_label7 = new javax.swing.JLabel();
        release_date_label = new javax.swing.JLabel();
        txt_filter_part = new javax.swing.JTextField();
        btn_filter_ok = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        export_plan_btn = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jSeparator3 = new javax.swing.JToolBar.Separator();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        edit_plan_btn = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jSeparator2 = new javax.swing.JToolBar.Separator();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        pallet_details_btn = new javax.swing.JButton();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jSeparator4 = new javax.swing.JToolBar.Separator();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        plan_list_btn = new javax.swing.JButton();
        message_label = new javax.swing.JTextField();
        txt_nbreLigne = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        txt_filter_pal_number = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        plan_id_filter = new javax.swing.JFormattedTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        pallet_details = new javax.swing.JMenuItem();
        load_plans_list = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dispatch Module");
        setBackground(new java.awt.Color(194, 227, 254));
        setFocusTraversalPolicyProvider(true);
        setName("dispatch_module"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        connectedUserName_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        connectedUserName_label.setForeground(new java.awt.Color(0, 51, 204));
        connectedUserName_label.setText(" ");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Session : ");

        scan_txt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        scan_txt.setForeground(new java.awt.Color(0, 0, 153));
        scan_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scan_txtActionPerformed(evt);
            }
        });
        scan_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scan_txtKeyPressed(evt);
            }
        });

        load_plan_lines_table.setAutoCreateRowSorter(true);
        load_plan_lines_table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        load_plan_lines_table.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        load_plan_lines_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        table_scroll.setViewportView(load_plan_lines_table);

        piles_box.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        piles_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "*", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32" }));
        piles_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                piles_boxItemStateChanged(evt);
            }
        });

        radio_btn_20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        radio_btn_20.setSelected(true);
        radio_btn_20.setText("Remorque 20\" (32 Piles)");
        radio_btn_20.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radio_btn_20ItemStateChanged(evt);
            }
        });

        radio_btn_40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        radio_btn_40.setText("Remorque 40\" (64 Piles)");
        radio_btn_40.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radio_btn_40ItemStateChanged(evt);
            }
        });

        create_time_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        create_time_label.setText("--/--/---- --:--");

        time_label1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label1.setText("Date création :");

        time_label2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label2.setText("Plan de chargement N° :");

        plan_num_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        plan_num_label.setText("#");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        new_plan_btn.setBackground(new java.awt.Color(153, 204, 255));
        new_plan_btn.setText("Nouveau...");
        new_plan_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new_plan_btnActionPerformed(evt);
            }
        });

        load_plan_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "N° Plan", "Date création", "Date expédition"
            }
        ));
        jScrollPane1.setViewportView(load_plan_table);

        refresh_btn.setBackground(new java.awt.Color(153, 204, 255));
        refresh_btn.setText("Actualiser");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(refresh_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(new_plan_btn))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(new_plan_btn)
                    .addComponent(refresh_btn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        release_plan_btn.setBackground(new java.awt.Color(255, 255, 0));
        release_plan_btn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        release_plan_btn.setText("Release");
        release_plan_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                release_plan_btnActionPerformed(evt);
            }
        });

        time_label3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label3.setText("Create user :");

        create_user_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        create_user_label.setText("-----");

        time_label4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label4.setText("Date Expédition :");

        dispatch_date_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dispatch_date_label.setText("--/--/----");

        time_label5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label5.setText("Destinations");

        delete_plan_btn.setBackground(new java.awt.Color(255, 0, 0));
        delete_plan_btn.setForeground(new java.awt.Color(255, 255, 255));
        delete_plan_btn.setText("Supprimer");
        delete_plan_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_plan_btnActionPerformed(evt);
            }
        });

        time_label6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label6.setText("Statut :");

        state_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        state_label.setText("-----");

        destinations_box.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        destinations_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                destinations_boxItemStateChanged(evt);
            }
        });
        destinations_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinations_boxActionPerformed(evt);
            }
        });

        set_packaging_pile_btn.setBackground(new java.awt.Color(153, 204, 255));
        set_packaging_pile_btn.setText("Packaging supplém...");
        set_packaging_pile_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_packaging_pile_btnActionPerformed(evt);
            }
        });

        pile_label_help.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        pile_label_help.setForeground(new java.awt.Color(204, 0, 0));
        pile_label_help.setText("0");

        destination_label_help.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        destination_label_help.setForeground(new java.awt.Color(204, 0, 0));
        destination_label_help.setText("#");

        time_label7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label7.setText("Date Release :");

        release_date_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        release_date_label.setText("--/--/----");

        txt_filter_part.setBackground(new java.awt.Color(204, 255, 204));
        txt_filter_part.setToolTipText("Part Number");
        txt_filter_part.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_filter_partActionPerformed(evt);
            }
        });
        txt_filter_part.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_filter_partKeyTyped(evt);
            }
        });

        btn_filter_ok.setBackground(new java.awt.Color(153, 204, 255));
        btn_filter_ok.setText("Actualiser");
        btn_filter_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filter_okActionPerformed(evt);
            }
        });

        jLabel1.setText("CPN");

        jLabel2.setText("Pile Num");

        jLabel3.setText("Destination");

        jToolBar1.setBackground(new java.awt.Color(220, 230, 255));
        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setRollover(true);

        export_plan_btn.setText("Exporter en Excel");
        export_plan_btn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, null, null));
        export_plan_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        export_plan_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export_plan_btnActionPerformed(evt);
            }
        });
        jToolBar1.add(export_plan_btn);
        jToolBar1.add(filler5);
        jToolBar1.add(jSeparator3);

        filler4.setBackground(new java.awt.Color(102, 102, 102));
        jToolBar1.add(filler4);

        edit_plan_btn.setText("Modifier Plan");
        edit_plan_btn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, null, null));
        edit_plan_btn.setFocusable(false);
        edit_plan_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        edit_plan_btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        edit_plan_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_plan_btnActionPerformed(evt);
            }
        });
        jToolBar1.add(edit_plan_btn);
        jToolBar1.add(filler2);
        jToolBar1.add(jSeparator2);
        jToolBar1.add(filler3);

        pallet_details_btn.setText("Détails palette");
        pallet_details_btn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, null, null));
        pallet_details_btn.setFocusable(false);
        pallet_details_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pallet_details_btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pallet_details_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pallet_details_btnActionPerformed(evt);
            }
        });
        jToolBar1.add(pallet_details_btn);
        jToolBar1.add(filler6);
        jToolBar1.add(jSeparator4);
        jToolBar1.add(filler7);

        plan_list_btn.setText("Plans de chargement");
        plan_list_btn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, null, null));
        plan_list_btn.setFocusable(false);
        plan_list_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        plan_list_btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        plan_list_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plan_list_btnActionPerformed(evt);
            }
        });
        jToolBar1.add(plan_list_btn);

        message_label.setEditable(false);
        message_label.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        message_label.setForeground(new java.awt.Color(255, 51, 51));

        txt_nbreLigne.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_nbreLigne.setText("0");

        progressBar.setStringPainted(true);

        txt_filter_pal_number.setBackground(new java.awt.Color(204, 255, 204));
        txt_filter_pal_number.setToolTipText("Part Number");
        txt_filter_pal_number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_filter_pal_numberKeyTyped(evt);
            }
        });

        jLabel4.setText("Pallet ");

        jLabel5.setText("Plan ID");

        plan_id_filter.setBackground(new java.awt.Color(153, 255, 255));
        plan_id_filter.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        plan_id_filter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                plan_id_filterKeyPressed(evt);
            }
        });

        jMenu1.setText("Menus");

        pallet_details.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        pallet_details.setText("Détails palette");
        pallet_details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pallet_detailsActionPerformed(evt);
            }
        });
        jMenu1.add(pallet_details);

        load_plans_list.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        load_plans_list.setText("Plans de chargement");
        load_plans_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                load_plans_listActionPerformed(evt);
            }
        });
        jMenu1.add(load_plans_list);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(message_label)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(scan_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(radio_btn_20)
                                        .addGap(11, 11, 11)
                                        .addComponent(radio_btn_40))))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(table_scroll, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(plan_id_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_filter_part, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txt_filter_pal_number, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(destinations_box, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(piles_box, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_filter_ok)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(set_packaging_pile_btn)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_nbreLigne)
                                .addGap(42, 42, 42))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(143, 143, 143)
                                        .addComponent(destination_label_help, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(57, 57, 57))
                                    .addComponent(time_label5)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel15)
                                            .addGap(446, 446, 446))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(time_label1)
                                                .addComponent(time_label2)
                                                .addComponent(time_label6))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(plan_num_label)
                                                        .addComponent(create_time_label))
                                                    .addGap(28, 28, 28))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(state_label)
                                                    .addGap(95, 95, 95)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(time_label4)
                                                .addComponent(time_label3)
                                                .addComponent(time_label7))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(release_date_label)
                                                .addComponent(create_user_label)
                                                .addComponent(dispatch_date_label)))))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pile_label_help))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 158, Short.MAX_VALUE)
                                        .addComponent(connectedUserName_label, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(delete_plan_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(release_plan_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(132, 132, 132)))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(message_label, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pile_label_help)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(delete_plan_btn)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(release_plan_btn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(85, 85, 85))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(connectedUserName_label, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(110, 110, 110))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(destination_label_help)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(time_label2)
                                                    .addComponent(plan_num_label))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(time_label1)
                                                    .addComponent(create_time_label))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(time_label6)
                                                    .addComponent(state_label)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(time_label3)
                                                    .addComponent(create_user_label))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(time_label4)
                                                    .addComponent(dispatch_date_label))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(time_label7)
                                                    .addComponent(release_date_label)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(time_label5)
                                        .addGap(128, 128, 128)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_filter_part, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_filter_pal_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(plan_id_filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(destinations_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(piles_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_filter_ok)
                            .addComponent(set_packaging_pile_btn)
                            .addComponent(txt_nbreLigne))
                        .addGap(7, 7, 7)
                        .addComponent(table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(scan_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radio_btn_20)
                            .addComponent(radio_btn_40))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleDescription("Dispatch Module");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scan_txtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scan_txtKeyPressed
        // User has pressed Carriage return button
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            state.doAction(WarehouseHelper.warehouse_out_context);
            state = WarehouseHelper.warehouse_out_context.getState();
            System.out.println(scan_txt.getText());
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to logoff ?", "Logoff confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirmed == 0) {
                logout();
            }
        }
    }//GEN-LAST:event_scan_txtKeyPressed

    //########################################################################
    //################ Reset GUI Component to State S01 ######################
    //########################################################################
    public void logout() {

        if (WarehouseHelper.warehouse_out_context.getUser().getId() != null) {
            //Save authentication line in HisLogin table
            HisLogin his_login = new HisLogin(
                    WarehouseHelper.warehouse_out_context.getUser().getId(),
                    WarehouseHelper.warehouse_out_context.getUser().getId(),
                    String.format(Helper.INFO0012_LOGOUT_SUCCESS,
                            WarehouseHelper.warehouse_out_context.getUser().getFirstName()
                            + " " + WarehouseHelper.warehouse_out_context.getUser().getLastName()
                            + " / " + WarehouseHelper.warehouse_out_context.getUser().getLogin(),
                            Helper.HOSTNAME, Helper.getStrTimeStamp()));
            his_login.setCreateId(WarehouseHelper.warehouse_out_context.getUser().getId());
            his_login.setWriteId(WarehouseHelper.warehouse_out_context.getUser().getId());

            String str = String.format(Helper.INFO0012_LOGOUT_SUCCESS,
                    WarehouseHelper.warehouse_out_context.getUser().getFirstName() + " " + WarehouseHelper.warehouse_out_context.getUser().getLastName()
                    + " / " + Helper.mode2_context.getUser().getLogin(), Helper.HOSTNAME,
                    Helper.getStrTimeStamp());
            his_login.setMessage(str);

            str = "";
            his_login.create(his_login);

            //Reset the state
            state = new S020_PalletNumberScan();

            this.clearContextSessionVals();

            //Helper.mode2_context.setUser(null);
            connectedUserName_label.setText("");
        }

    }

    //########################################################################
    //########################## USER LABEL METHODS ##########################
    //########################################################################
    public void setUserLabelText(String newText) {
        connectedUserName_label.setText(newText);
    }

    public JTextField getScanTxt() {
        return this.scan_txt;
    }

    public void setScanTxt(JTextField setScanTxt) {
        this.scan_txt = setScanTxt;
    }

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid        
        Helper.mode2_context.setUser(new ManufactureUsers());
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        int confirmed = JOptionPane.showConfirmDialog(null,
                "On quittant le programme vous perdez toutes vos données actuelles. Voulez-vous quitter ?", "Exit Program Message Box",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == 0) {
            clearGui();
            logout();
            this.dispose();
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//no
        }
    }//GEN-LAST:event_formWindowClosing

    private void radio_btn_40ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radio_btn_40ItemStateChanged
        loadPiles();
    }//GEN-LAST:event_radio_btn_40ItemStateChanged

    private void radio_btn_20ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radio_btn_20ItemStateChanged
        loadPiles();
    }//GEN-LAST:event_radio_btn_20ItemStateChanged

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped


    private void release_plan_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_release_plan_btnActionPerformed

        int confirmed = JOptionPane.showConfirmDialog(null,
                "Confirmez-vous la fin du chargement N° " + plan_num_label.getText() + " ?", "Fin du chargement",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == 0) {
            Helper.startSession();
            Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_PLAN_ID);
            query.setParameter("loadPlanId", Integer.valueOf(plan_num_label.getText()));
            Helper.sess.getTransaction().commit();
            List result = query.list();
            if (!result.isEmpty()) {
                //Initialize progress property.

                release_plan_btn.setEnabled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                for (Object obj : result) {
                    LoadPlanLine line = (LoadPlanLine) obj;
                    BaseContainer bc = new BaseContainer().getBaseContainer(line.getPalletNumber());
                    bc.setContainerState(Helper.PALLET_SHIPPED);
                    bc.setContainerStateCode(Helper.PALLET_SHIPPED_CODE);
                    bc.setShippedTime(new Date());
                    bc.setFifoTime(new Date());
                    bc.setDestination(line.getDestinationWh());
                    bc.update(bc);

                    if (Helper.PROP.getProperty("BOOK_PACKAGING") == null || "".equals(Helper.PROP.getProperty("BOOK_PACKAGING").toString())) {
                        JOptionPane.showMessageDialog(null, "Propriété BOOK_PACKAGING non spécifiée dans le "
                                + "fichier des propriétées !", "Erreur propriétés", JOptionPane.ERROR_MESSAGE);
                    } else if (Helper.PROP.getProperty("WH_FINISH_GOODS") == null || "".equals(Helper.PROP.getProperty("WH_FINISH_GOODS").toString())) {
                        JOptionPane.showMessageDialog(null, "Propriété WH_FINISH_GOODS non spécifiée  dans le "
                                + "fichier des propriétées !", "Erreur propriétés", JOptionPane.ERROR_MESSAGE);
                    } else if ("1".equals(Helper.PROP.getProperty("BOOK_PACKAGING").toString())) {
                        //Book packaging items                    
                        PackagingStockMovement pm = new PackagingStockMovement();
                        pm.bookMasterPack(
                                Helper.mode2_context.getUser().getFirstName() + " " + Helper.mode2_context.getUser().getLastName(),
                                bc.getPackType(),
                                1,
                                "OUT",
                                Helper.PROP.getProperty("WH_FINISH_GOODS"),
                                line.getDestinationWh(), //Indice 9 pour destination
                                "End of Dispatch",
                                plan_num_label.getText()
                        );
                    }
                }
                //Loop on packaging supplementaire et déduire les quantitées.
                if ("1".equals(Helper.PROP.getProperty("BOOK_PACKAGING").toString())) {
                    Helper.startSession();
                    query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_PACKAGING_BY_PLAN_ID);
                    query.setParameter("loadPlanId", Integer.valueOf(plan_num_label.getText()));
                    Helper.sess.getTransaction().commit();
                    result = query.list();
                    for (Object obj : result) {
                        LoadPlanLinePackaging line = (LoadPlanLinePackaging) obj;
                        PackagingStockMovement transaction
                                = new PackagingStockMovement(
                                        line.getPackItem(),
                                        "",
                                        plan_num_label.getText(),
                                        WarehouseHelper.warehouse_out_context.getUser().getFirstName() + " "
                                        + WarehouseHelper.warehouse_out_context.getUser().getLastName(),
                                        new Date(),
                                        Helper.PROP.getProperty("WH_PACKAGING"),
                                        -Float.valueOf(line.getQty()),
                                        "Packaging Supplementaire. " + line.getComment());
                        transaction.create(transaction);
                    }
                }
                Helper.startSession();
                WarehouseHelper.temp_load_plan.setPlanState(WarehouseHelper.LOAD_PLAN_STATE_CLOSED);
                WarehouseHelper.temp_load_plan.setEndTime(new Date());
                WarehouseHelper.temp_load_plan.update(WarehouseHelper.temp_load_plan);

                clearGui();

                //Refresh Data
                reloadPlansData();

                //Go back to step S020
                state = new S020_PalletNumberScan();
                WarehouseHelper.warehouse_out_context.setState(state);

                Toolkit.getDefaultToolkit().beep();
                setCursor(null);
                JOptionPane.showMessageDialog(null, "Plan released !\n");
            } else {
                JOptionPane.showMessageDialog(null, Helper.ERR0028_EMPTY_LOAD_PLAN, "Erreur fin de chargement", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_release_plan_btnActionPerformed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        reloadPlansData();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void new_plan_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_new_plan_btnActionPerformed
        new WAREHOUSE_DISPATCH_UI0004_NEW_PLAN(this, true);
    }//GEN-LAST:event_new_plan_btnActionPerformed

    private void delete_plan_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_plan_btnActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Voulez-vous supprimer le plan de chargement sélectionné ?", "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == 0) {
            Helper.startSession();
            Integer id = Integer.valueOf(plan_num_label.getText());
            Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_ID)
                    .setParameter("id", id);
            Helper.sess.getTransaction().commit();
            List result = query.list();
            if (result.isEmpty()) {
                Helper.startSession();
                query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_BY_ID);
                query.setParameter("id", id);
                Helper.sess.getTransaction().commit();
                result = query.list();
                LoadPlan plan = (LoadPlan) result.get(0);

                //Delete Plan Lines
                //############ REMOVE THE HARNESS FROM CONTAINER ###############
                query = Helper.sess.createQuery(HQLHelper.DEL_LOAD_PLAN_LINE_BY_PLAN_ID);
                query.setParameter("load_plan_id", plan.getId());
                query.executeUpdate();

                plan.delete(plan);

                //Reload Load Plan list
                this.reloadPlansData();

                //Reset Load Plan Lines table
                this.reset_load_plan_lines_table_content();

                clearGui();
                //Go back to step S020
                state = new S020_PalletNumberScan();
                WarehouseHelper.warehouse_out_context.setState(state);
            } else {
                JOptionPane.showMessageDialog(null, Helper.ERR0034_NOT_EMPTY_LOAD_PLAN, "Delete not allowed !", ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_delete_plan_btnActionPerformed

    private void scan_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scan_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scan_txtActionPerformed

    private void load_plans_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_load_plans_listActionPerformed
        new WAREHOUSE_DISPATCH_UI0006_LIST(this, true).setVisible(true);
    }//GEN-LAST:event_load_plans_listActionPerformed

    private void pallet_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pallet_detailsActionPerformed
        
    }//GEN-LAST:event_pallet_detailsActionPerformed

    private void destinations_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_destinations_boxItemStateChanged
        if (!"#".equals(plan_num_label.getText()) && plan_num_label != null) {

            //Set the values of destination and pile labels help
            if (destinations_box.getItemCount() != 0) {
                destination_label_help.setText(destinations_box.getSelectedItem().toString());
            }
            pile_label_help.setText(piles_box.getSelectedItem().toString());
            filterPlanLines(false);
        }
    }//GEN-LAST:event_destinations_boxItemStateChanged

    private void set_packaging_pile_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set_packaging_pile_btnActionPerformed
        new WAREHOUSE_DISPATCH_UI0008_SET_PACKAGING_OF_PILE(this, true, WarehouseHelper.temp_load_plan, destinations_box.getSelectedItem().toString());
    }//GEN-LAST:event_set_packaging_pile_btnActionPerformed

    private void piles_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_piles_boxItemStateChanged
        //Set the values of destination and pile labels help
        try {
            destination_label_help.setText(destinations_box.getSelectedItem().toString());
            pile_label_help.setText(piles_box.getSelectedItem().toString());
            filterPlanLines(false);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_piles_boxItemStateChanged

    private void btn_filter_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filter_okActionPerformed
        filterPlanLines(false);
    }//GEN-LAST:event_btn_filter_okActionPerformed

    private void export_plan_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_plan_btnActionPerformed
        //Create the excel workbook
        Workbook wb = new HSSFWorkbook();
        Sheet sheet1 = wb.createSheet("PILES_DETAILS");
        Sheet sheet2 = wb.createSheet("PILES_GROUPED");
        Sheet sheet3 = wb.createSheet("VSPK_DATALOAD");
        Sheet sheet4 = wb.createSheet("PACKAGING_MOUVEMENTS");
        Sheet sheet5 = wb.createSheet("TOTAL_STOCK_PER_FDP");
        CreationHelper createHelper = wb.getCreationHelper();

        //######################################################################
        //##################### SHEET 1 : PILES DETAILS ########################
        //Initialiser les entête du fichier
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet1.createRow((short) 0);
        // Create a cell and put a value in it.    

        row.createCell(0).setCellValue("N° LINE");
        row.createCell(1).setCellValue("TYPE");
        row.createCell(2).setCellValue("PILE NUM");
        row.createCell(3).setCellValue("PALLET NUM");
        row.createCell(4).setCellValue("CUSTOMER PN");
        row.createCell(5).setCellValue("INDEX");
        row.createCell(6).setCellValue("LEONI PN");
        row.createCell(7).setCellValue("QTY");
        row.createCell(8).setCellValue("PACK TYPE");
        row.createCell(9).setCellValue("ORDER NUM");
        row.createCell(10).setCellValue("DISPATCH LABEL NO");
        row.createCell(11).setCellValue("DESTINATION");

        //Load lines of the actual loading plan
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_PLAN_ID_ASC);
        query.setParameter("loadPlanId", Integer.valueOf(plan_num_label.getText()));

        Helper.sess.getTransaction().commit();
        List result = query.list();

        short sheetPointer = 1;

        for (Object o : result) {
            LoadPlanLine lpl = (LoadPlanLine) o;
            row = sheet1.createRow(sheetPointer);
            row.createCell(0).setCellValue(lpl.getId());
            row.createCell(1).setCellValue(lpl.getHarnessType());
            row.createCell(2).setCellValue(lpl.getPileNum());
            row.createCell(3).setCellValue(Integer.valueOf(lpl.getPalletNumber()));
            row.createCell(4).setCellValue(Integer.valueOf(lpl.getHarnessPart()));
            row.createCell(5).setCellValue(lpl.getHarnessIndex());
            row.createCell(6).setCellValue(lpl.getSupplierPart());
            row.createCell(7).setCellValue(lpl.getQty());
            row.createCell(8).setCellValue(lpl.getPackType());
            row.createCell(9).setCellValue(lpl.getOrderNum());
            row.createCell(10).setCellValue(lpl.getDestinationWh());
            sheetPointer++;
        }

        //######################################################################
        //##################### SHEET 2 : PILES SUMMARY ########################
        short sheet2Pointer = 0;

        Row row2 = sheet2.createRow(sheet2Pointer);
        sheet2Pointer++;
        // Create a cell and put a value in it.    

        row2.createCell(0).setCellValue("TYPE");
        row2.createCell(1).setCellValue("PILE NUM");
        row2.createCell(2).setCellValue("CUSTOMER PN");
        row2.createCell(3).setCellValue("INDEX");
        row2.createCell(4).setCellValue("LEONI PN");
        row2.createCell(5).setCellValue("TOTAL QTY");
        row2.createCell(6).setCellValue("UCS QTY");
        row2.createCell(7).setCellValue("PACK TYPE");
        row2.createCell(8).setCellValue("NBRE PACK");
        row2.createCell(9).setCellValue("ORDER NUM");
        row2.createCell(10).setCellValue("DESTINATION");

        Helper.startSession();

        SQLQuery query2 = Helper.sess.createSQLQuery(String.format(HQLHelper.GET_LOAD_PLAN_LINE_GROUPED_BY_PILES, plan_num_label.getText()));

        query2.addScalar("harness_type", StandardBasicTypes.STRING) //0
                .addScalar("pile_num", StandardBasicTypes.INTEGER) //1
                .addScalar("harness_part", StandardBasicTypes.INTEGER)//2
                .addScalar("harness_index", StandardBasicTypes.STRING)//3
                .addScalar("supplier_part", StandardBasicTypes.STRING)//4
                .addScalar("total_qty", StandardBasicTypes.INTEGER)//5
                .addScalar("qty", StandardBasicTypes.INTEGER)//6
                .addScalar("pack_type", StandardBasicTypes.STRING)//7
                .addScalar("nbre_pack", StandardBasicTypes.INTEGER)//8
                .addScalar("order_num", StandardBasicTypes.INTEGER)//9
                .addScalar("destination_wh", StandardBasicTypes.STRING);//10

        List<Object[]> result2 = query2.list();
        Helper.sess.getTransaction().commit();
        for (Object[] obj : result2) {
            row2 = sheet2.createRow(sheet2Pointer);

            row2.createCell(0).setCellValue((String) obj[0]);
            row2.createCell(1).setCellValue((Integer) obj[1]);
            row2.createCell(2).setCellValue((Integer) obj[2]);
            row2.createCell(3).setCellValue((String) obj[3]);
            row2.createCell(4).setCellValue((String) obj[4]);
            row2.createCell(5).setCellValue((Integer) obj[5]);
            row2.createCell(6).setCellValue((Integer) obj[6]);
            row2.createCell(7).setCellValue((String) obj[7]);
            row2.createCell(8).setCellValue((Integer) obj[8]);
            try {
                row2.createCell(9).setCellValue((Integer) obj[9]);
            } catch (Exception e) {
                row2.createCell(9).setCellValue("");
            }
            row2.createCell(10).setCellValue((String) obj[10]);
            sheet2Pointer++;
        }

        //######################################################################
        //##################### SHEET 3 : VSPK DATALOAD ########################
        short sheet3Pointer = 0;

        Row row3 = sheet3.createRow(sheet3Pointer);
        sheet3Pointer++;
        // Create a cell and put a value in it.    

        row3.createCell(0).setCellValue("Data");
        row3.createCell(1).setCellValue("Key Strokes");
        row3.createCell(2).setCellValue("WAREHOUSE");
        row3.createCell(3).setCellValue("Key Strokes");
        row3.createCell(4).setCellValue("PACK ITEM");
        row3.createCell(5).setCellValue("ENTER");
        row3.createCell(6).setCellValue("Key Strokes");
        row3.createCell(7).setCellValue("PLANT");
        row3.createCell(8).setCellValue("ORDER NUMBER");
        row3.createCell(9).setCellValue("PART NUMBER");
        row3.createCell(10).setCellValue("Key Strokes");
        row3.createCell(11).setCellValue("TOTAL QTY");
        row3.createCell(12).setCellValue("Key Strokes");
        row3.createCell(13).setCellValue("PART NUMBER");
        row3.createCell(14).setCellValue("Key Strokes");
        row3.createCell(15).setCellValue("INIT PACKAGING");
        row3.createCell(16).setCellValue("PACK TYPE");
        row3.createCell(17).setCellValue("ENTER");
        row3.createCell(18).setCellValue("STD PACK");
        row3.createCell(19).setCellValue("Key Strokes");
        row3.createCell(20).setCellValue("POSITION NUMBER");
        row3.createCell(21).setCellValue("Key Strokes");
        row3.createCell(22).setCellValue("DESTINATION");

        Helper.startSession();

        SQLQuery query3 = Helper.sess.createSQLQuery(String.format(HQLHelper.GET_LOAD_PLAN_LINE_GROUPED_BY_PILES, plan_num_label.getText()));

        query3.addScalar("harness_type", StandardBasicTypes.STRING)
                .addScalar("pile_num", StandardBasicTypes.INTEGER)
                .addScalar("harness_part", StandardBasicTypes.INTEGER)
                .addScalar("harness_index", StandardBasicTypes.STRING)
                .addScalar("supplier_part", StandardBasicTypes.STRING)
                .addScalar("total_qty", StandardBasicTypes.INTEGER)
                .addScalar("qty", StandardBasicTypes.INTEGER)
                .addScalar("pack_type", StandardBasicTypes.STRING)
                .addScalar("nbre_pack", StandardBasicTypes.INTEGER)
                .addScalar("order_num", StandardBasicTypes.STRING)
                .addScalar("destination_wh", StandardBasicTypes.STRING);

        List<Object[]> result3 = query3.list();
        Helper.sess.getTransaction().commit();
        int pilePointer = 0;
        //int packItemPointer = 1;
        for (Object[] obj : result3) {
            row3 = sheet3.createRow(sheet3Pointer);

            //Check if it's a new pile
            if (pilePointer != (Integer) obj[1]) {
                pilePointer = (Integer) obj[1];
                row3.createCell(0).setCellValue("\\{DELETE 9}");
            }
            row3.createCell(0).setCellValue("\\{DELETE 9}");
            row3.createCell(1).setCellValue("\\{TAB 2}");
            row3.createCell(2).setCellValue(Helper.PROP.getProperty("WH_FINISH_GOODS"));     //Warehouse
            row3.createCell(3).setCellValue("\\{TAB 8}");
            row3.createCell(4).setCellValue((int) 1);
            row3.createCell(5).setCellValue("ENT");
            row3.createCell(6).setCellValue("\\{TAB 2}");
            row3.createCell(7).setCellValue((int) 64);         //PLANT NUM
            try {
                row3.createCell(8).setCellValue(Integer.valueOf((String) obj[9]));      //ORDER NUM
            } catch (Exception e) {
                row3.createCell(8).setCellValue("?");
            }
            /*if (obj[9] != null && obj[9].toString().length() != 0) {
             row3.createCell(8).setCellValue(Integer.valueOf((String) obj[9]));      //ORDER NUM
             } else {
             row3.createCell(8).setCellValue("?");
             }*/
            row3.createCell(9).setCellValue((String) obj[4]);      //LPN
            row3.createCell(10).setCellValue("\\{TAB 2}");
            row3.createCell(11).setCellValue((int) obj[5]);     //TOTAL QTY
            row3.createCell(12).setCellValue("\\{TAB}");
            row3.createCell(13).setCellValue((String) obj[4]);      //LPN
            row3.createCell(14).setCellValue("\\{TAB 3}");
            row3.createCell(15).setCellValue((int) 1);
            row3.createCell(16).setCellValue((String) obj[7]);      //PACK TYPE
            row3.createCell(17).setCellValue("ENT");
            row3.createCell(18).setCellValue((int) obj[6]);     //STD PACK
            row3.createCell(19).setCellValue("\\{TAB 6}");
            row3.createCell(20).setCellValue("POSITION NUM " + pilePointer);
            row3.createCell(21).setCellValue("\\{F9}");
            row3.createCell(22).setCellValue((String) obj[10]);

            //packItemPointer++;
            sheet3Pointer++;
        }

        //######################################################################
        //##################### SHEET 4 : PACKAGIN MOUVEMENTS ########################
        short sheet4Pointer = 0;

        Row row4 = sheet4.createRow(sheet4Pointer);
        sheet4Pointer++;
        // Create a cell and put a value in it.    

        row4.createCell(0).setCellValue("PACK INTEM");
        row4.createCell(1).setCellValue("QTY");
        row4.createCell(2).setCellValue("WAREHOUSE");
        row4.createCell(3).setCellValue("CONSIG. NUM");

        Helper.startSession();

        String q4 = "SELECT pack_item, SUM(qty) as qty, warehouse, load_plan_id FROM \n"
                + "(SELECT pack_item, SUM(quantity) as qty, warehouse, CAST(document_id AS INT) AS load_plan_id FROM packaging_stock_movement \n"
                + "WHERE document_id = '" + this.plan_num_label.getText() + "' GROUP BY pack_item, warehouse, load_plan_id\n"
                + "UNION ALL \n"
                + "SELECT pack_item, SUM(qty) as qty, destination AS warehouse, load_plan_id \n"
                + "FROM load_plan_line_packaging l WHERE load_plan_id = " + this.plan_num_label.getText()
                + " GROUP BY pack_item, warehouse, load_plan_id ) t GROUP BY pack_item,warehouse, load_plan_id "
                + "ORDER BY warehouse ASC;";

        SQLQuery query4 = Helper.sess.createSQLQuery(q4);
        query2.addScalar("pack_item", StandardBasicTypes.STRING)
                .addScalar("qty", StandardBasicTypes.INTEGER)
                .addScalar("warehouse", StandardBasicTypes.STRING)
                .addScalar("load_plan_id", StandardBasicTypes.INTEGER);

        List<Object[]> result4 = query4.list();
        Helper.sess.getTransaction().commit();
        for (Object[] obj : result4) {
            row4 = sheet4.createRow(sheet4Pointer);
            row4.createCell(0).setCellValue((String) obj[0]);
            row4.createCell(1).setCellValue((Float) obj[1]);
            row4.createCell(2).setCellValue((String) obj[2]);
            row4.createCell(3).setCellValue((Integer) obj[3]);
            sheet4Pointer++;
        }
        /*
         ####################SHEET 5 : LABEST MASK ########################*/
        short sheet5Pointer = 0;

        Row row5 = sheet5.createRow(sheet5Pointer);
        sheet5Pointer++;
        // Create a cell and put a value in it.    

        row5.createCell(0).setCellValue("TYPE");
        row5.createCell(1).setCellValue("CPN");
        row5.createCell(2).setCellValue("INDEX");
        row5.createCell(3).setCellValue("LPN");
        row5.createCell(4).setCellValue("TOTAL QTY");
        //row5.createCell(5).setCellValue("PRICE");
        //row5.createCell(6).setCellValue("STD TIME");
        row5.createCell(7).setCellValue("ORDER NUM");
        row5.createCell(8).setCellValue("DESTINATION");

        Helper.startSession();

        SQLQuery query5 = Helper.sess.createSQLQuery(String.format(HQLHelper.GET_LOAD_PLAN_STOCK_PER_FDP, plan_num_label.getText()));
        query5.addScalar("harness_type", StandardBasicTypes.STRING)
                .addScalar("harness_part", StandardBasicTypes.INTEGER)
                .addScalar("harness_index", StandardBasicTypes.STRING)
                .addScalar("supplier_part", StandardBasicTypes.STRING)
                .addScalar("total_qty", StandardBasicTypes.INTEGER)
                .addScalar("order_num", StandardBasicTypes.STRING)
                .addScalar("destination_wh", StandardBasicTypes.STRING);

        List<Object[]> result5 = query5.list();
        Helper.sess.getTransaction().commit();
        for (Object[] obj : result5) {
            row5 = sheet5.createRow(sheet5Pointer);
            row5.createCell(0).setCellValue((String) obj[0]);
            row5.createCell(1).setCellValue((Integer) obj[1]);
            row5.createCell(2).setCellValue((String) obj[2]);
            row5.createCell(3).setCellValue((String) obj[3]);
            row5.createCell(4).setCellValue((Integer) obj[4]);
            try {
                row5.createCell(5).setCellValue((Integer) obj[5]);
            } catch (Exception e) {
                row5.createCell(5).setCellValue("");
            }
            row5.createCell(6).setCellValue((String) obj[6]);
            sheet5Pointer++;
        }

        //Past the workbook to the file chooser
        new JDialogExcelFileChooser(this, true, wb).setVisible(true);
    }//GEN-LAST:event_export_plan_btnActionPerformed

    private void edit_plan_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_plan_btnActionPerformed
        new WAREHOUSE_DISPATCH_UI0005_EDIT_PLAN(this, true, WarehouseHelper.temp_load_plan);
    }//GEN-LAST:event_edit_plan_btnActionPerformed

    private void txt_filter_partKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filter_partKeyTyped
        filterPlanLines(false);
    }//GEN-LAST:event_txt_filter_partKeyTyped

    private void destinations_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinations_boxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_destinations_boxActionPerformed

    private void txt_filter_pal_numberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filter_pal_numberKeyTyped
        filterPlanLines(false);
    }//GEN-LAST:event_txt_filter_pal_numberKeyTyped

    private void txt_filter_partActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_filter_partActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_filter_partActionPerformed

    public void loadPlanDataInGui(int id) {

        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_BY_ID);
        query.setParameter("id", id);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        LoadPlan plan = (LoadPlan) result.get(0);
        WarehouseHelper.temp_load_plan = plan;

        //Load destinations of the plan
        if (loadDestinations(Integer.valueOf(id))) {
            loadPlanDataToLabels(plan, destinations_box.getItemAt(0).toString());
            reloadPlanLinesData(Integer.valueOf(id), destinations_box.getItemAt(0).toString());

            //Disable delete button if the plan is CLOSED
            if (WarehouseHelper.LOAD_PLAN_STATE_CLOSED.equals(plan.getPlanState())) {
                delete_plan_btn.setEnabled(false);
                release_plan_btn.setEnabled(false);
                export_plan_btn.setEnabled(true);
                edit_plan_btn.setEnabled(false);
                set_packaging_pile_btn.setEnabled(true);
                destinations_box.setEnabled(true);
                piles_box.setEnabled(true);
                scan_txt.setEnabled(false);
                txt_filter_part.setEnabled(true);
            } else { // The plan still Open
                delete_plan_btn.setEnabled(true);
                release_plan_btn.setEnabled(true);
                export_plan_btn.setEnabled(true);
                edit_plan_btn.setEnabled(true);
                set_packaging_pile_btn.setEnabled(true);
                destinations_box.setEnabled(true);
                destinations_box.setSelectedIndex(0);
                piles_box.setEnabled(true);
                scan_txt.setEnabled(true);
                txt_filter_part.setEnabled(true);
            }
        }

        filterPlanLines(false);
        filterPlanLines(false);
    }

    private void plan_id_filterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_plan_id_filterKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loadPlanDataInGui(Integer.valueOf(plan_id_filter.getText()));
        }
    }//GEN-LAST:event_plan_id_filterKeyPressed

    private void pallet_details_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pallet_details_btnActionPerformed
        new PACKAGING_UI0010_PalletDetails(this, rootPaneCheckingEnabled, true, true, true, true).setVisible(true);
    }//GEN-LAST:event_pallet_details_btnActionPerformed

    private void plan_list_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plan_list_btnActionPerformed
        new WAREHOUSE_DISPATCH_UI0006_LIST(this, true).setVisible(true);
    }//GEN-LAST:event_plan_list_btnActionPerformed

    private void clearGui() {

        this.cleanDataLabels();

        //Clear mode2_context temp vars
        WarehouseHelper.warehouse_out_context.clearAllVars();

        //Clear lines from Jtable
        reset_load_plan_lines_table_content();

        //Disable delete button
        delete_plan_btn.setEnabled(false);

        //Disable End load button
        release_plan_btn.setEnabled(false);

        //Disable Export Excel button
        export_plan_btn.setEnabled(false);

        //Disable Edit plan button
        edit_plan_btn.setEnabled(false);

        destinations_box.setEnabled(false);
        piles_box.setEnabled(false);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_filter_ok;
    private javax.swing.JLabel connectedUserName_label;
    private javax.swing.JLabel create_time_label;
    private javax.swing.JLabel create_user_label;
    private javax.swing.JButton delete_plan_btn;
    private javax.swing.JLabel destination_label_help;
    private javax.swing.JComboBox destinations_box;
    private javax.swing.JLabel dispatch_date_label;
    private javax.swing.JButton edit_plan_btn;
    private javax.swing.JButton export_plan_btn;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable load_plan_lines_table;
    private javax.swing.JTable load_plan_table;
    private javax.swing.JMenuItem load_plans_list;
    private javax.swing.JTextField message_label;
    private javax.swing.JButton new_plan_btn;
    private javax.swing.JMenuItem pallet_details;
    private javax.swing.JButton pallet_details_btn;
    private javax.swing.JLabel pile_label_help;
    private javax.swing.JComboBox piles_box;
    private javax.swing.JFormattedTextField plan_id_filter;
    private javax.swing.JButton plan_list_btn;
    private javax.swing.JLabel plan_num_label;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JRadioButton radio_btn_20;
    private javax.swing.JRadioButton radio_btn_40;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JLabel release_date_label;
    private javax.swing.JButton release_plan_btn;
    private javax.swing.JTextField scan_txt;
    private javax.swing.JButton set_packaging_pile_btn;
    private javax.swing.JLabel state_label;
    private javax.swing.JScrollPane table_scroll;
    private javax.swing.JLabel time_label1;
    private javax.swing.JLabel time_label2;
    private javax.swing.JLabel time_label3;
    private javax.swing.JLabel time_label4;
    private javax.swing.JLabel time_label5;
    private javax.swing.JLabel time_label6;
    private javax.swing.JLabel time_label7;
    private javax.swing.JTextField txt_filter_pal_number;
    private javax.swing.JTextField txt_filter_part;
    private javax.swing.JLabel txt_nbreLigne;
    // End of variables declaration//GEN-END:variables

    public void loadPlanDataInGui() {
        String id = plan_num_label.getText();
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_BY_ID);
        query.setParameter("id", Integer.valueOf(id));

        Helper.sess.getTransaction().commit();
        List result = query.list();
        LoadPlan plan = (LoadPlan) result.get(0);
        WarehouseHelper.temp_load_plan = plan;
        loadPlanDataToLabels(plan, "");
        reloadPlanLinesData(Integer.valueOf(id), null);
        loadDestinations(Integer.valueOf(id));
        //Disable delete button if the plan is CLOSED
        if (WarehouseHelper.LOAD_PLAN_STATE_CLOSED.equals(plan.getPlanState())) {
            delete_plan_btn.setEnabled(false);
            release_plan_btn.setEnabled(false);
            export_plan_btn.setEnabled(true);
            edit_plan_btn.setEnabled(false);
            destinations_box.setEnabled(true);
            piles_box.setEnabled(false);
            set_packaging_pile_btn.setEnabled(false);
        } else {
            delete_plan_btn.setEnabled(true);
            release_plan_btn.setEnabled(true);
            export_plan_btn.setEnabled(true);
            edit_plan_btn.setEnabled(true);
            destinations_box.setEnabled(true);
            piles_box.setEnabled(true);
            set_packaging_pile_btn.setEnabled(true);
        }
    }

    /**
     * Filter the plan lines according to the give values
     *
     * @param pass
     */
    public void filterPlanLines(boolean pass) {

        if (!"*".equals(piles_box.getSelectedItem().toString())) {
            try {
                int pile = Integer.parseInt(piles_box.getSelectedItem().toString());
                filterPlanLines(
                        Integer.valueOf(plan_num_label.getText()),
                        destinations_box.getSelectedItem().toString(),
                        txt_filter_part.getText(), pile);
                
                filterPlanLines(
                        Integer.valueOf(plan_num_label.getText()),
                        destinations_box.getSelectedItem().toString(),
                        txt_filter_part.getText(), 0);

            } catch (NumberFormatException e) {
                filterPlanLines(
                        Integer.valueOf(plan_num_label.getText()),
                        destinations_box.getSelectedItem().toString(),
                        txt_filter_part.getText(), 0);
                filterPlanLines(
                        Integer.valueOf(plan_num_label.getText()),
                        destinations_box.getSelectedItem().toString(),
                        txt_filter_part.getText(), 0);
            }
        } else {
            if (!plan_num_label.getText().equals("#")) {                
                filterPlanLines(
                        Integer.valueOf(plan_num_label.getText()),
                        destinations_box.getSelectedItem().toString(),
                        txt_filter_part.getText(), 0);
                filterPlanLines(
                        Integer.valueOf(plan_num_label.getText()),
                        destinations_box.getSelectedItem().toString(),
                        txt_filter_part.getText(), 0);
            }
        }
    }
}
