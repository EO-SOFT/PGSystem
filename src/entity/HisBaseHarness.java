package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import helper.Helper;
import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * BaseHarness generated by hbm2java
 */
@Entity
@Table(name = "his_base_harness")
public class HisBaseHarness extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "his_base_harness_id_seq")
    @SequenceGenerator(name = "his_base_harness_id_seq", sequenceName = "his_base_harness_id_seq", allocationSize = 1)
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

    @Column(name = "harness_part")
    private String harnessPart;

    @Column(name = "counter")
    private String counter;

    @Column(name = "m_user")
    private String user;
    
    @Column(name = "create_user")
    private String createUser;

    @Column(name = "pallet_number")
    private String palletNumber;

    @Column(name = "harness_type")
    private String harnessType;

    @Column(name = "feedback")
    private String feedback;

    public HisBaseHarness() {
    }

    public HisBaseHarness setDefautlVals() {
        /*
         Set default values of this object 
         from the global mode2_context values
         */
        this.createTime = this.writeTime = Helper.getTimeStamp(null);
        this.createId = this.writeId = Helper.context.getUser().getId();
        this.user = Helper.context.getUser().getLogin();

        return this;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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

    public String getHarnessPart() {
        return this.harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getCounter() {
        return this.counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPalletNumber() {
        return this.palletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        this.palletNumber = palletNumber;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
        

    @Override
    public String toString() {
        return "HisBaseHarness{" + ""
                + "id=" + id + ","
                + "\n createTime=" + createTime + ","
                + "\n writeTime=" + writeTime + ","
                + "\n createId=" + createId + ","
                + "\n writeId=" + writeId + ","
                + "\n harnessPart=" + harnessPart + ","
                + "\n counter=" + counter + ","
                + "\n user=" + user + ","
                + "\n CreateUser=" + createUser + ","
                + "\n palletNumber=" + palletNumber + ","
                + "\n harnessType=" + harnessType + ","
                + "\n feedback=" + feedback + '}';
    }

    @SuppressWarnings("UnusedAssignment")
    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(this.createTime);
    }
    
    /**
     * Parse BaseHarness object to HisBaseHarness object
     * 
     * @param bh
     * @param feedback
     * @return HisBaseHarness
     */
    public HisBaseHarness parseHarnessData(BaseHarness bh, String feedback) {
        HisBaseHarness h = new HisBaseHarness();
        try {

            h.setCounter(bh.getCounter());
            h.setCreateId(bh.getCreateId());
            h.setCreateTime(bh.getCreateTime());
            h.setHarnessPart(bh.getHarnessPart());
            h.setHarnessType(bh.getHarnessType());
            h.setPalletNumber(bh.getPalletNumber());
            h.setUser(bh.getUser());
            h.setWriteId(bh.getWriteId());
            h.setWriteTime(bh.getWriteTime());
            if(feedback != null) h.setFeedback(feedback); else h.setFeedback("-");

        } catch (Exception e) {
            System.out.println("parseHarnessData ERROR " + e.getMessage());
            System.out.println(bh.toString());
        }
        return h;
    }
}
