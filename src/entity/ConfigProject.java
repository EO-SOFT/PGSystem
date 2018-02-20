package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import helper.Helper;
import java.util.List;
import org.hibernate.Query;
import helper.HQLHelper;
import hibernate.DAO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 * ConfigProject generated by hbm2java
 */
@Entity
@Table(name = "config_project")
public class ConfigProject extends DAO implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_project_id_seq")
    @SequenceGenerator(name = "config_project_id_seq", sequenceName = "config_project_id_seq", allocationSize = 1)
    private Integer id;
    
    @Column(name="harness_type")
    private String harnessType;
    
    @Column(name="customer")
    private String customer;
    
    public ConfigProject() {
    }

    public ConfigProject(String harnessType) {
        this.harnessType = harnessType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public String getCustomer() {
        return this.customer;
    }
    

    //######################################################################
    public List select() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_PROJECT);
        //Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }
    
    public List<String[]> selectCustomers() {
        Helper.startSession();
        SQLQuery query = Helper.sess.createSQLQuery("SELECT DISTINCT customer FROM config_project");
        query.addScalar("customer", StandardBasicTypes.STRING);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();        
        return query.list();
    }
    
    public List<String[]> selectHarnessType() {
        Helper.startSession();
        SQLQuery query = Helper.sess.createSQLQuery("SELECT DISTINCT harness_type FROM config_project");
        query.addScalar("harness_type", StandardBasicTypes.STRING);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();        
        return query.list();
    }
    
    /**
     * 
     * @return 
     */
    public List getProjects(String harnessType) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_PROJECT);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }

}
