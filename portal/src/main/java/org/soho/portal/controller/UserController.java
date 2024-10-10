package org.soho.portal.controller;

import jakarta.annotation.Resource;
import org.soho.portal.common.BaseResponse;
import org.soho.portal.model.dto.user.UserRegisterDTO;
import org.soho.portal.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("register")
    public BaseResponse<Long> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {

        return BaseResponse.success(userService.registerUser(userRegisterDTO.getUsername(),userRegisterDTO.getUserPassword(),userRegisterDTO.getCheckPassword()));
    }

}
