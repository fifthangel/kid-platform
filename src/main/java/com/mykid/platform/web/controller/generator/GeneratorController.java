package com.mykid.platform.web.controller.generator;

import com.mykid.platform.common.controller.BaseController;
import com.mykid.platform.common.entity.PlatformResponse;
import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.common.exception.PlatformException;
import com.mykid.platform.common.exception.FileDownloadException;
import com.mykid.platform.common.utils.PlatformUtil;
import com.mykid.platform.common.utils.FileUtil;
import com.mykid.platform.pojo.entity.Column;
import com.mykid.platform.pojo.entity.GeneratorConfig;
import com.mykid.platform.pojo.entity.GeneratorConstant;
import com.mykid.platform.helper.GeneratorHelper;
import com.mykid.platform.service.IGeneratorConfigService;
import com.mykid.platform.service.IGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("generator")
public class GeneratorController extends BaseController {

    private static final String SUFFIX = "_code.zip";

    @Autowired
    private IGeneratorService generatorService;
    @Autowired
    private IGeneratorConfigService generatorConfigService;
    @Autowired
    private GeneratorHelper generatorHelper;

    @GetMapping("tables/info")
    @RequiresPermissions("generator:view")
    public PlatformResponse tablesInfo(String tableName, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(generatorService.getTables(tableName, request, GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME));
        return new PlatformResponse().success().data(dataTable);
    }

    @GetMapping
    @RequiresPermissions("generator:generate")
    public void generate(@NotBlank(message = "{required}") String name, String remark, HttpServletResponse response) throws FileDownloadException {
        try {
            GeneratorConfig generatorConfig = generatorConfigService.findGeneratorConfig();
            if (generatorConfig == null) {
                throw new PlatformException("代码生成配置为空");
            }

            String className = name;
            if (GeneratorConfig.TRIM_YES.equals(generatorConfig.getIsTrim())) {
                className = name.replaceFirst(generatorConfig.getTrimValue(), "");
            }

            generatorConfig.setTableName(name);
            generatorConfig.setClassName(PlatformUtil.underscoreToCamel(className));
            generatorConfig.setTableComment(remark);
            // 生成代码到临时目录
            List<Column> columns = generatorService.getColumns(GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME, name);
            generatorHelper.generateEntityFile(columns, generatorConfig);
            generatorHelper.generateMapperFile(columns, generatorConfig);
            generatorHelper.generateMapperXmlFile(columns, generatorConfig);
            generatorHelper.generateServiceFile(columns, generatorConfig);
            generatorHelper.generateServiceImplFile(columns, generatorConfig);
            generatorHelper.generateControllerFile(columns, generatorConfig);
            // 打包
            String zipFile = System.currentTimeMillis() + SUFFIX;
            FileUtil.compress(GeneratorConstant.TEMP_PATH + "src", zipFile);
            // 下载
            FileUtil.download(zipFile, name + SUFFIX, true, response);
            // 删除临时目录
            FileUtil.delete(GeneratorConstant.TEMP_PATH);
        } catch (Exception e) {
            String message = "代码生成失败，" + e.getMessage();
            log.error(message, e);
            throw new FileDownloadException(message);
        }
    }
}
