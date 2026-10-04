package com.novablog.api.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novablog.api.model.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
