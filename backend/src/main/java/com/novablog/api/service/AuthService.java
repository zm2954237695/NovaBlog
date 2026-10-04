package com.novablog.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novablog.api.common.BusinessException;
import com.novablog.api.dto.LoginRequest;
import com.novablog.api.dto.LoginResponse;
import com.novablog.api.model.User;
import com.novablog.api.repository.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.username()));
        if (user == null || !Boolean.TRUE.equals(user.getEnabled())
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        return new LoginResponse(jwtService.createToken(user.getUsername()), "Bearer",
                jwtService.getExpiresInSeconds(),
                new LoginResponse.UserProfile(user.getId(), user.getUsername(), user.getDisplayName(), user.getRole()));
    }
}
