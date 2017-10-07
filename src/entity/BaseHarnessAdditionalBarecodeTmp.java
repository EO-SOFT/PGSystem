package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import helper.HQLHelper;
import helper.Helper;
import org.hibernate.Query;

/**
 * BaseHarness generated by hbm2java
 */
public class BaseHarnessAdditionalBarecodeTmp implements java.io.Serializable {

    //private String labelCode;
    private String[] labelCode = new String[0];
    //= new String[2];
    private BaseHarness harness;

    public BaseHarnessAdditionalBarecodeTmp() {
    }

    public BaseHarnessAdditionalBarecodeTmp(String[] labelCode, BaseHarness harness, int harnessId) {
        this.labelCode = labelCode;
        this.harness = harness;
    }

    public String[] getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String[] labelCode) {
        this.labelCode = labelCode;
    }    
    
    public void setLabelCode(int index, String labelCode) {
        this.labelCode[index] = labelCode;
    }

    public BaseHarness getHarness() {
        return harness;
    }

    public void setHarness(BaseHarness harness) {
        this.harness = harness;
    }

    //######################################################################    
    public static boolean isLabelCodeExist(String labelCode) {
        //Tester si le harness counter exist dans la base BaseHarness        
        Helper.log.info("Searching additional code [" + labelCode + "] in BaseHarnessAddtionalBarecode.");

        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ADDITIONAL_BARCODES_BY_CODE);
        query.setParameter("labelCode", labelCode);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
//        HibernateUtil.shutdown();

        if (query.list().size() == 0) {
            //Great!!! Is a new counter
            Helper.log.info(String.format(Helper.INFO0010_NEW_ENGINE_LABEL, labelCode));
            return false;
        } else {
            Helper.log.warning(String.format(Helper.INFO0011_DUPLICAT_ENGINE_LABEL, labelCode));
            return true;

        }
    }

}