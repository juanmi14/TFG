/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author Juanmi
 */
class ZoneDiameterBreakpoint {
    //elements
    private S s;
    private R r;

    public ZoneDiameterBreakpoint(){}
    
    public ZoneDiameterBreakpoint(S s, R r) {
        this.s = s;
        this.r = r;
    }

    public S getS() {
        return s;
    }

    public void setS(S s) {
        this.s = s;
    }

    public R getR() {
        return r;
    }

    public void setR(R r) {
        this.r = r;
    }
}
