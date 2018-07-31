package com.huisu.iyoox.entity.base;

import java.io.Serializable;
import java.util.List;

/**
 * Function:
 * Date: 2018/7/30
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class BaseSendTaskResultModel implements Serializable{
    public int code;
    public String msg;
    public List<Integer> data;
}
