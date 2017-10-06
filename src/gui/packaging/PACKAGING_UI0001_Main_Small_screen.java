/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging;

import java.util.List;
import javax.swing.JFrame;
import helper.Helper;
import helper.HQLHelper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseEngineLabelTmp;
import entity.ConfigBarcode;
import entity.HisLogin;
import entity.ManufactureUsers;
import gui.warehouse_fg_reception.WAREHOUSE_FG_UI0002_PALLET_LIST;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import scanner.GUI;
import gui.packaging.state.S010_UserCodeScan;
import gui.packaging.state.State;

/**
 *
 * @author user
 */
public final class PACKAGING_UI0001_Main_Small_screen extends javax.swing.JFrame {

    String str = null;
    State state = Helper.context.getState();
    Vector<String> container_table_header = new Vector<String>();
    Vector container_table_data = new Vector();

    /* "Pallet number" Column index in "container_table" */
    private static int PALLET_NUMBER_COLINDEX = 0;

    //########################################################################
    public PACKAGING_UI0001_Main_Small_screen() {
    }

    /**
     * Creates new form UI0000_Login
     *
     * @param context
     */
    public PACKAGING_UI0001_Main_Small_screen(Object[] context, JFrame parent) {
        initComponents();
        //Initialiser les valeurs globales de test (Pattern Liste,...)
        Helper.startSession();

        initGui();
    }

    public void initGui() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        // Set jTable Row Style
        //Centrer le jframe dans le screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.setIconLabel(state.getImg());

        this.connectedUserName_label.setHorizontalAlignment(JLabel.CENTER);
        //-----------------------

        //Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = dim.width;
        int ySize = dim.height;
        this.setSize(xSize, ySize);

        panel_top.setLayout(new BorderLayout());
        panel_top.setBackground(Color.DARK_GRAY);
        //panel_top.setPreferredSize(new Dimension(xSize, (int) (Math.round(ySize * 0.90))));
        this.add(panel_top, BorderLayout.NORTH);

        panel_bottom.setLayout(new BorderLayout());
        //panel_bottom.setPreferredSize(new Dimension((int) (Math.round(xSize * 0.80)), (int) (Math.round(ySize * 0.20))));
        this.add(panel_bottom, BorderLayout.SOUTH);
        //container_table.setPreferredSize(new Dimension((int) (Math.round(xSize * 0.60)), (int) (Math.round(ySize * 0.80))));
        //------------------------
        //Initialize table header                        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        img_lbl.setHorizontalAlignment(JLabel.CENTER);
        img_lbl.setVerticalAlignment(JLabel.CENTER);

        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        //Initialize double clique on table row
        this.initContainerTableDoubleClick();

        Helper.loadProjectsInJbox(harnessTypeBox);
        Helper.loadProjectsInJbox(harnessTypeFilterBox);

        //this.day_date_picker.setDate(new Date());
        //Focus on scann textbox        
        harnessTypeBox.requestFocus();

        //Show the jframe
        this.setVisible(true);

        //Maximaze the jframe
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //########################################################################
    //######################## MAIN TABLE METHODS ############################
    //########################################################################
    public void load_container_table_header() {
        this.reset_container_table_content();
        container_table_header.add("Pallet Number");
        container_table_header.add("Harness Part");
        container_table_header.add("Index");
        container_table_header.add("Pack Type");
        container_table_header.add("Quantity Expected");
        container_table_header.add("Quantity Read");
        container_table_header.add("Create Time");
        container_table_header.add("Harness Type");
        container_table_header.add("State");

        container_table.setModel(new DefaultTableModel(container_table_data, container_table_header));
        container_table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        container_table.setAutoCreateRowSorter(true);
    }

    public void reset_container_table_content() {
        container_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(container_table_data, container_table_header);
        container_table.setModel(dataModel);
    }

