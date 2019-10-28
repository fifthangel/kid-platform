package com.mykid.platform.web.controller.job;

import com.mykid.platform.common.annotation.Log;
import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.Job;
import com.mykid.platform.service.IJobService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
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
@RequestMapping("job")
public class JobController extends BaseController {

    @Autowired
    private IJobService jobService;

    @GetMapping
    @RequiresPermissions("job:view")
    public PlatformResponse jobList(QueryRequest request, Job job) {
        Map<String, Object> dataTable = getDataTable(this.jobService.findJobs(request, job));
        return new PlatformResponse().success().data(dataTable);
    }

    @GetMapping("cron/check")
    public boolean checkCron(String cron) {
        try {
            return CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            return false;
        }
    }

    @Log("新增定时任务")
    @PostMapping
    @RequiresPermissions("job:add")
    public PlatformResponse addJob(@Valid Job job) throws PlatformException {
        try {
            this.jobService.createJob(job);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "新增定时任务失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("删除定时任务")
    @GetMapping("delete/{jobIds}")
    @RequiresPermissions("job:delete")
    public PlatformResponse deleteJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws PlatformException {
        try {
            String[] ids = jobIds.split(StringPool.COMMA);
            this.jobService.deleteJobs(ids);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除定时任务失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("修改定时任务")
    @PostMapping("update")
    public PlatformResponse updateJob(@Valid Job job) throws PlatformException {
        try {
            this.jobService.updateJob(job);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改定时任务失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("执行定时任务")
    @RequiresPermissions("job:run")
    @GetMapping("run/{jobIds}")
    public PlatformResponse runJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws PlatformException {
        try {
            this.jobService.run(jobIds);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "执行定时任务失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("暂停定时任务")
    @GetMapping("pause/{jobIds}")
    @RequiresPermissions("job:pause")
    public PlatformResponse pauseJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws PlatformException {
        try {
            this.jobService.pause(jobIds);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "暂停定时任务失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @Log("恢复定时任务")
    @GetMapping("resume/{jobIds}")
    @RequiresPermissions("job:resume")
    public PlatformResponse resumeJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws PlatformException {
        try {
            this.jobService.resume(jobIds);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "恢复定时任务失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("job:export")
    public void export(QueryRequest request, Job job, HttpServletResponse response) throws PlatformException {
        try {
            List<Job> jobs = this.jobService.findJobs(request, job).getRecords();
            ExcelKit.$Export(Job.class, response).downXlsx(jobs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
