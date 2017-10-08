/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import entity.ConfigProject;
import entity.ConfigShift;
import entity.ConfigUcs;
import gui.UI0000_ModuleChoice;
import gui.packaging.PACKAGING_UI0001_Main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import org.hibernate.Session;
import gui.packaging.state.Context;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author user
 */
public class Helper {

    public static String APPNAME = "PGSystem";

    public static String VERSION = "1.17.10.05";

    public static String AUTHOR = "Created By EZZIOURI Oussama";

    private static Helper instance = null;

    @SuppressWarnings("empty-statement")
    protected Helper() {
        // Exists only to defeat instantiation.        
    }

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    //State Design pattern
    /**
     *
     */
    public static Session sess = HibernateUtil.getInstance().openSession();

    /**
     *
     */
    public static Context context = new Context();

    /**
     *
     */
    public static PACKAGING_UI0001_Main Packaging_Gui = null;
    
    /**
     * 
     */
    //public static UI0000_ModuleChoice Module_Choice_Gui = null;
            
    private static MessageDigest digester;

    /**
     *
     */
    public static String HOSTNAME;

    /**
     * Actual hostname
     */
    private static String TMP_MSG = "";

    //-------------------------- Properties       -----------------------------
    /**
     *
     */
    public final static Properties PROP = new Properties();

    //-------------------------- LOG and Messages -----------------------------
    /**
     *
     */
    public static Logger log = null;

    /**
     *
     */
    public static FileHandler logFileHandler;

    /**
     *
     */
    public static String ERR0035_PART_NUMBER_FORMAT = "Invalid part number format [%s]!";

    /**
     *
     */
    public static String ERR0034_NOT_EMPTY_LOAD_PLAN = "Plan de chargement contient des élements.\n Merci d'annuler les élements "
            + "avant de supprimer le plan.";

    /**
     *
     */
    public static String ERR0033_DESTINATIONS_NOT_EMPTY = "Une ou plusieurs élements associés à cette destination %s.\nSupprimer ses élements en premier.";

    /**
     *
     */
    public static String ERR0032_INCORRECT_DATE_FORMAT = "Format de date incorrecte.";

    /**
     *
     */
    public static String ERR0031_NO_FINAL_DESTINATION_SELECTED = "Veuillez selectionner au moins une destination finale.";
    /**
     *
     */
    public static String ERR0030_NO_DELIVERY_DATE_SELECTED = "Aucune date dispatch n'est selectionnée.";

    /**
     *
     */
    public static String ERR0025_PACKTYPE_ALREADY_OPEN_IN_THE_SAME_WORKSTATION = "Palette/Box déjà ouvert(e) du type %s dans ce poste %s, pour la référence %s.\n"
            + "Solution N° 1 : - Veuillez terminer le packaging de la palette %s avant d'ouvrir une nouvelle palette de type %s.\n"
            + "Solution N° 2 : - Emballer cette référence dans un autre poste packaging.";

    /**
     *
     */
    public static String ERR0029_NO_DESTINATIONS_FOR_DISPATCH = "Aucune destination trouvé pour le dispatch. Merci de créer des destinations dans la table 'load_plan_destination'.";
    /**
     *
     */
    public static String ERR0028_EMPTY_LOAD_PLAN = "Aucune pile enregistrée pour cette destination %s.";
    /**
     *
     */
    public static String ERR0027_NO_WORKPLACE_FOUND = "No Workplace found in database. Please check Config_Workplace table.";
    /**
     *
     */
    public static String ERR0026_NO_SEGMENT_FOUND = "No Segment found in database. Please check Config_Segment table.";
    /**
     *
     */
    public static String ERR0025_WORKSTATION_PALLET = "Vous êtes dans le poste %s. Vous devez scanner le faisceau dans le poste %s !";

    /**
     *
     */
    public static String ERR0024_PALLET_ALREADY_OPEN = "Palette déjà ouverte pour la référence %s !";

    /**
     *
     */
    public static String ERR0023_PALLET_NOT_FOUND = "No pallet found for this number %s !";

    /**
     *
     */
    public static String ERR0022_PACK_TYPE_NOT_FOUND_IN_UCS = "No pack type found in UCS for project [%s], harness part [%s], supplier part [%s], index [%s] and pack size [%s].";

