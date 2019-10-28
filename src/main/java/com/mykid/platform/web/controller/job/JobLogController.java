package com.mykid.platform.web.controller.job;

import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.JobLog;
import com.mykid.platform.service.IJobLogService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@Validated
@RestController
@RequestMapping("jobLog")
public class JobLogController extends BaseController {

    @Autowired
    private IJobLogService jobLogService;

    @GetMapping
    @RequiresPermissions("job:log:view")
    public PlatformResponse jobLogList(QueryRequest request, JobLog log) {
        Map<String, Object> dataTable = getDataTable(this.jobLogService.findJobLogs(request, log));
        return new PlatformResponse().success().data(dataTable);
    }

    @GetMapping("delete/{jobIds}")
    @RequiresPermissions("job:log:delete")
    public PlatformResponse deleteJobLog(@NotBlank(message = "{required}") @PathVariable String jobIds) throws PlatformException {
        try {
            String[] ids = jobIds.split(StringPool.COMMA);
            this.jobLogService.deleteJobLogs(ids);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除调度日志失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("job:log:export")
    public void export(QueryRequest request, JobLog jobLog, HttpServletResponse response) throws PlatformException {
        try {
            List<JobLog> jobLogs = this.jobLogService.findJobLogs(request, jobLog).getRecords();
            ExcelKit.$Export(JobLog.class, response).downXlsx(jobLogs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
