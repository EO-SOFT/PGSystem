package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import helper.Helper;
import helper.HQLHelper;
import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.Query;

/**
 * BaseContainer generated by hbm2java
 */
@Entity
@Table(name = "drop_base_container")
public class DropBaseContainer extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drop_base_container_id_seq")
    @SequenceGenerator(name = "drop_base_container_id_seq", sequenceName = "drop_base_container_id_seq", allocationSize = 1)
    private Integer id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "drop_time")
    private Date dropTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "write_time")
    private Date writeTime;

    @Column(name = "create_id")
    private int createId;

    @Column(name = "write_id")
    private int writeId;

    @Column(name = "m_user")
    private String user;
    
    @Column(name = "create_user")
    private String createUser;
    
    @Column(name = "drop_user")
    private String dropUser;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "closed_time")
    private Date closedTime;
    
    @Column(name = "work_time")
    private Float workTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "stored_time")
    private Date storedTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "shipped_time")
    private Date shippedTime;

    @Column(name = "pallet_number")
    private String palletNumber;

    @Column(name = "harness_part")
    private String harnessPart;

    @Column(name = "harness_index")
    private String harnessIndex;

    @Column(name = "supplier_part_number")
    private String supplierPartNumber;

    @Column(name = "qty_expected")
    private int qtyExpected;

    @Column(name = "qty_read")
    private int qtyRead;

    @Column(name = "container_state")
    private String containerState;

    @Column(name = "container_state_code")
    private String containerStateCode;

    @Column(name = "pack_type")
    private String packType;

    @Column(name = "harness_type")
    private String harnessType;

    @Column(name = "drop_feedback")
    private String dropFeedback;    
    
    /**
     * Is the global area that englob many carrousels or fixed boards
     */
    @Column(name = "segment")
    private String segment;
    
    /**
     * Is the area where the harness is been produced, it can be a group of fixed board
     * or a carrousel
     */
    @Column(name = "workplace")
    private String workplace;

    @OneToMany(mappedBy = "container")
    private Set<DropBaseHarness> harnessList = new HashSet<DropBaseHarness>(0);

    @Column(name="std_time")
    private double stdTime;    
    
    public DropBaseContainer() {
    }

    public DropBaseContainer setDefautlVals() {
        /*
         Set default values of this object 
         from the global mode2_context values
         */
        this.startTime = this.createTime = this.dropTime = this.writeTime = Helper.getTimeStamp(null);
        this.createId = this.writeId = Helper.mode2_context.getUser().getId();
        this.user = Helper.mode2_context.getUser().getLogin();
        this.dropUser = Helper.mode2_context.getUser().getFirstName() + " " + Helper.mode2_context.getUser().getLastName();
        return this;
    }

    public DropBaseContainer(String palletNumber, String harnessPart, String harnessIndex, String supplierPartNumber, int qtyExpected, int qtyRead, String state, String state_code, String packType, String harnessType, String dropFeedback, double stdTime) {
        this.startTime = this.createTime = this.writeTime = Helper.getTimeStamp(null);
        this.createId = this.writeId = Helper.mode2_context.getUser().getId();
        this.user = Helper.mode2_context.getUser().getLogin();
        this.dropUser =Helper.mode2_context.getUser().getFirstName() + " " + Helper.mode2_context.getUser().getLastName();
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.containerState = state;
        this.containerStateCode = state_code;
        this.qtyExpected = qtyExpected;
        this.qtyRead = qtyRead;
        this.packType = packType;
        this.harnessType = harnessType;
        this.dropFeedback = dropFeedback;
        this.stdTime = stdTime;
    }

    public DropBaseContainer(Date createTime, Date writeTime, Date dropTime, int createId, int writeId, String user, Date startTime, String palletNumber, String harnessPart, String harnessIndex, String supplierPartNumber, int qtyExpected, int qtyRead, String packType, String harnessType, String dropFeedback, double stdTime) {
        this.dropTime = this.writeTime = this.startTime = this.createTime = Helper.getTimeStamp(null);        
        this.createId =this.writeId= Helper.mode2_context.getUser().getId();
        this.dropUser =Helper.mode2_context.getUser().getFirstName() + " " + Helper.mode2_context.getUser().getLastName();
        this.user = Helper.mode2_context.getUser().getFirstName() + " " + Helper.mode2_context.getUser().getLastName() + " / " + Helper.mode2_context.getUser().getLogin();
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.qtyExpected = qtyExpected;
        this.qtyRead = qtyRead;
        this.packType = packType;
        this.harnessType = harnessType;
        this.dropFeedback = dropFeedback;
        this.stdTime = stdTime;
    }

    public DropBaseContainer(Date createTime, Date writeTime, Date dropTime, 
            int createId, int writeId, String user, String dropUser, Date startTime, 
            Date closedTime, Float workTime, String palletNumber, 
            String harnessPart, String harnessIndex, String supplierPartNumber, 
            int qtyExpected, int qtyRead, String state, String state_code,
            String packType, String harnessType, String dropFeedback, double stdTime) {
        this.createTime = createTime;
        this.writeTime = writeTime;
        this.dropTime = dropTime;
        this.createId = createId;
        this.writeId = writeId;
        this.user = user;
        this.dropUser = dropUser;
        this.startTime = startTime;
        this.closedTime = closedTime;
        this.workTime = workTime;
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.qtyExpected = qtyExpected;
        this.qtyRead = qtyRead;
        this.containerState = state;
        this.containerStateCode = state_code;
        this.packType = packType;
        this.harnessType = harnessType;
        this.dropFeedback = dropFeedback;
        this.stdTime = stdTime;
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

    @SuppressWarnings("UnusedAssignment")
    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(this.createTime);
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

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDropUser() {
        return dropUser;
    }

    public void setDropUser(String dropUser) {
        this.dropUser = dropUser;
    }        

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getClosedTime() {
        return this.closedTime;
    }

    public void setClosedTime(Date closedTime) {
        this.closedTime = closedTime;
    }
    
    public Float getWorkTime() {
        return this.workTime;
    }

    public void setWorkTime(Float workTime) {
        this.workTime = workTime;
    }

    public Date getStoredTime() {
        return storedTime;
    }

    public void setStoredTime(Date storedTime) {
        this.storedTime = storedTime;
    }

    public Date getShippedTime() {
        return shippedTime;
    }

    public void setShippedTime(Date shippedTime) {
        this.shippedTime = shippedTime;
    }
    
    public String getPalletNumber() {
        return this.palletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        this.palletNumber = palletNumber;
    }

    public String getHarnessPart() {
        return this.harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getHarnessIndex() {
        return this.harnessIndex;
    }

    public void setHarnessIndex(String harnessIndex) {
        this.harnessIndex = harnessIndex;
    }

    public String getSupplierPartNumber() {
        return this.supplierPartNumber;
    }

    public void setSupplierPartNumber(String supplierPartNumber) {
        this.supplierPartNumber = supplierPartNumber;
    }

    public int getQtyExpected() {
        return this.qtyExpected;
    }

    public void setQtyExpected(int qtyExpected) {
        this.qtyExpected = qtyExpected;
    }

    public int getQtyRead() {
        return this.qtyRead;
    }

    public void setQtyRead(int qtyRead) {
        this.qtyRead = qtyRead;
    }

    public String getContainerState() {
        return containerState;
    }

    public void setContainerState(String containerState) {
        this.containerState = containerState;
    }

    public String getContainerStateCode() {
        return containerStateCode;
    }

    public void setContainerStateCode(String containerStateCode) {
        this.containerStateCode = containerStateCode;
    }

    public String getPackType() {
        return this.packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }

    public Set<DropBaseHarness> getHarnessList() {
        return harnessList;
    }

    public void setHarnessList(Set<DropBaseHarness> harnessList) {
        this.harnessList = harnessList;
    }

    public String getDropFeedback() {
        return dropFeedback;
    }

    public void setDropFeedback(String dropFeedback) {
        this.dropFeedback = dropFeedback;
    }

    public Date getDropTime() {
        return dropTime;
    }

    public void setDropTime(Date dropTime) {
        this.dropTime = dropTime;
    }
    
    public double getStdTime() {
        return stdTime;
    }

    public void setStdTime(double stdTime) {
        this.stdTime = stdTime;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }
        
    public DropBaseContainer getBaseContainer(String palletNumber) {
        //Start transaction
        Helper.startSession();

        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NUMBER);
        query.setParameter("palletNumber", palletNumber);
        Helper.sess.getTransaction().commit();
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (DropBaseContainer) query.list().get(0);
        }
    }

    //######################################################################
    @Override
    public String toString() {
        return "DropBaseContainer{" + "id=" + id + ", createTime=" + createTime + ", writeTime=" + writeTime + ", createId=" + createId + ", writeId=" + writeId + ", user=" + user + ", startTime=" + startTime + ", closedTime=" + closedTime + ", workTime=" + workTime + ", palletNumber=" + palletNumber + ", harnessPart=" + harnessPart + ", harnessIndex=" + harnessIndex + ", supplierPartNumber=" + supplierPartNumber + ", qtyExpected=" + qtyExpected + ", qtyRead=" + qtyRead + ", containerState=" + containerState + ", containerStateCode=" + containerStateCode + ", packType=" + packType + ", harnessType=" + harnessType + ", dropFeedback=" + dropFeedback + ", harnessList=" + harnessList + '}';
    }

    @Override
    public int create(Object obj) {        
        DropBaseContainer dbc = (DropBaseContainer) obj;        
        //Save Container History Line
        String feedback = "Pallet dropped with comment : " + dbc.getDropFeedback();
        HisBaseContainer hbc = new HisBaseContainer().parseDropContainerData(dbc, feedback);
        hbc.create(hbc);               
        return super.create(obj); 
    }

}
