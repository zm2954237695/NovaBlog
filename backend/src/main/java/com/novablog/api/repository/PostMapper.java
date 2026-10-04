package com.novablog.api.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novablog.api.model.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
