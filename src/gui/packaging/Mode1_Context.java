/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging;

import gui.packaging.mode1.state.Mode1_State;

/**
 *
 * @author ezou1001
 */
public class Mode1_Context extends Context {

    private Mode1_State state;

    public Mode1_Context() {
        state = null;
    }

    public void setState(Mode1_State state) {
        this.state = state;
    }

    public Mode1_State getState() {
        return state;
    }

}
