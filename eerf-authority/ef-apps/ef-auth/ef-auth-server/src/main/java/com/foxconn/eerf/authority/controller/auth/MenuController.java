package com.foxconn.eerf.authority.controller.auth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.foxconn.eerf.auth.server.utils.JwtTokenServerUtils;
import com.foxconn.eerf.auth.utils.JwtUserInfo;
import com.foxconn.eerf.authority.biz.service.auth.MenuService;
import com.foxconn.eerf.authority.dto.auth.*;
import com.foxconn.eerf.authority.entity.auth.Menu;
import com.foxconn.eerf.base.BaseController;
import com.foxconn.eerf.base.R;
import com.foxconn.eerf.base.entity.SuperEntity;
import com.foxconn.eerf.database.mybatis.conditions.Wraps;
import com.foxconn.eerf.database.mybatis.conditions.query.LbqWrapper;
import com.foxconn.eerf.dozer.DozerUtils;
import com.foxconn.eerf.log.annotation.SysLog;
import com.foxconn.eerf.utils.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 前端控制器
 * 菜单
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
@Api(value = "Menu", tags = "菜单")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private DozerUtils dozer;
    /**
     * 分页查询菜单
     */
    @ApiOperation(value = "分页查询菜单", notes = "分页查询菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "long", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页显示几条", dataType = "long", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/page")
    @SysLog("分页查询菜单")
    public R<IPage<Menu>> page(Menu data) {
        IPage<Menu> page = getPage();
        // 构建值不为null的查询条件
        LbqWrapper<Menu> query = Wraps.lbQ(data).orderByDesc(Menu::getUpdateTime);
        menuService.page(page, query);
        return success(page);
    }

    /**
     * 查询菜单
     */
    @ApiOperation(value = "查询菜单", notes = "查询菜单")
    @GetMapping("/{id}")
    @SysLog("查询菜单")
    public R<Menu> get(@PathVariable Long id) {
        return success(menuService.getById(id));
    }

    /**
     * 新增菜单
     */
    @ApiOperation(value = "新增菜单", notes = "新增菜单不为空的字段")
    @PostMapping
    @SysLog("新增菜单")
    public R<Menu> save(@RequestBody @Validated MenuSaveDTO data) {
        Menu menu = dozer.map(data, Menu.class);
        menuService.save(menu);
        return success(menu);
    }

    /**
     * 修改菜单
     */
    @ApiOperation(value = "修改菜单", notes = "修改菜单不为空的字段")
    @PutMapping
    @SysLog("修改菜单")
    public R<Menu> update(@RequestBody @Validated(SuperEntity.Update.class) MenuUpdateDTO data) {
        Menu menu = dozer.map(data, Menu.class);
        menuService.updateById(menu);
        return success(menu);
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单", notes = "根据id物理删除菜单")
    @DeleteMapping
    @SysLog("删除菜单")
    public R<Boolean> delete(@RequestParam("ids[]") List<Long> ids) {
        menuService.removeByIds(ids);
        return success(true);
    }

    /**
     * 查询用户可用的所有资源
     * @param group  菜单分组
     * @param userId 指定用户id
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group", value = "菜单组", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "查询用户可用的所有菜单", notes = "查询用户可用的所有菜单")
    @GetMapping
    @Deprecated
    public R<List<MenuTreeDTO>> myMenus(@RequestParam(value = "group", required = false) String group,
                                        @RequestParam(value = "userId", required = false) Long userId) {
        if (userId == null || userId <= 0) {
            userId = getUserId();
        }
        List<Menu> list = menuService.findVisibleMenu(group, userId);
        List<MenuTreeDTO> treeList = dozer.mapList(list, MenuTreeDTO.class);

        List<MenuTreeDTO> tree = TreeUtil.build(treeList);
        return success(tree);
    }

    private List<VueRouter> buildSuperAdminRouter() {
        List<VueRouter> tree = new ArrayList<>();
        List<VueRouter> children = new ArrayList<>();

        VueRouter defaults = new VueRouter();
        defaults.setPath("/defaults");
        defaults.setComponent("Layout");
        defaults.setHidden(false);
        defaults.setAlwaysShow(true);
        defaults.setMeta(RouterMeta.builder()
                .title("系统设置").icon("el-icon-coin").breadcrumb(true)
                .build());
        defaults.setId(-1L);
        defaults.setChildren(children);

        tree.add(defaults);
        return tree;
    }

    @Autowired
    private JwtTokenServerUtils jwtTokenServerUtils;

    /**
     * 查询用户可用的所有菜单路由树
     * @param group
     * @param userId
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group", value = "菜单组", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "查询用户可用的所有菜单路由树", notes = "查询用户可用的所有菜单路由树")
    @GetMapping("/router")
    public R<List<VueRouter>> myRouter(@RequestParam(value = "group", required = false) String group,
                                       @RequestParam(value = "userId", required = false) Long userId, HttpServletRequest request) {
        if (userId == null || userId <= 0) {
            userId = getUserId();
        }
        if(userId == 0){
            String userToken = request.getHeader("token");
            JwtUserInfo userInfo = jwtTokenServerUtils.getUserInfo(userToken);
            userId = userInfo.getUserId();
        }
        List<Menu> list = menuService.findVisibleMenu(group, userId);
        List<VueRouter> treeList = dozer.mapList(list, VueRouter.class);
        return success(TreeUtil.build(treeList));
    }

    @ApiOperation(value = "查询超管菜单路由树", notes = "查询超管菜单路由树")
    @GetMapping("/admin/router")
    public R<List<VueRouter>> adminRouter() {
        return success(buildSuperAdminRouter());
    }

    /**
     * 查询系统中所有的的菜单树结构， 不用缓存，因为该接口很少会使用，就算使用，也会管理员维护菜单时使用
     */
    @ApiOperation(value = "查询系统所有的菜单", notes = "查询系统所有的菜单")
    @GetMapping("/tree")
    @SysLog("查询系统所有的菜单")
    public R<List<MenuTreeDTO>> allTree() {
        List<Menu> list = menuService.list(Wraps.<Menu>lbQ().orderByAsc(Menu::getSortValue));
        List<MenuTreeDTO> treeList = dozer.mapList(list, MenuTreeDTO.class);
        return success(TreeUtil.build(treeList));
    }
}
