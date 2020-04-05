package com.kazma.user.controller;

import com.kazma.user.consant.CookieCons;
import com.kazma.user.consant.RedisCons;
import com.kazma.user.entity.ResultVO;
import com.kazma.user.entity.UserInfo;
import com.kazma.user.enums.ResultEnum;
import com.kazma.user.enums.RoleEnum;
import com.kazma.user.service.IUserService;
import com.kazma.user.utils.CookieUtils;
import com.kazma.user.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 买家登陆
    @GetMapping("/buyer")
    public ResultVO buyerLogin(@RequestParam("openid") String openid, HttpServletResponse response) {
        UserInfo userInfo = userService.findUserByOpenid(openid);
        if (userInfo == null || userInfo.getId() == null) {
            return ResultUtils.error(ResultEnum.LOGIN_FAIL);
        }

        if (!userInfo.getRole().equals(RoleEnum.BUYER.getCode())) {
            return ResultUtils.error(ResultEnum.ERROR_ROLE);
        }

        CookieUtils.set(response, CookieCons.OPENID, userInfo.getOpenid(), CookieCons.EXPIRE);
        return ResultUtils.success(userInfo);
    }

    // 买家登陆
    @GetMapping("/seller")
    public ResultVO sellerLogin(@RequestParam("openid") String openid,
                                HttpServletResponse response,
                                @CookieValue(value = CookieCons.TOKEN_NAME, required = false) String cookieToken) {
        // 判断是否已经登陆
        if (StringUtils.isNotBlank(cookieToken)) {
            String rOpenId = redisTemplate.opsForValue().get(String.format(RedisCons.TOKEN_TEMPLATE, cookieToken));
            if (StringUtils.isNotBlank(rOpenId)) {
                return ResultUtils.success(null);
            }
        }
        
        UserInfo userInfo = userService.findUserByOpenid(openid);
        if (userInfo == null || userInfo.getId() == null) {
            return ResultUtils.error(ResultEnum.LOGIN_FAIL);
        }

        if (!userInfo.getRole().equals(RoleEnum.SELLER.getCode())) {
            return ResultUtils.error(ResultEnum.ERROR_ROLE);
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        Integer expire = CookieCons.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisCons.TOKEN_TEMPLATE, token),
                openid,
                expire,
                TimeUnit.SECONDS
        );

        CookieUtils.set(response, CookieCons.TOKEN_NAME, token, CookieCons.EXPIRE);
        return ResultUtils.success(null);
    }

}