    /**
     *
     */
    public static String ERR0021_PACK_SIZE_NOT_FOUND_IN_UCS = "No pack size found in UCS for project [%s], harness part [%s], supplier part [%s], index [%s] and pack type [%s].";

    /**
     *
     */
    public static String ERR0020_INDEX_NOT_FOUND_IN_UCS = "No index found in UCS for project [%s], harness part [%s] and supplier part [%s].";

    /**
     *
     */
    public static String ERR0019_SUPPLIER_NOT_FOUND_IN_UCS_HARNESS_TYPE = "No supplier part number found in UCS for project [%s] and harness part [%s].";

    /**
     *
     */
    public static String ERR0018_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE = "No harness part found in UCS for project [%s].";

    /**
     *
     */
    public static String ERR0017_NO_SHIFT_FOUND = "No Shift found in database. Please check Config_Shift table.";

    /**
     *
     */
    public static String ERR0016_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE = "Harness part [%s] NOT found in UCS [%s].";

    /**
     *
     */
    public static String ERR0015_ENGINE_LABEL_FORMAT = "Invalid Engine Label scanned format [%s]";

    /**
     *
     */
    public static String ERR0014_NO_PROJECT_FOUND = "No Project found in database. Please check Config_Project table.";

    /**
     *
     */
    public static String ERR0013_UNKNOWN_PRINT_MODE = "Unknown PRINT_MODE %s property. Please use DesktopPrinter or JobPrinter values.";

    /**
     *
     */
    public static String ERR0012_APPLICATION_ENCOUNTRED_PROBLEM = "Application encountered some problem !";

    /**
     *
     */
    public static String ERR0012_APPLICATION_ALREADY_RUNNING = "Application already running !";

    /**
     *
     */
    public static String ERR0011_INVALID_CLOSE_PALLET_BARCODE = "Invalid CLOSE PALLET barcode [%s]!";

    /**
     *
     */
    public static String ERR0010_CONTAINER_NOT_FOUND = "Container [%s] not found.";

    /**
     *
     */
    public static String ERR0009_COUNTER_NOT_MATCH_HP = "Counter [%s] doesn't match harness part [%s]";

    /**
     *
     */
    public static String ERR0008_INCORRECT_PALLET_NUMBER = "Incorrect pallet number [%s]";

    /**
     *
     */
    public static String ERR0007_PRINTING_FAILED = "Failed to print [%s] file!";

    /**
     *
     */
    public static String ERR0006_INVALID_OPEN_PALLET_BARCODE = "Invalid OPEN PALLET barcode [%s]!";

    /**
     *
     */
    public static String ERR0005_HP_COUNTER_FORMAT = "Invalid Counter scanned format [%s]!";

    /**
     *
     */
    public static String ERR0004_HP_NOT_FOUND = "Harness part [%s] NOT found.";

    /**
     *
     */
    public static String ERR0003_HP_FORMAT = "Invalid Harness Part scanned [%s] !";

    /**
     *
     */
    public static String ERR0002_LOGIN_PASS_FAILED = "Login or password failed !";

    /**
     *
     */
    public static String ERR0001_LOGIN_FAILED = "Invalid matricule number or account locked !";

    /**
     *
     */
    public static String ERR0000_DB_CONNECT_FAILED = "com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure";

    /**
     *
     */
    public static String INFO0001_LOGIN_SUCCESS = "User %s connected to host %s at %s.";

    /**
     *
     */
    public static String INFO0002_CREATE_SUCCESS = "Element #%d inserted in table %s .";

    /**
     *
     */
    public static String INFO0003_HP_FOUND = "Harness part [%s] found for project [%s].";

    /**
     *
     */
    public static String INFO0004_HP_COUNTER_FOUND = "Harness counter [%s] already scanned in pallet number [%s] !";

    /**
     *
     */
    public static String INFO0005_NEW_HP_COUNTER = "New Harness counter [%s].";

    /**
     *
     */
    public static String INFO0006_NEW_CONTAINER = "New container [%s].";

    /**
     *
     */
    public static String INFO0007_HP_CONTAINER_FOUND = "Container [%s] already scanned !";

    /**
     *
     */
    public static String INFO0008_CORRECT_PALLET_NUMBER = "Scanned Pallet number [%s] OK.";

    /**
     *
     */
    public static String INFO0009_OPEN_CONTAINER_FOUND = "Open Pallet found. Number [%s] OK.";

