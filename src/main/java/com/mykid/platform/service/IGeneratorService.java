package com.mykid.platform.service;

import com.mykid.platform.common.entity.QueryRequest;
import com.mykid.platform.pojo.entity.Column;
import com.mykid.platform.pojo.entity.Table;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author MrBird
 */
public interface IGeneratorService {

    List<String> getDatabases(String databaseType);

    IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    List<Column> getColumns(String databaseType, String schemaName, String tableName);
}
