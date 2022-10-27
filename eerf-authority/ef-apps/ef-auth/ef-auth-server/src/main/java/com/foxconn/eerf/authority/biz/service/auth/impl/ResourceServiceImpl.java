package com.foxconn.eerf.authority.biz.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxconn.eerf.authority.biz.dao.auth.ResourceMapper;
import com.foxconn.eerf.authority.biz.service.auth.ResourceService;
import com.foxconn.eerf.authority.dto.auth.ResourceQueryDTO;
import com.foxconn.eerf.authority.entity.auth.Resource;
import com.foxconn.eerf.base.id.CodeGenerate;
import com.foxconn.eerf.common.constant.CacheKey;
import com.foxconn.eerf.database.mybatis.conditions.Wraps;
import com.foxconn.eerf.exception.BizException;
import com.foxconn.eerf.utils.StrHelper;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务实现类
 * 资源
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    @Autowired
    private CacheChannel cache;
    @Autowired
    private CodeGenerate codeGenerate;
    /**
     * 查询用户的可用资源权限
     */
    @Override
    public List<Resource> findVisibleResource(ResourceQueryDTO resourceQueryDTO) {
        //查询当前用户可访问的资源
        List<Resource> visibleResource = baseMapper.findVisibleResource(resourceQueryDTO);
        if(visibleResource != null && visibleResource.size() > 0){
            List<String> userResource = visibleResource.
                    stream().
                    map((Resource r) -> {
                        return r.getMethod() + r.getUrl();
                    }).collect(Collectors.toList());
            //将当前用户可访问的资源载入缓存，形式为：GET/user/page
            cache.set(CacheKey.USER_RESOURCE,
                    resourceQueryDTO.getUserId().toString(),
                    userResource);
        }
        return visibleResource;
    }
    @Override
    public void removeByMenuId(List<Long> menuIds) {
        List<Resource> resources = super.list(Wraps.<Resource>lbQ().in(Resource::getMenuId, menuIds));
        if (resources.isEmpty()) {
            return;
        }
        List<Long> idList = resources.stream().mapToLong(Resource::getId).boxed().collect(Collectors.toList());
        super.removeByIds(idList);
    }

    @Override
    public boolean save(Resource resource) {
        resource.setCode(StrHelper.getOrDef(resource.getCode(), codeGenerate.next()));
        if (super.count(Wraps.<Resource>lbQ().eq(Resource::getCode, resource.getCode())) > 0) {
            throw BizException.validFail("编码[%s]重复", resource.getCode());
        }
        super.save(resource);
        return true;
    }

    @Override
    public List<Long> findMenuIdByResourceId(List<Long> resourceIdList) {
        return baseMapper.findMenuIdByResourceId(resourceIdList);
    }
}
