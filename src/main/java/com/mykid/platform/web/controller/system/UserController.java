package com.mykid.platform.web.controller.system;

import com.mykid.platform.common.annotation.Log;
import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.common.utils.MD5Util;
import com.mykid.platform.pojo.entity.User;
import com.mykid.platform.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @GetMapping("{username}")
    public User getUser(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findUserDetail(username);
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username, String userId) {
        return this.userService.findByName(username) == null || StringUtils.isNotBlank(userId);
    }

    @GetMapping("list")
    @RequiresPermissions("user:view")
    public PlatformResponse userList(User user, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.userService.findUserDetail(user, request));
        return new PlatformResponse().success().data(dataTable);
    }

    @Log("新增用户")
    @PostMapping
    @RequiresPermissions("user:add")
    public PlatformResponse addUser(@Valid User user) throws PlatformException {
        try {
            this.userService.createUser(user);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "新增用户失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("删除用户")
    @GetMapping("delete/{userIds}")
    @RequiresPermissions("user:delete")
    public PlatformResponse deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws PlatformException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除用户失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("修改用户")
    @PostMapping("update")
    @RequiresPermissions("user:update")
    public PlatformResponse updateUser(@Valid User user) throws PlatformException {
        try {
            if (user.getUserId() == null)
                throw new PlatformException("用户ID为空");
            this.userService.updateUser(user);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改用户失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @PostMapping("password/reset/{usernames}")
    @RequiresPermissions("user:password:reset")
    public PlatformResponse resetPassword(@NotBlank(message = "{required}") @PathVariable String usernames) throws PlatformException {
        try {
            String[] usernameArr = usernames.split(StringPool.COMMA);
            this.userService.resetPassword(usernameArr);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "重置用户密码失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @PostMapping("password/update")
    public PlatformResponse updatePassword(
            @NotBlank(message = "{required}") String oldPassword,
            @NotBlank(message = "{required}") String newPassword) throws PlatformException {
        try {
            User user = getCurrentUser();
            if (!StringUtils.equals(user.getPassword(), MD5Util.encrypt(user.getUsername(), oldPassword))) {
                throw new PlatformException("原密码不正确");
            }
            userService.updatePassword(user.getUsername(), newPassword);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改密码失败，" + e.getMessage();
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("avatar/{image}")
    public PlatformResponse updateAvatar(@NotBlank(message = "{required}") @PathVariable String image) throws PlatformException {
        try {
            User user = getCurrentUser();
            this.userService.updateAvatar(user.getUsername(), image);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改头像失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @PostMapping("theme/update")
    public PlatformResponse updateTheme(String theme, String isTab) throws PlatformException {
        try {
            User user = getCurrentUser();
            this.userService.updateTheme(user.getUsername(), theme, isTab);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改系统配置失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @PostMapping("profile/update")
    public PlatformResponse updateProfile(User user) throws PlatformException {
        try {
            User currentUser = getCurrentUser();
            user.setUserId(currentUser.getUserId());
            this.userService.updateProfile(user);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改个人信息失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("user:export")
    public void export(QueryRequest queryRequest, User user, HttpServletResponse response) throws PlatformException {
        try {
            List<User> users = this.userService.findUserDetail(user, queryRequest).getRecords();
            ExcelKit.$Export(User.class, response).downXlsx(users, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
