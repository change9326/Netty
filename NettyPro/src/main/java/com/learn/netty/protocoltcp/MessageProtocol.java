package com.learn.netty.protocoltcp;


/**
 * @author yujiaqi
 * @date 2020-05-28 11:44
 * @description:协议包
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