    /**
     *
     */
    public static String INFO0010_NEW_ENGINE_LABEL = "New label engine [%s]";

    /**
     *
     */
    public static String INFO0011_DUPLICAT_ENGINE_LABEL = "Duplicated engine label [%s]";

    /**
     *
     */
    public static String INFO0012_LOGOUT_SUCCESS = "User %s disconnected from host %s at %s.";
    //-------------------------- Prefix And Suffix

    /**
     *
     */
    public static String HARN_PART_PREF = "P";

    /**
     *
     */
    public static String SUPPLIER_PART_PREF = "P";

    /**
     *
     */
    public static String HARN_COUNTER_PREF = "P";

    /**
     *
     */
    public static String QUANTITY_PREF = "Q";

    /**
     *
     */
    public static String CLOSE_PAL_PREF = "CP";

    public static String BOOK_WAREHOUSE_IN_PACK_FG = "BOOK PACKAGING RECIEVED FINISH GOODS";

    public static String BOOK_WAREHOUSE_OUT_PACK_FG = "BOOK PACKAGING SHIPPED FINISH GOODS";

    /**
     *
     */
    public static String UCS_SPLITER = "|";

    //-------------------------- MASK DE SAISIE COUNTER
    //Example ; P2220512705.02.2016;11:44:00
    //
    public static String[] DATAMATRIX_PATTERN_LIST;

    public static String[] PARTNUMBER_PATTERN_LIST;

    public static String[][] PLASTICBAG_BARCODE_PATTERN_LIST;

    /**
     *
     */
    //public static Integer ENGINE_LABEL_TIMES = 2;
    //-------------------------- NEW PALLET MASK
    /**
     *
     */
    public static String OPEN_PALLET_PATTERN = "NEWP";

    /**
     *
     */
    public static String PALLET_NUMBER_PATTERN = "^\\d{9}";

    /**
     *
     */
    public static String CLOSING_PALLET_PATTERN = "^[C]{1}[P]{1}\\d{9}";

    //-------------------------- HARNESS TYPE
    /**
     *
     */
    //public static final String SMALL = "SMALL";

    /**
     *
     */
    //public static final String ENGINE = "ENGINE";

    /**
     *
     */
    //public static final String MDEP = "MDEP";

    //Pallet print state
    /**
     *
     */
    public static final String PALLET_PRINT_NEW = "NEW";

    /**
     *
     */
    public static final String PALLET_PRINT_INPROCESS = "IN_PROCESS";

    /**
     *
     */
    public static final String PALLET_PRINT_PRINTED = "PRINTED";

    /**
     *
     */
    public static final String PALLET_PRINT_ERROR = "ERROR";

    //Pallet print state
    /**
     *
     */
    public static final String PALLET_PRINT_REPRINT = "REPRINT";

    //Base Container state
    /**
     *
     */
    public static final String PALLET_OPEN = "OPEN";

    /**
     *
     */
    public static final String PALLET_OPEN_CODE = "1000";

    /**
     *
     */
    public static final String PALLET_WAITING = "WAITING";

    /**
     *
     */
    public static final String PALLET_WAITING_CODE = "1500";

    /**
     *
     */
    public static final String PALLET_QUARANTAINE = "QUARANTAINE";

    /**
     *
     */
    public static final String PALLET_QUARANTAINE_CODE = "1400";

    /**
     *
     */
    public static final String PALLET_CLOSED = "CLOSED";

    /**
     *
     */
    public static final String PALLET_CLOSED_CODE = "1900";

    /**
     *
     */
    public static final String PALLET_STORED = "STORED";

    /**
     *
     */
    public static final String PALLET_STORED_CODE = "2000";

    /**
     *
     */
    public static final String PALLET_SHIPPED = "SHIPPED";

    /**
     *
     */
    public static final String PALLET_SHIPPED_CODE = "3000";

    /**
     *
     */
    public static final String PALLET_DROPPED = "DROPPED";

    /**
     *
     */
    public static final String PALLET_DROPPED_CODE = "-1000";

