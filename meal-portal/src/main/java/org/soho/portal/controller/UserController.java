package org.soho.portal.controller;

import jakarta.annotation.Resource;
import org.soho.common.model.dto.user.UserRegisterDTO;
import org.soho.common.model.vo.BaseResponse;
import org.soho.portal.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    //TODO 1、头像图片（minio）
    // 2、手机号注册
    @PostMapping("register")
    public BaseResponse<Boolean> register(@ModelAttribute @Validated UserRegisterDTO userRegisterDTO) {

        return BaseResponse.success(userService.registerUser(userRegisterDTO));
    }

}
