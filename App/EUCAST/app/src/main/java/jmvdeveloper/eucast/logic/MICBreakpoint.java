package jmvdeveloper.eucast.logic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Juanmi
 */
public class MICBreakpoint {
    //elements
    @XStreamAlias("S")
    private S s;
    @XStreamAlias("R")
    private R r;

    public MICBreakpoint(){}
    
    public MICBreakpoint(S s, R r) {
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