    /**
     *
     */
    public static final String[][] PALLET_STATES = {
        {"ALL", ""},
        {PALLET_OPEN, ""},
        {PALLET_WAITING, ""},
        {PALLET_CLOSED, ""},
        {PALLET_STORED, "selected"},
        {PALLET_SHIPPED, ""},                        
        //{PALLET_QUARANTAINE, ""}
    };
    //PALLET STATE COLLUMN INDEX IN UI0000_MAIN CONTAINER TABLE
    /**
     *
     */
    public static int PALLET_STATE_COL_INDEX = 8;

    /**
     *
     *
     */
    public static String SCHEDULE_STATE_NEW = "NEW";

    /**
     *
     *
     */
    public static String SCHEDULE_STATE_NEW_CODE = "1000";

    //HARNESS PART COUNTER LENGTH
    /**
     *
     */
    public static Integer HARN_PART_LEN = 9;

    /**
     *
     */
    public static List<String> CONFIG_MENUS = Arrays.asList(
            "---",
            "Unités de conditionnement standard (UCS)",
            "Masque code à barre",
            "Utilisateurs",
            //"Planner",
            "Configuration packaging"
    );

    //USER LEVELS
    /**
     *
     */
    public static final Integer PROFIL_OPERATOR = 1000;
    public static final Integer PROFIL_ADMIN = 9000;

    /**
     * true: Active le vérouillage pour une seule palette par part number
     *
     */
    //public static final boolean UNIQUE_PALLET_PER_PART_NUMBER = false;
    /**
     * true: Active le vérouillage pour une seule palette par type de packaging
     * (KLTV, HV, RV)
     *
     */
    public static final boolean UNIQUE_PALLET_PER_PACK_TYPE = false;

