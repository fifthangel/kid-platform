package com.mykid.platform.common.aspect;

import com.mykid.platform.common.properties.PlatformProperties;
import com.mykid.platform.common.utils.HttpContextUtil;
import com.mykid.platform.common.utils.IPUtil;
import com.mykid.platform.pojo.entity.Log;
import com.mykid.platform.service.ILogService;
import com.mykid.platform.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 *
 * @author MrBird
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private PlatformProperties platformProperties;

    @Autowired
    private ILogService logService;

    @Pointcut("@annotation(com.mykid.platform.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (platformProperties.isOpenAopLog()) {
            // 保存日志
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            Log log = new Log();
            if (user != null)
                log.setUsername(user.getUsername());
            log.setIp(ip);
            log.setTime(time);
            logService.saveLog(point, log);
        }
        return result;
    }
}
