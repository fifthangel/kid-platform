package com.mykid.platform.web.controller.job;

import com.mykid.platform.common.entity.PlatformConstant;
import com.mykid.platform.common.utils.PlatformUtil;
import com.mykid.platform.pojo.entity.Job;
import com.mykid.platform.service.IJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

/**
 * @author MrBird
 */
@Controller("jobView")
@RequestMapping(PlatformConstant.VIEW_PREFIX + "job")
public class ViewController {

    @Autowired
    private IJobService jobService;

    @GetMapping("job")
    @RequiresPermissions("job:view")
    public String online() {
        return PlatformUtil.view("job/job");
    }

    @GetMapping("job/add")
    @RequiresPermissions("job:add")
    public String jobAdd() {
        return PlatformUtil.view("job/jobAdd");
    }

    @GetMapping("job/update/{jobId}")
    @RequiresPermissions("job:update")
    public String jobUpdate(@NotBlank(message = "{required}") @PathVariable Long jobId, Model model) {
        Job job = jobService.findJob(jobId);
        model.addAttribute("job", job);
        return PlatformUtil.view("job/jobUpdate");
    }

    @GetMapping("log")
    @RequiresPermissions("job:log:view")
    public String log() {
        return PlatformUtil.view("job/jobLog");
    }

}
