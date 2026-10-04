package com.novablog.api.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novablog.api.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
