package com.mykid.platform.web.controller.monitor;

import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.Log;
import com.mykid.platform.service.ILogService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("log")
public class LogController extends BaseController {

    @Autowired
    private ILogService logService;

    @GetMapping("list")
    @RequiresPermissions("log:view")
    public PlatformResponse logList(Log log, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.logService.findLogs(log, request));
        return new PlatformResponse().success().data(dataTable);
    }

    @GetMapping("delete/{ids}")
    @RequiresPermissions("log:delete")
    public PlatformResponse deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws PlatformException {
        try {
            String[] logIds = ids.split(StringPool.COMMA);
            this.logService.deleteLogs(logIds);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "删除日志失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("log:export")
    public void export(QueryRequest request, Log lg, HttpServletResponse response) throws PlatformException {
        try {
            List<Log> logs = this.logService.findLogs(lg, request).getRecords();
            ExcelKit.$Export(Log.class, response).downXlsx(logs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
