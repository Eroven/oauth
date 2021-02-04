package me.zhaotb.oauth.server.web;


import me.zhaotb.oauth.server.bean.PageInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@RestController
public class ResourcesController {

    @RequestMapping("resources/some-list")
    public List<Object> list(PageInfo pageInfo) {
        int capacity = pageInfo.getPageSize();
        ArrayList<Object> list = new ArrayList<>(capacity);
        for (int i = pageInfo.getCurrentPage() * capacity; i < capacity * (pageInfo.getCurrentPage()+1); i++) {
            list.add("data-" + i);
        }
        return list;
    }

}
