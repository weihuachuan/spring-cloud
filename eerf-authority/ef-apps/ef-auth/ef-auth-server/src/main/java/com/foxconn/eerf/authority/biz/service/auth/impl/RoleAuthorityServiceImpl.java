package com.foxconn.eerf.authority.biz.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxconn.eerf.authority.biz.dao.auth.RoleAuthorityMapper;
import com.foxconn.eerf.authority.biz.service.auth.ResourceService;
import com.foxconn.eerf.authority.biz.service.auth.RoleAuthorityService;
import com.foxconn.eerf.authority.biz.service.auth.UserRoleService;
import com.foxconn.eerf.authority.dto.auth.RoleAuthoritySaveDTO;
import com.foxconn.eerf.authority.dto.auth.UserRoleSaveDTO;
import com.foxconn.eerf.authority.entity.auth.RoleAuthority;
import com.foxconn.eerf.authority.entity.auth.UserRole;
import com.foxconn.eerf.authority.enumeration.auth.AuthorizeType;
import com.foxconn.eerf.common.constant.CacheKey;
import com.foxconn.eerf.database.mybatis.conditions.Wraps;
import com.foxconn.eerf.utils.NumberHelper;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务实现类
 * 角色的资源
 */
@Slf4j
@Service
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements RoleAuthorityService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CacheChannel cache;

    @Override
    public boolean saveUserRole(UserRoleSaveDTO userRole) {
        userRoleService.remove(Wraps.<UserRole>lbQ().eq(UserRole::getRoleId, userRole.getRoleId()));
        List<UserRole> list = userRole.getUserIdList()
                .stream()
                .map((userId) -> UserRole.builder()
                        .userId(userId)
                        .roleId(userRole.getRoleId())
                        .build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(list);

        //清除 用户拥有的资源列表
        userRole.getUserIdList().forEach((userId) -> {
            String key = CacheKey.buildKey(userId);
            cache.evict(CacheKey.USER_RESOURCE, key);
        });
        return true;
    }

    @Override
    public boolean saveRoleAuthority(RoleAuthoritySaveDTO dto) {
        //删除角色和资源的关联
        super.remove(Wraps.<RoleAuthority>lbQ().eq(RoleAuthority::getRoleId, dto.getRoleId()));

        List<RoleAuthority> list = new ArrayList<>();
        if (dto.getResourceIdList() != null && !dto.getResourceIdList().isEmpty()) {
            List<Long> menuIdList = resourceService.findMenuIdByResourceId(dto.getResourceIdList());
            if (dto.getMenuIdList() == null || dto.getMenuIdList().isEmpty()) {
                dto.setMenuIdList(menuIdList);
            } else {
                dto.getMenuIdList().addAll(menuIdList);
            }

            //保存授予的资源
            List<RoleAuthority> resourceList = new HashSet<>(dto.getResourceIdList())
                    .stream()
                    .map((resourceId) -> RoleAuthority.builder()
                            .authorityType(AuthorizeType.RESOURCE)
                            .authorityId(resourceId)
                            .roleId(dto.getRoleId())
                            .build())
                    .collect(Collectors.toList());
            list.addAll(resourceList);
        }
        if (dto.getMenuIdList() != null && !dto.getMenuIdList().isEmpty()) {
            //保存授予的菜单
            List<RoleAuthority> menuList = new HashSet<>(dto.getMenuIdList())
                    .stream()
                    .map((menuId) -> RoleAuthority.builder()
                            .authorityType(AuthorizeType.MENU)
                            .authorityId(menuId)
                            .roleId(dto.getRoleId())
                            .build())
                    .collect(Collectors.toList());
            list.addAll(menuList);
        }
        super.saveBatch(list);

        // 清理
        List<Long> userIdList = userRoleService.listObjs(Wraps.<UserRole>lbQ().select(UserRole::getUserId).eq(UserRole::getRoleId, dto.getRoleId()),
                (userId) -> NumberHelper.longValueOf0(userId));
        userIdList.stream().collect(Collectors.toSet()).forEach((userId) -> {
            log.info("清理了 {} 的菜单/资源", userId);
            cache.evict(CacheKey.USER_RESOURCE, String.valueOf(userId));
        });

        return true;
    }
}
