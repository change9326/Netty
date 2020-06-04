package com.learn.rpc.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yujiaqi
 * @date 2020-06-03 11:16
 * @description
 */
@Data
public class InvokeMessage implements Serializable {
    /**
     * 接口名称，即服务名称
     */
    private String className;
    /**
     * 要远程调用的方法名
     */
    private String methodName;
    /**
     * 方法参数类型列表
     */
    private Class<?>[] paramTypes;
    /**
     * 方法参数值列表
     */
    private Object[] paramValues;
}