    public void reload_container_table_data(List resultList) {
        this.reset_container_table_content();

        for (Object o : resultList) {
            BaseContainer base_container = (BaseContainer) o;
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(base_container.getPalletNumber());
            oneRow.add(base_container.getHarnessPart());
            oneRow.add(base_container.getHarnessIndex());
            oneRow.add(base_container.getPackType());
            oneRow.add(base_container.getQtyExpected());
            oneRow.add(base_container.getQtyRead());
            oneRow.add(base_container.getCreateTimeString("dd/MM/yy HH:mm"));
            oneRow.add(base_container.getHarnessType());
            oneRow.add(base_container.getContainerState());
            container_table_data.add(oneRow);
        }
        container_table.setModel(new DefaultTableModel(container_table_data, container_table_header));

        //Initialize default style for table container
        setContainerTableRowsStyle();
    }

    public void setContainerTableRowsStyle() {
        //Initialize default style for table container

        //#######################
        container_table.setFont(new Font(String.valueOf(Helper.PROP.getProperty("JTABLE_FONT")), Font.BOLD, Integer.valueOf(Helper.PROP.getProperty("JTABLE_FONTSIZE"))));
        container_table.setRowHeight(Integer.valueOf(Helper.PROP.getProperty("JTABLE_ROW_HEIGHT")));
        container_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                String status = (String) table.getModel().getValueAt(row, Helper.PALLET_STATE_COL_INDEX);
                //############### OPEN
                if (Helper.PALLET_OPEN.equals(status)) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);
                } //############### CLOSED
                else if (Helper.PALLET_CLOSED.equals(status)) {
                    setBackground(Color.LIGHT_GRAY);
                    setForeground(Color.BLACK);
                } //############### QUARANTAINE
                else if (Helper.PALLET_QUARANTAINE.equals(status)) {
                    setBackground(Color.RED);
                    setForeground(Color.BLACK);
                } //############### WAITING
                else if (Helper.PALLET_WAITING.equals(status)) {
                    setBackground(Color.CYAN);
                    setForeground(Color.BLACK);
                } //############### STORED
                else if (Helper.PALLET_STORED.equals(status)) {
                    setBackground(Color.ORANGE);
                    setForeground(Color.BLACK);
                }//############### DROPPED
                else if (Helper.PALLET_SHIPPED.equals(status)) {
                    setBackground(Color.BLUE);
                    setForeground(Color.BLACK);
                }

                setHorizontalAlignment(JLabel.CENTER);

                return this;
            }
        });
        //#######################
        this.disableEditingTable();
    }

    public void disableEditingTable() {
        for (int c = 0; c < container_table.getColumnCount(); c++) {
            Class<?> col_class = container_table.getColumnClass(c);
            container_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    private void initContainerTableDoubleClick() {
        this.container_table.addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            System.out.println("Helper.context.getUser().getAccessLevel()" + Helper.context.getUser().getAccessLevel());
                            if (Helper.context.getUser().getAccessLevel() == Helper.PROFIL_ADMIN) {
                                new PACKAGING_UI0010_PalletDetails(null, rootPaneCheckingEnabled, String.valueOf(container_table.getValueAt(container_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), true, true, true).setVisible(true);
                            } else {
                                new PACKAGING_UI0010_PalletDetails(null, rootPaneCheckingEnabled, String.valueOf(container_table.getValueAt(container_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), false, false, false).setVisible(true);
                            }
                        }
                    }
                }
        );
    }

    public void enableOperatorMenus() {
        this.menu02_process.setVisible(true);
        this.menu020_waiting.setVisible(true);
    }

    public void disableOperatorMenus() {
        this.menu02_process.setVisible(false);
        this.menu020_waiting.setVisible(false);
    }

    public void enableAdminMenus() {
        this.enableOperatorMenus();
        this.menu03_config.setVisible(true);

    }

    public void disableAdminMenus() {
        this.disableOperatorMenus();
        this.menu03_config.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        panel_top = new javax.swing.JPanel();
        img_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        container_table = new javax.swing.JTable();
        requestedPallet_label = new javax.swing.JLabel();
        connectedUserName_label = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        refresh = new javax.swing.JButton();
        harnessTypeFilterBox = new javax.swing.JComboBox();
        panel_bottom = new javax.swing.JPanel();
        scan_txtbox = new javax.swing.JTextField();
        harnessTypeBox = new javax.swing.JComboBox();
        menu_bar = new javax.swing.JMenuBar();
        menu01_report = new javax.swing.JMenu();
        menu011_prod_statistics = new javax.swing.JMenuItem();
        menu013_pallet_list = new javax.swing.JMenuItem();
        menu018_open_pallet_list = new javax.swing.JMenuItem();
        menu010_pallet_details = new javax.swing.JMenuItem();
        menu012_deleted_pallet = new javax.swing.JMenuItem();
        menu012_harness_details = new javax.swing.JMenuItem();
        menu012_deleted_harness = new javax.swing.JMenuItem();
        menu017_ucs = new javax.swing.JMenuItem();
        menu02_process = new javax.swing.JMenu();
        menu020_waiting = new javax.swing.JMenuItem();
        menu03_config = new javax.swing.JMenu();
        menu030 = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Packaging Module");
        setBackground(new java.awt.Color(51, 51, 51));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panel_top.setBackground(new java.awt.Color(255, 255, 255));
        panel_top.setForeground(new java.awt.Color(255, 255, 255));
        panel_top.setMinimumSize(new java.awt.Dimension(800, 400));
        panel_top.setPreferredSize(new java.awt.Dimension(1364, 780));
        panel_top.setRequestFocusEnabled(false);

        img_lbl.setBackground(new java.awt.Color(255, 255, 255));
        img_lbl.setForeground(new java.awt.Color(255, 255, 255));
        img_lbl.setText(" ");

        container_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(container_table);

        requestedPallet_label.setBackground(new java.awt.Color(255, 0, 0));
        requestedPallet_label.setFont(new java.awt.Font("Courier New", 1, 36)); // NOI18N
        requestedPallet_label.setForeground(new java.awt.Color(255, 255, 255));
        requestedPallet_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        connectedUserName_label.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        connectedUserName_label.setForeground(new java.awt.Color(255, 255, 255));

        refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/refresh.png"))); // NOI18N
        refresh.setText("Actualiser");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        harnessTypeFilterBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                harnessTypeFilterBoxItemStateChanged(evt);
            }
        });
        harnessTypeFilterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harnessTypeFilterBoxActionPerformed(evt);
            }
        });

        panel_bottom.setBackground(new java.awt.Color(51, 51, 51));

        scan_txtbox.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        scan_txtbox.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        scan_txtbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scan_txtboxKeyPressed(evt);
            }
        });

        harnessTypeBox.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        harnessTypeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        harnessTypeBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                harnessTypeBoxItemStateChanged(evt);
            }
        });
        harnessTypeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harnessTypeBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_bottomLayout = new javax.swing.GroupLayout(panel_bottom);
        panel_bottom.setLayout(panel_bottomLayout);
        panel_bottomLayout.setHorizontalGroup(
            panel_bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_bottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(harnessTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scan_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_bottomLayout.setVerticalGroup(
            panel_bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_bottomLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scan_txtbox, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(harnessTypeBox))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_topLayout = new javax.swing.GroupLayout(panel_top);
        panel_top.setLayout(panel_topLayout);
        panel_topLayout.setHorizontalGroup(
            panel_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(connectedUserName_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_topLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1304, 1304, 1304)
                .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_topLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requestedPallet_label, javax.swing.GroupLayout.PREFERRED_SIZE, 1345, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(img_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 1244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_topLayout.createSequentialGroup()
                        .addComponent(harnessTypeFilterBox, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_topLayout.setVerticalGroup(
            panel_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_topLayout.createSequentialGroup()
                .addComponent(connectedUserName_label, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(img_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(requestedPallet_label, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(harnessTypeFilterBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panel_bottom, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114)
                .addGroup(panel_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        menu01_report.setText("Report");

        menu011_prod_statistics.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menu011_prod_statistics.setText("Statistiques production");
        menu011_prod_statistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu011_prod_statisticsActionPerformed(evt);
            }
        });
        menu01_report.add(menu011_prod_statistics);

        menu013_pallet_list.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menu013_pallet_list.setText("Liste des palettes");
        menu013_pallet_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu013_pallet_listActionPerformed(evt);
            }
        });
        menu01_report.add(menu013_pallet_list);

        menu018_open_pallet_list.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menu018_open_pallet_list.setText("Palettes ouvertes");
        menu018_open_pallet_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu018_open_pallet_listActionPerformed(evt);
            }
        });
        menu01_report.add(menu018_open_pallet_list);

        menu010_pallet_details.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        menu010_pallet_details.setText("Détails palette");
        menu010_pallet_details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu010_pallet_detailsActionPerformed(evt);
            }
        });
        menu01_report.add(menu010_pallet_details);

        menu012_deleted_pallet.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menu012_deleted_pallet.setText("Palettes annulées");
        menu012_deleted_pallet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu012_deleted_palletActionPerformed(evt);
            }
        });
        menu01_report.add(menu012_deleted_pallet);

        menu012_harness_details.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        menu012_harness_details.setText("Détails faisceau");
        menu012_harness_details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu012_harness_detailsActionPerformed(evt);
            }
        });
        menu01_report.add(menu012_harness_details);

        menu012_deleted_harness.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menu012_deleted_harness.setText("Faisceaux annulés");
        menu012_deleted_harness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu012_deleted_harnessActionPerformed(evt);
            }
        });
        menu01_report.add(menu012_deleted_harness);

        menu017_ucs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        menu017_ucs.setText("Unités de packaging");
        menu017_ucs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu017_ucsActionPerformed(evt);
            }
        });
        menu01_report.add(menu017_ucs);

        menu_bar.add(menu01_report);

        menu02_process.setText("Process");
        menu02_process.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu02_processActionPerformed(evt);
            }
        });

        menu020_waiting.setBackground(java.awt.Color.cyan);
        menu020_waiting.setText("Continue \"Waiting\"");
        menu020_waiting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu020_waitingActionPerformed(evt);
            }
        });
        menu02_process.add(menu020_waiting);

        menu_bar.add(menu02_process);

        menu03_config.setText("Configuration");

        menu030.setText("Serial COM");
        menu030.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu030ActionPerformed(evt);
            }
        });
        menu03_config.add(menu030);

        menu_bar.add(menu03_config);

        setJMenuBar(menu_bar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_top, javax.swing.GroupLayout.DEFAULT_SIZE, 1275, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_top, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //########################################################################
    //########################## USER LABEL METHODS ##########################
    //########################################################################
    public void setUserLabelText(String newText) {
        connectedUserName_label.setText(newText);
    }

    //########################################################################
    //####################### PROJECT BOX FILTER METHODS #####################
    //########################################################################
    public void HarnessTypeFilterSetSelectedItem(String value) {
        harnessTypeFilterBox.setSelectedItem(value);
    }

    //########################################################################
    //########################## PROJECT BOX METHODS #########################
    //########################################################################
    public void setHarnessTypeBoxState(boolean state) {
        harnessTypeBox.setEnabled(state);
    }

    public JComboBox getHarnessTypeBox() {
        return harnessTypeBox;
    }

    public void setHarnessTypeBox(JComboBox projectBox) {
        this.harnessTypeBox = projectBox;
    }

    public void HarnessTypeBoxSelectIndex(int index) {
        this.harnessTypeBox.setSelectedIndex(index);
    }

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid
        Helper.context.setBaseContainerTmp(new BaseContainerTmp());
        Helper.context.setBaseEngineLabelTmp(new BaseEngineLabelTmp());
        Helper.context.setUser(new ManufactureUsers());
    }

    //########################################################################
    //########################## SCAN BOX METHODS ############################
    //########################################################################
    public void clearScanBox() {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
    }

    //########################################################################
    //################ Reset GUI Component to State S01 ######################
    //########################################################################
    public void logout() {

        //Save authentication line in HisLogin table
        HisLogin his_login = new HisLogin(
                Helper.context.getUser().getId(), Helper.context.getUser().getId(),
                String.format(Helper.INFO0012_LOGOUT_SUCCESS,
                        Helper.context.getUser().getFirstName()
                        + " " + Helper.context.getUser().getLastName()
                        + " / " + Helper.context.getUser().getLogin(),
                        Helper.HOSTNAME, Helper.getStrTimeStamp()));
        his_login.setCreateId(Helper.context.getUser().getId());
        his_login.setWriteId(Helper.context.getUser().getId());

        str = String.format(Helper.INFO0012_LOGOUT_SUCCESS,
                Helper.context.getUser().getFirstName() + " " + Helper.context.getUser().getLastName()
                + " / " + Helper.context.getUser().getLogin(), Helper.HOSTNAME,
                Helper.getStrTimeStamp() + " Project : "
                + Helper.context.getUser().getHarnessType());
        his_login.setMessage(str);

        str = "";
        his_login.create(his_login);

        //Reset the state
        state = new S010_UserCodeScan();

        this.clearContextSessionVals();

        //Helper.context.setUser(null);
        //Reset Image
        //Helper.Packaging_Gui.img_lbl.setIcon(state.getImg());
        //Clear Scan Box
        //Helper.Packaging_Gui.requestedPallet_label.setText("");
        //Enable Project Box
        Helper.Packaging_Gui.HarnessTypeBoxSelectIndex(0);
        Helper.Packaging_Gui.setHarnessTypeBoxState(true);
        disableAdminMenus();
        disableOperatorMenus();

        connectedUserName_label.setText("");

    }

    private void menu010_pallet_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu010_pallet_detailsActionPerformed
        if (Helper.context.getUser().getAccessLevel() == Helper.PROFIL_ADMIN) {
            new PACKAGING_UI0010_PalletDetails(this, rootPaneCheckingEnabled, true, true, true, true).setVisible(true);
        } else {
            new PACKAGING_UI0010_PalletDetails(this, rootPaneCheckingEnabled, false, false, false, false).setVisible(true);
        }

    }//GEN-LAST:event_menu010_pallet_detailsActionPerformed

    private void menu030ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu030ActionPerformed
        new GUI(this, true).setVisible(true);
    }//GEN-LAST:event_menu030ActionPerformed

    private void menu011_prod_statisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu011_prod_statisticsActionPerformed
        PACKAGING_UI0011_ProdStatistics ui0011 = new PACKAGING_UI0011_ProdStatistics(this, false);
        ui0011.setVisible(true);
    }//GEN-LAST:event_menu011_prod_statisticsActionPerformed

    public void reloadDataTable() {

        Helper.startSession();

        List<Object> states = new ArrayList<Object>();
        List<Object> projects = new ArrayList<Object>();

        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_STATES_AND_PROJECTS);
        query.setFirstResult(0);
        query.setMaxResults(50);
        
        states.add(Helper.PALLET_OPEN);
        states.add(Helper.PALLET_WAITING);
        
        projects.add(String.valueOf(harnessTypeFilterBox.getSelectedItem()));        

        query.setParameterList("states", states)
                .setParameterList("projects", projects);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        this.reload_container_table_data(result);

    }
    private void menu012_harness_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu012_harness_detailsActionPerformed
        PACKAGING_UI0012_HarnessDetails harnessDetails;
        if (Helper.context.getUser().getAccessLevel() == Helper.PROFIL_ADMIN) {
            harnessDetails = new PACKAGING_UI0012_HarnessDetails(this, rootPaneCheckingEnabled, true);
        } else {
            harnessDetails = new PACKAGING_UI0012_HarnessDetails(this, rootPaneCheckingEnabled, false);
        }
        harnessDetails.setVisible(true);

    }//GEN-LAST:event_menu012_harness_detailsActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        reloadDataTable();
    }//GEN-LAST:event_refreshActionPerformed

    private void scan_txtboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scan_txtboxKeyPressed
        // User has pressed Carriage return button
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            state.doAction(Helper.context);
            state = Helper.context.getState();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to logoff ?", "Logoff confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirmed == 0) {
                logout();
            }

        }
    }//GEN-LAST:event_scan_txtboxKeyPressed

    private void harnessTypeFilterBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_harnessTypeFilterBoxItemStateChanged
        //
    }//GEN-LAST:event_harnessTypeFilterBoxItemStateChanged

    private void harnessTypeFilterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harnessTypeFilterBoxActionPerformed
        for (int i = 0; i < harnessTypeBox.getItemCount(); i++) {            
            if(String.valueOf(harnessTypeFilterBox.getSelectedItem()).equals(String.valueOf(harnessTypeBox.getItemAt(i)))){                
                this.harnessTypeBox.setSelectedIndex(i);                
                break;
            }            
        }

        reloadDataTable();
    }//GEN-LAST:event_harnessTypeFilterBoxActionPerformed

    private void harnessTypeBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_harnessTypeBoxItemStateChanged

    }//GEN-LAST:event_harnessTypeBoxItemStateChanged

    private void harnessTypeBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harnessTypeBoxActionPerformed

        for (int i = 0; i < harnessTypeFilterBox.getItemCount(); i++) {
            if(String.valueOf(harnessTypeBox.getSelectedItem()).equals(String.valueOf(harnessTypeFilterBox.getItemAt(i)))){
                this.harnessTypeFilterBox.setSelectedIndex(i);
                break;
            }            
        }   
    }//GEN-LAST:event_harnessTypeBoxActionPerformed

    private void menu02_processActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu02_processActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_menu02_processActionPerformed

    private void menu020_waitingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu020_waitingActionPerformed
        new PACKAGING_UI0013_PalletWaiting(this, true);
    }//GEN-LAST:event_menu020_waitingActionPerformed

    private void menu013_pallet_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu013_pallet_listActionPerformed
        new WAREHOUSE_FG_UI0002_PALLET_LIST(this, true).setVisible(true);
    }//GEN-LAST:event_menu013_pallet_listActionPerformed

    private void menu012_deleted_palletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu012_deleted_palletActionPerformed
        new PACKAGING_UI0015_DroppedContainer(this, true).setVisible(true);
    }//GEN-LAST:event_menu012_deleted_palletActionPerformed

    private void menu012_deleted_harnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu012_deleted_harnessActionPerformed
        new PACKAGING_UI0016_DroppedHarness(this, true).setVisible(true);
    }//GEN-LAST:event_menu012_deleted_harnessActionPerformed

    private void menu017_ucsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu017_ucsActionPerformed
        new PACKAGING_UI0017_UCS_List(this, true).setVisible(true);
    }//GEN-LAST:event_menu017_ucsActionPerformed

    private void menu018_open_pallet_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu018_open_pallet_listActionPerformed
        new PACKAGING_UI0018_OpenContainer(this, true).setVisible(true);
    }//GEN-LAST:event_menu018_open_pallet_listActionPerformed

    public void setIconLabel(ImageIcon icon) {
        this.img_lbl.setIcon(icon);
    }

    public JTextField getScanTxt() {
        return this.scan_txtbox;
    }

    public void setScanTxt(JTextField setScanTxt) {
        this.scan_txtbox = setScanTxt;
    }

    public JLabel getRequestedPallet_label() {
        return requestedPallet_label;
    }

    public void setRequestedPallet_label(JLabel requestedPallet_label) {
        this.requestedPallet_label = requestedPallet_label;
    }

    public void setRequestedPallet_txt(String text) {
        this.requestedPallet_label.setText(text);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connectedUserName_label;
    private javax.swing.JTable container_table;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JComboBox harnessTypeBox;
    private javax.swing.JComboBox harnessTypeFilterBox;
    private javax.swing.JLabel img_lbl;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem menu010_pallet_details;
    private javax.swing.JMenuItem menu011_prod_statistics;
    private javax.swing.JMenuItem menu012_deleted_harness;
    private javax.swing.JMenuItem menu012_deleted_pallet;
    private javax.swing.JMenuItem menu012_harness_details;
    private javax.swing.JMenuItem menu013_pallet_list;
    private javax.swing.JMenuItem menu017_ucs;
    private javax.swing.JMenuItem menu018_open_pallet_list;
    private javax.swing.JMenu menu01_report;
    private javax.swing.JMenuItem menu020_waiting;
    private javax.swing.JMenu menu02_process;
    private javax.swing.JMenuItem menu030;
    private javax.swing.JMenu menu03_config;
    private javax.swing.JMenuBar menu_bar;
    private javax.swing.JPanel panel_bottom;
    private javax.swing.JPanel panel_top;
    private javax.swing.JButton refresh;
    private javax.swing.JLabel requestedPallet_label;
    private javax.swing.JTextField scan_txtbox;
    // End of variables declaration//GEN-END:variables
}
