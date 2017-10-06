/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.state;

import entity.ManufactureUsers;

/**
 *
 * @author ezou1001
 */
public class WarehouseOutContext {

    private ManufactureUsers user = new ManufactureUsers();
    private State state;

    
    

    public WarehouseOutContext() {
        state = null;

    }

    public ManufactureUsers getUser() {
        return user;
    }

    public void setUser(ManufactureUsers user) {
        this.user = user;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void clearAllVars() {
        WarehouseHelper.temp_load_plan = null;
    }

}
