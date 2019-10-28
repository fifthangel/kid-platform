package com.mykid.platform.web.controller.system;


import com.mykid.platform.common.annotation.Log;
import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.Role;
import com.mykid.platform.service.IRoleService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public PlatformResponse getAllRoles(Role role) {
        return new PlatformResponse().success().data(roleService.findRoles(role));
    }

    @GetMapping("list")
    @RequiresPermissions("role:view")
    public PlatformResponse roleList(Role role, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.roleService.findRoles(role, request));
        return new PlatformResponse().success().data(dataTable);
    }

    @Log("新增角色")
    @PostMapping
    @RequiresPermissions("role:add")
    public PlatformResponse addRole(@Valid Role role) throws PlatformException {
        try {
            this.roleService.createRole(role);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "新增角色失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("删除角色")
    @GetMapping("delete/{roleIds}")
    @RequiresPermissions("role:delete")
    public PlatformResponse deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws PlatformException {
        try {
            this.roleService.deleteRoles(roleIds);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除角色失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("修改角色")
    @PostMapping("update")
    @RequiresPermissions("role:update")
    public PlatformResponse updateRole(Role role) throws PlatformException {
        try {
            this.roleService.updateRole(role);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改角色失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("role:export")
    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) throws PlatformException {
        try {
            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
            ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

}