    //SUPPLIER PART LENGTH
    //public static Integer SUPP_PART_LEN = 9;
    /**
     *
     */
    public static void startSession() {
        Helper.sess.flush();
        Helper.sess.clear();
        try {
            Helper.sess.beginTransaction();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, Helper.ERR0000_DB_CONNECT_FAILED, "Database error !", ERROR_MESSAGE);
            System.err.println("Initial SessionFactory creation failed." + e.getMessage());
        }
    }

    //----------------------------- MD5 BLOC ---------------------------------
    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param str
     * @return
     */
    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }
    //-------------------------- END MD5 BLOC ---------------------------------

    //-------------------------- Image Path ------------------------------
    /**
     *
     * @param path
     * @return 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static String InitDailyLogFile(String path) {
        // if the main log directory does not exist, create it        
        File log_dir = new File(path);
        if (!log_dir.exists()) {
            log_dir.mkdir();
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);
        File today_log_dir = new File(path + today);

        // if the today log directory does not exist, create it
        if (!today_log_dir.exists()) {
            today_log_dir.mkdir();
        }
        try {
            String log_path = today_log_dir + File.separator + today + ".log";
            File file = new File(log_path);

            if (file.createNewFile()) {
                TMP_MSG +=  "File " + file.getName() + " created !";
            } else {
                TMP_MSG +=  "File " + file.getName() + " already exists.";
            }
            //Intilize the logFileHandler, formating etc...

            logFileHandler = new FileHandler(log_path, true);
            logFileHandler.setFormatter(new SimpleFormatter());
            //Add the file handler to log object.            
            log = Logger.getLogger(log_path);
            log.addHandler(logFileHandler);
            return TMP_MSG;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TMP_MSG;
    }

    /**
     *
     * @param print_dir_path
     * @param palet_dir
     * @param galia_dir
     * @param picking_dir
     * @param dispatch_dir
     * @return 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static String InitDailyDestPrintDir(String print_dir_path, String palet_dir, String galia_dir, String picking_dir, String dispatch_dir) {
        // if the printing output printing
        File print_dir = new File(print_dir_path);
        if (!print_dir.exists()) {
            print_dir.mkdir();
        }

        // if the today printing directory does not exist, create it
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);

        File today_print_dir = new File(print_dir_path + today);
        if (!today_print_dir.exists()) {
            today_print_dir.mkdir();
            TMP_MSG += "Print directory [" + today_print_dir.getPath() + "] created !\n";
        } else {
            TMP_MSG += "Print directory [" + today_print_dir.getPath() + "] already exist!\n";
        }

        //Create palet sub directory        
        String palet_dir_path = today_print_dir.getPath() + palet_dir + File.separator;
        File file1 = new File(palet_dir_path);
        if (!file1.exists()) {
            file1.mkdir();
            TMP_MSG += "Print sub directory [" + file1.getPath() + "] created !\n";
        } else {
            TMP_MSG += "Print sub directory [" + file1.getPath() + "] already exist!\n";
        }

        //Create galia sub directory
        String galia_dir_path = today_print_dir.getPath() + galia_dir + File.separator;
        File file2 = new File(galia_dir_path);
        if (!file2.exists()) {
            file2.mkdir();
            TMP_MSG += "Print sub directory [" + file2.getPath() + "] created !\n";
        } else {
            TMP_MSG += "Print sub directory [" + file2.getPath() + "] already exist!\n";
        }

        //Create galia sub directory
        String picking_dir_path = today_print_dir.getPath() + picking_dir + File.separator;
        File file3 = new File(picking_dir_path);
        if (!file3.exists()) {
            file3.mkdir();
            TMP_MSG += "Print sub directory [" + file3.getPath() + "] created !\n";
        } else {
            TMP_MSG += "Print sub directory [" + file3.getPath() + "] already exist!\n";
        }

        //Create galia sub directory
        String dispatch_dir_path = today_print_dir.getPath() + dispatch_dir + File.separator;
        File file4 = new File(dispatch_dir_path);
        if (!file4.exists()) {
            file4.mkdir();
            TMP_MSG += "Print sub directory [" + file4.getPath() + "] created !\n";
        } else {
            TMP_MSG += "Print sub directory [" + file4.getPath() + "] already exist!\n";
        }

        log.info(TMP_MSG);
        return TMP_MSG;        
    }

    /**
     * Load Helper.PROP attribut values from config.properties
     */
    public static void loadConfigProperties() {

        /// read from file
        InputStream input = null;
        try {
            input = new FileInputStream(".\\config.properties");
            // load a properties file
            PROP.load(input);
            // get the property value and print it out
            System.out.println("Load properties file :\n " + PROP.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Config properties error !", ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Properties file must be in the same folder as the .jar file.", "Config properties error !", WARNING_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //----------------------- END LOG and Messages -----------------------------
    //----------------------------- Date And Time ------------------------------
    /**
     *
     * @return
     */
    public static String getStrTimeStamp() {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format : Patter of datetime example : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getStrTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format
     * @return
     */
    public static Date getTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(reportDate);
        } catch (ParseException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInHours(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInHr = TimeUnit.MILLISECONDS.toHours(duration);
        return diffInHr;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInMinutes(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(duration);
        return diffInMin;
    }
    //-------------------------- End Date And Time -----------------------------

    //------------------------ Center JDialog in screen ------------------------
    /**
     *
     * @param jdialog
     *
     * Center the jdialog in the center of the screen
     */
    public static void centerJDialog(JDialog jdialog) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - jdialog.getWidth()) / 2;
        int y = (screenSize.height - jdialog.getHeight()) / 2;
        jdialog.setLocation(x, y);
    }

    /**
     *
     * @param jframe
     *
     * Center the jframe in the center of the screen
     */
    public static void centerJFrame(JFrame jframe) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - jframe.getWidth()) / 2;
        int y = (screenSize.height - jframe.getHeight()) / 2;
        jframe.setLocation(x, y);
    }

    public static void loadProjectsInJbox(JComboBox jbox) {
        List result = new ConfigProject().select();
        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, Helper.ERR0014_NO_PROJECT_FOUND, "Projects Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0014_NO_PROJECT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigProject cp = (ConfigProject) o;
                jbox.addItem(new ComboItem(cp.getHarnessType(), cp.getHarnessType()));
            }
        }
    }

    public static void loadShiftsInJbox(JComboBox jbox) {
        List result = new ConfigShift().select();
        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, Helper.ERR0017_NO_SHIFT_FOUND, "Shift Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0017_NO_SHIFT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigShift cs = (ConfigShift) o;
                jbox.addItem(new ComboItem(cs.getDescription(), cs.getName()));
            }
        }
    }

    public static void loadHarnessInJbox(JComboBox jbox, String harnessType) {
        List result = new ConfigUcs().getHarnessList(harnessType);

        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0018_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessType), "UCS Configuration error !", ERROR_MESSAGE);
            System.err.println(String.format(Helper.ERR0018_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessType));
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigUcs cu = (ConfigUcs) o;
                jbox.addItem(new ComboItem(
                        cu.getHarnessPart(),
                        String.valueOf(cu.getHarnessPart())));
            }
        }
    }

    public static void loadSupplierPartInJbox(JComboBox jbox, String harnessType, String harnessPart) {
        List result = new ConfigUcs().getSupplierPartList(harnessType, harnessPart);
        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0019_SUPPLIER_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessType, harnessPart), "UCS Configuration error !", ERROR_MESSAGE);
            System.err.println(String.format(Helper.ERR0019_SUPPLIER_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessType, harnessPart));
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigUcs cu = (ConfigUcs) o;
                jbox.addItem(new ComboItem(
                        cu.getSupplierPartNumber(),
                        String.valueOf(cu.getSupplierPartNumber())));
            }
        }
    }

    public static void loadIndexInJbox(JComboBox jbox, String harnessType, String harnessPart, String supplierPartNumber) {
        List result = new ConfigUcs().getIndexList(harnessType, harnessPart, supplierPartNumber);
        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0020_INDEX_NOT_FOUND_IN_UCS, harnessType, harnessPart, supplierPart), "UCS Configuration error !", ERROR_MESSAGE);
            System.err.println(String.format(Helper.ERR0020_INDEX_NOT_FOUND_IN_UCS, harnessType, harnessPart, supplierPartNumber));
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigUcs cu = (ConfigUcs) o;
                jbox.addItem(new ComboItem(
                        cu.getHarnessIndex(),
                        cu.getHarnessIndex()));
            }
        }
    }

    public static void loadPackSizeInJbox(JComboBox jbox,
            String harnessType,
            String harnessPart,
            String supplierPartNumber,
            String harnessIndex,
            String packType) {
        List result = new ConfigUcs().getPackSizeList(harnessType, harnessPart, supplierPartNumber, harnessIndex, packType);
        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0021_PACK_SIZE_NOT_FOUND_IN_UCS, harnessType, harnessPart, supplierPart, index), "UCS Configuration error !", ERROR_MESSAGE);
            System.err.println(String.format(Helper.ERR0021_PACK_SIZE_NOT_FOUND_IN_UCS, harnessType, harnessPart, supplierPartNumber, harnessIndex, packType));
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigUcs cu = (ConfigUcs) o;
                jbox.addItem(new ComboItem(
                        String.valueOf(cu.getPackSize()),
                        String.valueOf(cu.getPackSize())));
            }
        }
    }

    public static void loadPackTypeInJbox(JComboBox jbox,
            String harnessType,
            String harnessPart,
            String supplierPartNumber,
            String harnessIndex) {
        List result = new ConfigUcs().getPackTypeList(harnessType, harnessPart, supplierPartNumber, harnessIndex);
        if (result.isEmpty()) {
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0022_PACK_TYPE_NOT_FOUND_IN_UCS, harnessType, harnessPart, supplierPart, harnessIndex, packType), "UCS Configuration error !", ERROR_MESSAGE);
            System.err.println(String.format(Helper.ERR0022_PACK_TYPE_NOT_FOUND_IN_UCS, harnessType, harnessPart, supplierPartNumber, harnessIndex));
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigUcs cu = (ConfigUcs) o;
                jbox.addItem(new ComboItem(
                        String.valueOf(cu.getPackType()),
                        String.valueOf(cu.getPackType())));
            }
        }
    }

    public static void loadContainerStateInJbox(JComboBox jbox) {
        jbox.addItem(new ComboItem(Helper.PALLET_STORED, Helper.PALLET_STORED));
        jbox.addItem(new ComboItem(Helper.PALLET_OPEN, Helper.PALLET_OPEN));
        jbox.addItem(new ComboItem(Helper.PALLET_WAITING, Helper.PALLET_WAITING));
        jbox.addItem(new ComboItem(Helper.PALLET_SHIPPED, Helper.PALLET_SHIPPED));
        jbox.addItem(new ComboItem(Helper.PALLET_CLOSED, Helper.PALLET_CLOSED));
    }

    public static void loadCustomersInJbox(JComboBox jbox) {
        List<String[]> result = new ConfigProject().selectHarnessType();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0014_NO_PROJECT_FOUND, "Projects Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0014_NO_PROJECT_FOUND);
        } else {
            System.out.println(result.toString());
            for (int i = 0; i<result.size();i++) {                
                jbox.addItem(new ComboItem(result.get(i)[0], result.get(i)[0]));
            }
        }
    }

}
