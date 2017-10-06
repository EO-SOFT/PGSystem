package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import helper.Helper;
import org.hibernate.Query;
import helper.HQLHelper;
import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * BaseHarness generated by hbm2java
 */
@Entity
@Table(name = "base_engine_label")
public class BaseEngineLabel extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_engine_label_id_seq")
    @SequenceGenerator(name = "base_engine_label_id_seq", sequenceName = "base_engine_label_id_seq", allocationSize = 1)
    private Integer id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "write_time")
    private Date writeTime;

    @Column(name = "create_id")
    private int createId;

    @Column(name = "write_id")
    private int writeId;

    @Column(name = "label_code")
    private String labelCode;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private BaseHarness harness;

    @Column(name = "harness_id", insertable = false, updatable = false)
    private int harnessId;

    public BaseEngineLabel() {
    }

    public BaseEngineLabel setDefautlVals() {
        /*
         Set default values of this object 
         from the global context values
         */
        this.createTime = Helper.getTimeStamp(null);
        this.writeTime = Helper.getTimeStamp(null);
        this.createId = Helper.context.getUser().getId();
        this.writeId = Helper.context.getUser().getId();

        return this;
    }

    public BaseEngineLabel(String labelCode, BaseHarness harness, int harnessId) {
        this.setDefautlVals();
        this.labelCode = labelCode;
        this.harness = harness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getWriteTime() {
        return this.writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public int getCreateId() {
        return this.createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public int getWriteId() {
        return this.writeId;
    }

    public void setWriteId(int writeId) {
        this.writeId = writeId;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public BaseHarness getHarness() {
        return harness;
    }

    public void setHarness(BaseHarness harness) {
        this.harness = harness;
    }

    public int getHarnessId() {
        return harnessId;
    }

    public void setHarnessId(int harnessId) {
        this.harnessId = harnessId;
    }

    //######################################################################    
    public static boolean isLabelCodeExist(String labelCode) {
        //Tester si le harness counter exist dans la base BaseHarness    
        // true if exist, false if not
        
        Helper.log.info(String.format("Searching Engine Label code [%s] in BaseEngineLabel.", labelCode));

        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ENGINE_LABEL_BY_CODE);
        query.setParameter("labelCode", labelCode);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();

        if (query.list().isEmpty()) {
            for (String item : Helper.context.getBaseEngineLabelTmp().getLabelCode()) {
                if (labelCode.equals(item)) {
                    return true;                    
                }
            }
            //Great!!! Is a new Label Code
            Helper.log.info(String.format(Helper.INFO0010_NEW_ENGINE_LABEL, labelCode));
            return false;
        } else {
            Helper.log.warning(String.format(Helper.INFO0011_DUPLICAT_ENGINE_LABEL, labelCode));
            return true;

        }
    }

    @SuppressWarnings("UnusedAssignment")
    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(this.createTime);
    }

    /*
     public static boolean checkLabelFormat(String plasticBagBarcode) {
     //Tester le format du compteur Fx        
     if (!plasticBagBarcode.equals("")
     && plasticBagBarcode.matches(Helper.ENGINE_LABEL_PATTERN_01)) {
     return true;
     } else {
     Helper.log.info(String.format(Helper.ERR0015_ENGINE_LABEL_FORMAT, plasticBagBarcode));
     return false;
     }
     }
     */
    public boolean checkLabelFormat(String plasticBagBarcode) {
        //Tester le format du plastic barcode
        boolean flag = false;
        for (String pattern : Helper.PLASTICBAG_BARCODE_PATTERN_LIST) {
            if (plasticBagBarcode.matches(pattern.trim())) {
                flag = true;
                break;
            }
        }

        if (!plasticBagBarcode.equals("") && flag == true) {
            return true;
        } else {
            Helper.log.info(String.format(Helper.ERR0005_HP_COUNTER_FORMAT, plasticBagBarcode));
            return false;
        }
    }

}
