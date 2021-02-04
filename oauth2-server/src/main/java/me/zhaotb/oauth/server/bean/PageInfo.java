package me.zhaotb.oauth.server.bean;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Data
public class PageInfo implements Serializable {

    private static final long serialVersionUID = -5139701923643754877L;
    private int currentPage;
    private int pageSize;


}
