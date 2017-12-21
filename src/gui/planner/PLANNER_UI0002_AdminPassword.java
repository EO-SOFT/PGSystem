/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.planner;

import gui.packaging.mode1.gui.PACKAGING_UI0001_Main_Mode1;
import entity.HisLogin;
import entity.ManufactureUsers;
import helper.HQLHelper;
import helper.Helper;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import gui.packaging.mode2.state.Mode2_S010_UserCodeScan;
import gui.packaging.mode2.state.Mode2_S020_HarnessPartScan;

/**
 *
 * @author user
 */
public class PLANNER_UI0002_AdminPassword extends javax.swing.JDialog {

    Frame parent;

    /**
     * Creates new form UI0010_PalletDetails
     *
     * @param parent
     * @param modal
     */
    public PLANNER_UI0002_AdminPassword(Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        this.parent = parent;
        Helper.centerJDialog(this);
        this.setResizable(false);
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
        ok_btn = new javax.swing.JButton();
        admin_password_txtbox = new javax.swing.JPasswordField();
        admin_login_txtbox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administrator Password");
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        ok_btn.setText("OK");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        admin_password_txtbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                admin_password_txtboxKeyPressed(evt);
            }
        });

        admin_login_txtbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                admin_login_txtboxKeyPressed(evt);
            }
        });

        jLabel1.setText("Password");

        jLabel2.setText("Login");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ok_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(admin_login_txtbox, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addComponent(admin_password_txtbox)))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addComponent(admin_login_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(4, 4, 4)
                .addComponent(admin_password_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ok_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void clearPasswordBox() {
        //Vider le champs de text scan
        admin_password_txtbox.setText("");
    }

    private boolean checkLoginAndPass() {
        Helper.sess.beginTransaction();

        Query query = Helper.sess.createQuery(HQLHelper.CHECK_LOGIN_PASS);
        query.setParameter("password", String.valueOf(admin_password_txtbox.getPassword()));
        query.setParameter("login", admin_login_txtbox.getText());

        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (!result.isEmpty()) {
            Helper.startSession();
            ManufactureUsers user = (ManufactureUsers) result.get(0);
            user.setLoginTime(new Date());
            Helper.mode2_context.setUser(user);
            Helper.mode2_context.getUser().update(Helper.mode2_context.getUser());

            try {
                Helper.HOSTNAME = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Mode2_S010_UserCodeScan.class.getName()).log(Level.SEVERE, null, ex);
            }
            String str = String.format(Helper.INFO0001_LOGIN_SUCCESS,
                    user.getFirstName() + " " + user.getLastName()
                    + " / " + user.getLogin(), Helper.HOSTNAME,
                    Helper.getStrTimeStamp() + " Module : Planner");
            Helper.log.log(Level.INFO, str);

            //Save authentication line in HisLogin table
            HisLogin his_login = new HisLogin(
                    user.getId(), user.getId(),
                    String.format(Helper.INFO0001_LOGIN_SUCCESS,
                            user.getFirstName() + " " + user.getLastName() + " / " + user.getLogin(),
                            Helper.HOSTNAME, Helper.getStrTimeStamp()));
            his_login.setCreateId(user.getId());
            his_login.setWriteId(user.getId());
            his_login.setMessage(str);
            his_login.create(his_login);
            return true;
        }
        return false;
    }

    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed
        if (checkLoginAndPass()) {
            new PACKAGING_UI0001_Main_Mode1().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
            admin_password_txtbox.setText("");
        }
    }//GEN-LAST:event_ok_btnActionPerformed

    private void admin_password_txtboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_admin_password_txtboxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkLoginAndPass()) {
                this.parent.setState(JFrame.ICONIFIED);
                PLANNER_UI0001_Main planner = new PLANNER_UI0001_Main(null, null);
                planner.setVisible(true);
                planner.toFront();
                planner.repaint();

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
                admin_password_txtbox.setText("");
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {            
            this.dispose();
        }
    }//GEN-LAST:event_admin_password_txtboxKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (Helper.mode2_context.getUser() == null) {
            Helper.mode2_context.setState(new Mode2_S010_UserCodeScan());
        }

    }//GEN-LAST:event_formWindowClosing

    private void admin_login_txtboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_admin_login_txtboxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            admin_password_txtbox.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {            
            this.dispose();
        }
    }//GEN-LAST:event_admin_login_txtboxKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField admin_login_txtbox;
    private javax.swing.JPasswordField admin_password_txtbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton ok_btn;
    // End of variables declaration//GEN-END:variables
}
