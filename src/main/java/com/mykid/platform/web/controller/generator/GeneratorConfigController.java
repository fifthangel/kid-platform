package com.mykid.platform.web.controller.generator;

import com.mykid.platform.common.annotation.Log;
import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.pojo.entity.GeneratorConfig;
import com.mykid.platform.service.IGeneratorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("generatorConfig")
public class GeneratorConfigController extends BaseController {

    @Autowired
    private IGeneratorConfigService generatorConfigService;

    @GetMapping
    @RequiresPermissions("generator:configure:view")
    public PlatformResponse getGeneratorConfig() {
        return new PlatformResponse().success().data(generatorConfigService.findGeneratorConfig());
    }

    @Log("修改GeneratorConfig")
    @PostMapping("update")
    @RequiresPermissions("generator:configure:update")
    public PlatformResponse updateGeneratorConfig(@Valid GeneratorConfig generatorConfig) throws PlatformException {
        try {
            if (StringUtils.isBlank(generatorConfig.getId()))
                throw new PlatformException("配置id不能为空");
            this.generatorConfigService.updateGeneratorConfig(generatorConfig);
            return new PlatformResponse().success();
        } catch (Exception e) {
            String message = "修改GeneratorConfig失败";
            log.error(message, e);
            throw new PlatformException(message);
        }
    }
}
