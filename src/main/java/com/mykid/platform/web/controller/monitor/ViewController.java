package com.mykid.platform.web.controller.monitor;

import com.mykid.platform.common.entity.PlatformConstant;
import com.mykid.platform.common.exception.RedisConnectException;
import com.mykid.platform.common.utils.PlatformUtil;
import com.mykid.platform.pojo.entity.JvmInfo;
import com.mykid.platform.pojo.entity.RedisInfo;
import com.mykid.platform.pojo.entity.ServerInfo;
import com.mykid.platform.pojo.entity.TomcatInfo;
import com.mykid.platform.helper.PlatformActuatorHelper;
import com.mykid.platform.service.IRedisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.mykid.platform.endpoint.PlatformMetricsEndpoint.MetricResponse;

/**
 * @author MrBird
 */
@Controller("monitorView")
@RequestMapping(PlatformConstant.VIEW_PREFIX + "monitor")
public class ViewController {

    @Autowired
    private PlatformActuatorHelper actuatorHelper;
    @Autowired
    private IRedisService redisService;

    @GetMapping("online")
    @RequiresPermissions("online:view")
    public String online() {
        return PlatformUtil.view("monitor/online");
    }

    @GetMapping("log")
    @RequiresPermissions("log:view")
    public String log() {
        return PlatformUtil.view("monitor/log");
    }

    @GetMapping("loginlog")
    @RequiresPermissions("loginlog:view")
    public String loginLog() {
        return PlatformUtil.view("monitor/loginLog");
    }

    @GetMapping("redis/info")
    @RequiresPermissions("redis:view")
    public String getRedisInfo(Model model) throws RedisConnectException {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        model.addAttribute("infoList", infoList);
        return PlatformUtil.view("monitor/redisInfo");
    }

    @GetMapping("redis/terminal")
    @RequiresPermissions("redis:terminal:view")
    public String redisTerminal(Model model) {
        String osName = System.getProperty("os.name");
        model.addAttribute("osName", osName);
        return PlatformUtil.view("monitor/redisTerminal");
    }

    @GetMapping("httptrace")
    @RequiresPermissions("httptrace:view")
    public String httptrace() {
        return PlatformUtil.view("monitor/httpTrace");
    }

    @GetMapping("jvm")
    @RequiresPermissions("jvm:view")
    public String jvmInfo(Model model) {
        List<MetricResponse> jvm = actuatorHelper.getMetricResponseByType("jvm");
        JvmInfo jvmInfo = actuatorHelper.getJvmInfoFromMetricData(jvm);
        model.addAttribute("jvm", jvmInfo);
        return PlatformUtil.view("monitor/jvmInfo");
    }

    @GetMapping("tomcat")
    @RequiresPermissions("tomcat:view")
    public String tomcatInfo(Model model) {
        List<MetricResponse> tomcat = actuatorHelper.getMetricResponseByType("tomcat");
        TomcatInfo tomcatInfo = actuatorHelper.getTomcatInfoFromMetricData(tomcat);
        model.addAttribute("tomcat", tomcatInfo);
        return PlatformUtil.view("monitor/tomcatInfo");
    }

    @GetMapping("server")
    @RequiresPermissions("server:view")
    public String serverInfo(Model model) {
        List<MetricResponse> jdbcInfo = actuatorHelper.getMetricResponseByType("jdbc");
        List<MetricResponse> systemInfo = actuatorHelper.getMetricResponseByType("system");
        List<MetricResponse> processInfo = actuatorHelper.getMetricResponseByType("process");

        ServerInfo serverInfo = actuatorHelper.getServerInfoFromMetricData(jdbcInfo, systemInfo, processInfo);
        model.addAttribute("server", serverInfo);
        return PlatformUtil.view("monitor/serverInfo");
    }

    @GetMapping("swagger")
    public String swagger() {
        return PlatformUtil.view("monitor/swagger");
    }
}
