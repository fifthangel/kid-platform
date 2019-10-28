package com.mykid.platform.web.controller.system;


import com.mykid.platform.common.annotation.Log;
import com.mykid.platform.common.entity.DeptTree;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.Dept;
import com.mykid.platform.service.IDeptService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @GetMapping("select/tree")
    public List<DeptTree<Dept>> getDeptTree() throws PlatformException {
        try {
            return this.deptService.findDepts();
        } catch (Exception e) {
            String message = "获取部门树失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("tree")
    public PlatformResponse getDeptTree(Dept dept) throws PlatformException {
        try {
            List<DeptTree<Dept>> depts = this.deptService.findDepts(dept);
            return new PlatformResponse().success().data(depts);
        } catch (Exception e) {
            String message = "获取部门树失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("新增部门")
    @PostMapping
    @RequiresPermissions("dept:add")
    public PlatformResponse addDept(@Valid Dept dept) throws PlatformException {
        try {
            this.deptService.createDept(dept);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "新增部门失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("删除部门")
    @GetMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")
    public PlatformResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws PlatformException {
        try {
            String[] ids = deptIds.split(StringPool.COMMA);
            this.deptService.deleteDepts(ids);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除部门失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("修改部门")
    @PostMapping("update")
    @RequiresPermissions("dept:update")
    public PlatformResponse updateDept(@Valid Dept dept) throws PlatformException {
        try {
            this.deptService.updateDept(dept);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改部门失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("dept:export")
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) throws PlatformException {
        try {
            List<Dept> depts = this.deptService.findDepts(dept, request);
            ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
