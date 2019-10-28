package com.mykid.platform.web.controller.others;

import com.mykid.platform.common.entity.PlatformConstant;
import com.mykid.platform.common.utils.PlatformUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MrBird
 */
@Controller("othersView")
@RequestMapping(PlatformConstant.VIEW_PREFIX + "others")
public class ViewController {

    @GetMapping("platform/form")
    @RequiresPermissions("platform:form:view")
    public String platformForm() {
        return PlatformUtil.view("others/platform/form");
    }

    @GetMapping("platform/form/group")
    @RequiresPermissions("platform:formgroup:view")
    public String platformFormGroup() {
        return PlatformUtil.view("others/platform/formGroup");
    }

    @GetMapping("platform/tools")
    @RequiresPermissions("platform:tools:view")
    public String platformTools() {
        return PlatformUtil.view("others/platform/tools");
    }

    @GetMapping("platform/icon")
    @RequiresPermissions("platform:icons:view")
    public String platformIcon() {
        return PlatformUtil.view("others/platform/icon");
    }

    @GetMapping("platform/others")
    @RequiresPermissions("others:platform:others")
    public String platformOthers() {
        return PlatformUtil.view("others/platform/others");
    }

    @GetMapping("apex/line")
    @RequiresPermissions("apex:line:view")
    public String apexLine() {
        return PlatformUtil.view("others/apex/line");
    }

    @GetMapping("apex/area")
    @RequiresPermissions("apex:area:view")
    public String apexArea() {
        return PlatformUtil.view("others/apex/area");
    }

    @GetMapping("apex/column")
    @RequiresPermissions("apex:column:view")
    public String apexColumn() {
        return PlatformUtil.view("others/apex/column");
    }

    @GetMapping("apex/radar")
    @RequiresPermissions("apex:radar:view")
    public String apexRadar() {
        return PlatformUtil.view("others/apex/radar");
    }

    @GetMapping("apex/bar")
    @RequiresPermissions("apex:bar:view")
    public String apexBar() {
        return PlatformUtil.view("others/apex/bar");
    }

    @GetMapping("apex/mix")
    @RequiresPermissions("apex:mix:view")
    public String apexMix() {
        return PlatformUtil.view("others/apex/mix");
    }

    @GetMapping("map")
    @RequiresPermissions("map:view")
    public String map() {
        return PlatformUtil.view("others/map/gaodeMap");
    }

    @GetMapping("eximport")
    @RequiresPermissions("others:eximport:view")
    public String eximport() {
        return PlatformUtil.view("others/eximport/eximport");
    }

    @GetMapping("eximport/result")
    public String eximportResult() {
        return PlatformUtil.view("others/eximport/eximportResult");
    }
}
