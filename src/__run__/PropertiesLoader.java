package __run__;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import interfaces.LoggerInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

/**
 *
 * @author OUSSAMA EZZIOURI
 */
public class PropertiesLoader implements LoggerInterface {

    /**
     *
     */
    private static MessageDigest digester;

    /**
     *
     */
    public static String HOSTNAME;

    /**
     *
     */
    public final static Properties APP_PROP = new Properties();

    /**
     *
     */
    public static FileHandler logFileHandler;

    /**
     *
     */
    public static Logger log = null;

    //----------------------------- MD5 BLOC ---------------------------------
    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }

        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
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
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    //-------------------------- Log folder Path ------------------------------
    /**
     * Create daily logging file. If the log output directory doesn't exist, it
     * creates one
     *
     * @param path
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void CreateDailyLogFile(String path) {
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
                System.out.println("File " + file.getName() + " is created!");
            } else {
                System.out.println("File " + file.getName() + " already exists.");
            }
            //Intilize the logFileHandler, formating etc...

            logFileHandler = new FileHandler(log_path, true);
            logFileHandler.setFormatter(new SimpleFormatter());
            //Add the file handler to log object.            
            log = Logger.getLogger(log_path);
            log.addHandler(logFileHandler);
        } catch (IOException e) {
        }

    }

    /**
     * Create daily print directory. If it doesn't exist, it creates one a new
     * one
     *
     * @param print_dir_path
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void CreateDailyOutPrintDir(String print_dir_path) {
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
            log.log(Level.INFO, "Print directory [{0}] is created!\n", today_print_dir.getPath());
        } else {
            log.log(Level.INFO, "Print directory [{0}] already exist!\n", today_print_dir.getPath());
        }
    }

    /**
     * Populate DBInstance.APP_PROP attribut values from config.properties
     */
    public static void loadConfigProperties() {

        /// read from file
        InputStream input = null;
        try {
            input = new FileInputStream(".\\src\\config.properties");
            // load a properties file
            APP_PROP.load(input);
            
            //Map loaded value to global variables
            Global.mapProperties();
            
            // get the property value and print it out
            System.out.println("Load properties file :\n " + APP_PROP.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Config properties error !", ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Properties file must be in the same folder as the .jar file.", "Config properties error !", WARNING_MESSAGE);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public void fine(String msg) {
        log.fine(msg);
    }

    @Override
    public void finer(String msg) {
        log.finer(msg);
    }

    @Override
    public void finest(String msg) {
        log.finest(msg);
    }

    @Override
    public void warning(String msg) {
        log.warning(msg);
    }

    @Override
    public void severe(String msg) {
        log.severe(msg);
    }

}
