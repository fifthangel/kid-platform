package com.mykid.platform.web.controller.system;

import com.mykid.platform.common.authentication.ShiroHelper;
import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformConstant;
import com.mykid.platform.common.utils.DateUtil;
import com.mykid.platform.common.utils.PlatformUtil;
import com.mykid.platform.pojo.entity.User;
import com.mykid.platform.service.IUserService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MrBird
 */
@Controller("systemView")
public class ViewController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ShiroHelper shiroHelper;

    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (PlatformUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(PlatformUtil.view("login"));
            return mav;
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return PlatformUtil.view("error/403");
    }



    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentuserAuthorizationInfo();
        User user = super.getCurrentUser();
        user.setPassword("It's a secret");
        model.addAttribute("user", userService.findByName(user.getUsername())); // 获取实时的用户信息
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles",authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return PlatformUtil.view("layout");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "password/update")
    public String passwordUpdate() {
        return PlatformUtil.view("system/user/passwordUpdate");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "user/profile")
    public String userProfile() {
        return PlatformUtil.view("system/user/userProfile");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "user/avatar")
    public String userAvatar() {
        return PlatformUtil.view("system/user/avatar");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "user/profile/update")
    public String profileUpdate() {
        return PlatformUtil.view("system/user/profileUpdate");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/user")
    @RequiresPermissions("user:view")
    public String systemUser() {
        return PlatformUtil.view("system/user/user");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/user/add")
    @RequiresPermissions("user:add")
    public String systemUserAdd() {
        return PlatformUtil.view("system/user/userAdd");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/user/detail/{username}")
    @RequiresPermissions("user:view")
    public String systemUserDetail(@PathVariable String username, Model model) {
        resolveUserModel(username, model, true);
        return PlatformUtil.view("system/user/userDetail");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/user/update/{username}")
    @RequiresPermissions("user:update")
    public String systemUserUpdate(@PathVariable String username, Model model) {
        resolveUserModel(username, model, false);
        return PlatformUtil.view("system/user/userUpdate");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/role")
    @RequiresPermissions("role:view")
    public String systemRole() {
        return PlatformUtil.view("system/role/role");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/menu")
    @RequiresPermissions("menu:view")
    public String systemMenu() {
        return PlatformUtil.view("system/menu/menu");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "system/dept")
    @RequiresPermissions("dept:view")
    public String systemDept() {
        return PlatformUtil.view("system/dept/dept");
    }

    @RequestMapping(PlatformConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return PlatformUtil.view("index");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "404")
    public String error404() {
        return PlatformUtil.view("error/404");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "403")
    public String error403() {
        return PlatformUtil.view("error/403");
    }

    @GetMapping(PlatformConstant.VIEW_PREFIX + "500")
    public String error500() {
        return PlatformUtil.view("error/500");
    }

    private void resolveUserModel(String username, Model model, Boolean transform) {
        User user = userService.findByName(username);
        model.addAttribute("user", user);
        if (transform) {
            String ssex = user.getSex();
            if (User.SEX_MALE.equals(ssex)) user.setSex("男");
            else if (User.SEX_FEMALE.equals(ssex)) user.setSex("女");
            else user.setSex("保密");
        }
        if (user.getLastLoginTime() != null)
            model.addAttribute("lastLoginTime", DateUtil.getDateFormat(user.getLastLoginTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
    }
}
