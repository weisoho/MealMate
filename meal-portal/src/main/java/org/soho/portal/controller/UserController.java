package org.soho.portal.controller;

import jakarta.annotation.Resource;
import org.soho.common.model.dto.user.RegisterUserDTO;
import org.soho.common.model.entity.UserEntity;
import org.soho.common.model.vo.BaseResponse;
import org.soho.common.model.vo.LoginWeChatVo;
import org.soho.portal.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册用户
     * @param registerUserDTO   入参
     * @return  注册结果
     */
    @PostMapping("register")
    public BaseResponse<Boolean> register(@ModelAttribute @Validated RegisterUserDTO registerUserDTO) {

        return BaseResponse.success(userService.insertUser(registerUserDTO));
    }

    /**
     * 微信一键登录
     * @param code  微信获取到的登录码
     * @return  存储的token和用户信息
     */
    @GetMapping("login/wechat")
    public Mono<BaseResponse<LoginWeChatVo>> wechatLogin(@RequestParam String code){
        return userService.wechatLogin(code)
                .map(BaseResponse::success); // 包装为统一响应格式;
    }

    /**
     * 微信验证码登录
     * @param phone 手机号码
     * @param code  验证码
     * @return      登录人信息
     */
    @PostMapping("login/sms")
    public BaseResponse<UserEntity> smsLogin(@RequestParam String phone, @RequestParam String code){
        UserEntity userEntity = userService.smsLogin(phone,code);
        return BaseResponse.success(userEntity);
    }

    @PostMapping("send-verify-code")
    public BaseResponse<UserEntity> sendVerityCode(@RequestParam String phone){

        return BaseResponse.success();
    }
    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("WebFlux is working!");
    }
}
