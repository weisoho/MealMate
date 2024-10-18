package org.soho.portal.controller;

import jakarta.annotation.Resource;
import org.soho.portal.common.BaseResponse;
import org.soho.portal.model.dto.user.UserRegisterDTO;
import org.soho.portal.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    //TODO 头像图片（OSS），密码机密（加密算法or权限框架）
    @PostMapping("register")
    public BaseResponse<Long> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {

        return BaseResponse.success(userService.registerUser(userRegisterDTO.getUsername(),userRegisterDTO.getUserPassword(),userRegisterDTO.getCheckPassword()));
    }

}
