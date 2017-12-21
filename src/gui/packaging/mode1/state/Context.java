/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.state;

import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.ManufactureUsers;

/**
 *
 * @author ezou1001
 */
public class Context {

    private BaseContainerTmp baseContainerTmp = new BaseContainerTmp();
    private BaseHarnessAdditionalBarecodeTmp baseHarnessAdditionalBarecodeTmp = new BaseHarnessAdditionalBarecodeTmp();
    private String feedback = "-";    
    private Integer labelCount = 0;
    //Connected User Object
    private ManufactureUsers user = new ManufactureUsers();
    private State state;

    public Context() {
        state = null;
    }

    public Integer getLabelCount() {
        return labelCount;
    }

    public void setLabelCount(Integer labelCount) {
        this.labelCount = labelCount;
    }

    public ManufactureUsers getUser() {
        return user;
    }

    public void setUser(ManufactureUsers user) {
        this.user = user;
    }

    public BaseContainerTmp getBaseContainerTmp() {
        return baseContainerTmp;
    }

    public void setBaseContainerTmp(BaseContainerTmp baseContainerTmp) {
        this.baseContainerTmp = baseContainerTmp;
    }

    public BaseHarnessAdditionalBarecodeTmp getBaseHarnessAdditionalBarecodeTmp() {
        return baseHarnessAdditionalBarecodeTmp;
    }

    public void setBaseHarnessAdditionalBarecodeTmp(BaseHarnessAdditionalBarecodeTmp baseHarnessAdditionalBarecodeTmp) {
        this.baseHarnessAdditionalBarecodeTmp = baseHarnessAdditionalBarecodeTmp;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void clearContext() {
        this.user = null;
        this.baseContainerTmp = null;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Context{" + "baseContainerTmp=" + baseContainerTmp + '}';
    }
    
    

}
