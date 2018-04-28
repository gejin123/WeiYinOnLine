package com.hyphenate.chatuidemo;

/**
 * Created by gejin-PC on 2018/3/5.
 */

public class Message {
    public String getLeftMsg() {
        return leftMsg;
    }

    public String getRightMsg() {
        return rightMsg;
    }

    private String leftMsg;
    private String rightMsg;

    public Message(String leftMsg, String rightMsg) {
        this.leftMsg = leftMsg;
        this.rightMsg = rightMsg;

    }
}
