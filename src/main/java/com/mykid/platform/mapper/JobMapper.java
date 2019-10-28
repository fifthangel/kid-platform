package com.mykid.platform.mapper;


import com.mykid.platform.pojo.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author MrBird
 */
public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();
}