package com.hyphenate.chatuidemo;

import org.litepal.crud.DataSupport;

/**
 * Created by gejin-PC on 2018/4/5.
 */

public class Account extends DataSupport {
    private String acname;
    private int acId;

    public void setAcId(int acId) {
        this.acId = acId;
    }

    public int getAcId() {
        return acId;
    }

    public void setAcname(String acname) {
        this.acname = acname;
    }

    public String getAcname() {
        return acname;
    }
}
