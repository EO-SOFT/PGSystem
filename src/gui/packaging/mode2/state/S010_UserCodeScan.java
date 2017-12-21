/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import helper.Helper;
import helper.HQLHelper;
import entity.ManufactureUsers;
import gui.packaging.mode1.gui.PACKAGING_UI0002_PasswordRequest;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.hibernate.Query;

/**
 *
 * @author ezou1001
 */
public class S010_UserCodeScan implements State {

    private ImageIcon imgIcon = new ImageIcon(Helper.PROP.getProperty("IMG_PATH") + "S01_UserCodeScan.jpg");

    public S010_UserCodeScan() {

    }

    @Override
    public String toString() {
        return "State S01 : S01_UserCodeScan";
    }

    @Override
    public ImageIcon getImg() {
        return this.imgIcon;
    }

    @Override
    public void doAction(Context context) {

        JTextField scan_txtbox = Helper.Packaging_Gui_Mode2.getScanTxt();
        //String harnessType = String.valueOf(Helper.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem());        

        if (!scan_txtbox.getText().trim().equals("")) {
            //Start transaction
            Helper.startSession();

            Query query = Helper.sess.createQuery(HQLHelper.CHECK_LOGIN_ONLY);
            query.setParameter("login", scan_txtbox.getText());
            //query.setParameter("active", 1); //active user only
            //query.setParameter("harnessType", harnessType);
            Helper.sess.getTransaction().commit();
            List result = query.list();

            Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
            switch (result.size()) {
                case 0:
                    JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
                    Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
                    context.setState(this);
                    clearScanBox(scan_txtbox);
                    Helper.Packaging_Gui_Mode2.HarnessTypeBoxSelectIndex(0);
                    break;
                case 1:
                    new PACKAGING_UI0002_PasswordRequest(null, true, (ManufactureUsers) result.get(0)).setVisible(true);

                    //Clear login from barcode
                    clearScanBox(scan_txtbox);

                    //Disable Project Box
                    Helper.Packaging_Gui_Mode2.setHarnessTypeBoxState(false);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, result.size()+" Utilisateurs trouvés avec le même login !", "Login Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
            Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
            context.setState(this);
            clearScanBox(scan_txtbox);
        }
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
