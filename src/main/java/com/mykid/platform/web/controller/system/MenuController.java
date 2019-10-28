package com.mykid.platform.web.controller.system;


import com.mykid.platform.common.annotation.Log;
import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.MenuTree;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.Menu;
import com.mykid.platform.pojo.entity.User;
import com.mykid.platform.service.IMenuService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @GetMapping("{username}")
    public PlatformResponse getUserMenus(@NotBlank(message = "{required}") @PathVariable String username) throws PlatformException {
        User currentUser = getCurrentUser();
        if (!StringUtils.equalsIgnoreCase(username, currentUser.getUsername()))
            throw new PlatformException("您无权获取别人的菜单");
        MenuTree<Menu> userMenus = this.menuService.findUserMenus(username);
        return new PlatformResponse().data(userMenus);
    }

    @GetMapping("tree")
    public PlatformResponse getMenuTree(Menu menu) throws PlatformException {
        try {
            MenuTree<Menu> menus = this.menuService.findMenus(menu);
            return new PlatformResponse().success().data(menus.getChilds());
        } catch (Exception e) {
            String message = "获取菜单树失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("新增菜单/按钮")
    @PostMapping
    @RequiresPermissions("menu:add")
    public PlatformResponse addMenu(@Valid Menu menu) throws PlatformException {
        try {
            this.menuService.createMenu(menu);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("删除菜单/按钮")
    @GetMapping("delete/{menuIds}")
    @RequiresPermissions("menu:delete")
    public PlatformResponse deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws PlatformException {
        try {
            this.menuService.deleteMeuns(menuIds);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("修改菜单/按钮")
    @PostMapping("update")
    @RequiresPermissions("menu:update")
    public PlatformResponse updateMenu(@Valid Menu menu) throws PlatformException {
        try {
            this.menuService.updateMenu(menu);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("menu:export")
    public void export(Menu menu, HttpServletResponse response) throws PlatformException {
        try {
            List<Menu> menus = this.menuService.findMenuList(menu);
            ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
