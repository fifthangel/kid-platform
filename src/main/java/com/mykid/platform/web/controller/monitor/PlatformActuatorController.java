package com.mykid.platform.web.controller.monitor;

import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.common.utils.DateUtil;
import com.mykid.platform.endpoint.PlatformHttpTraceEndpoint;
import com.mykid.platform.pojo.entity.PlatformHttpTrace;
import com.mykid.platform.helper.PlatformActuatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mykid.platform.endpoint.PlatformHttpTraceEndpoint.PlatformHttpTraceDescriptor;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("platform/actuator")
public class PlatformActuatorController {

    @Autowired
    private PlatformHttpTraceEndpoint platformHttpTraceEndpoint;
    @Autowired
    private PlatformActuatorHelper actuatorHelper;

    @GetMapping("httptrace")
    @RequiresPermissions("httptrace:view")
    public PlatformResponse httpTraces(String method, String url) throws PlatformException {
        try {
            PlatformHttpTraceDescriptor traces = platformHttpTraceEndpoint.traces();
            List<HttpTrace> httpTraceList = traces.getTraces();
            List<PlatformHttpTrace> platformHttpTraces = new ArrayList<>();
            httpTraceList.forEach(t -> {
                PlatformHttpTrace platformHttpTrace = new PlatformHttpTrace();
                platformHttpTrace.setRequestTime(DateUtil.formatInstant(t.getTimestamp(), DateUtil.FULL_TIME_SPLIT_PATTERN));
                platformHttpTrace.setMethod(t.getRequest().getMethod());
                platformHttpTrace.setUrl(t.getRequest().getUri());
                platformHttpTrace.setStatus(t.getResponse().getStatus());
                platformHttpTrace.setTimeTaken(t.getTimeTaken());
                if (StringUtils.isNotBlank(method) && StringUtils.isNotBlank(url)) {
                    if (StringUtils.equalsIgnoreCase(method, platformHttpTrace.getMethod())
                            && StringUtils.containsIgnoreCase(platformHttpTrace.getUrl().toString(), url))
                        platformHttpTraces.add(platformHttpTrace);
                } else if (StringUtils.isNotBlank(method)) {
                    if (StringUtils.equalsIgnoreCase(method, platformHttpTrace.getMethod()))
                        platformHttpTraces.add(platformHttpTrace);
                } else if (StringUtils.isNotBlank(url)) {
                    if (StringUtils.containsIgnoreCase(platformHttpTrace.getUrl().toString(), url))
                        platformHttpTraces.add(platformHttpTrace);
                } else {
                    platformHttpTraces.add(platformHttpTrace);
                }
            });
            Map<String, Object> data = new HashMap<>();
            data.put("rows", platformHttpTraces);
            data.put("total", platformHttpTraces.size());
            return new PlatformResponse().success().data(data);
        } catch (Exception e) {
            String message = "请求追踪失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
