package com.foxconn.eerf.authority.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.foxconn.eerf.authority.biz.service.common.OptLogService;
import com.foxconn.eerf.authority.entity.common.OptLog;
import com.foxconn.eerf.base.BaseController;
import com.foxconn.eerf.base.R;
import com.foxconn.eerf.database.mybatis.conditions.Wraps;
import com.foxconn.eerf.database.mybatis.conditions.query.LbqWrapper;
import com.foxconn.eerf.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端控制器
 * 系统操作日志
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/optLog")
@Api(value = "OptLog", tags = "系统操作日志")
public class OptLogController extends BaseController {
    @Autowired
    private OptLogService optLogService;
    /**
     * 分页查询系统操作日志
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "long", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页显示几条", dataType = "long", paramType = "query", defaultValue = "10"),
    })
    @ApiOperation(value = "分页查询系统操作日志", notes = "分页查询系统操作日志")
    @GetMapping("/page")
    public R<IPage<OptLog>> page(OptLog data) {
        IPage<OptLog> page = getPage();
        // 构建值不为null的查询条件
        LbqWrapper<OptLog> query = Wraps.lbQ(data)
                .leFooter(OptLog::getCreateTime, getEndCreateTime())
                .geHeader(OptLog::getCreateTime, getStartCreateTime())
                .orderByDesc(OptLog::getId);
        optLogService.page(page, query);
        return success(page);
    }

    /**
     * 查询系统操作日志
     */
    @ApiOperation(value = "查询系统操作日志", notes = "查询系统操作日志")
    @GetMapping("/{id}")
    public R<OptLog> get(@PathVariable Long id) {
        return success(optLogService.getById(id));
    }

    /**
     * 删除系统操作日志
     */
    @ApiOperation(value = "删除系统操作日志", notes = "根据id物理删除系统操作日志")
    @DeleteMapping
    @SysLog("删除系统操作日志")
    public R<Boolean> delete(@RequestParam("ids[]") List<Long> ids) {
        optLogService.removeByIds(ids);
        return success(true);
    }
}
