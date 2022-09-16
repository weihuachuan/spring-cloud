package com.foxconn.eerf.zuul.api;

import com.foxconn.eerf.authority.dto.auth.ResourceQueryDTO;
import com.foxconn.eerf.authority.entity.auth.Resource;
import com.foxconn.eerf.base.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资源API熔断
 */
@Component
public class ResourceApiFallback implements ResourceApi {
    public R<List> list() {
        return null;
    }

    public R<List<Resource>> visible(ResourceQueryDTO resource) {
        return null;
    }
}
