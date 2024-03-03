package com.isliujiao.testssoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 厚积薄发
 * @create 2023-04-03-10:48
 */
@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取并返回用户信息
     * @param token 令牌
     * @return 用户信息
     */
    @ResponseBody
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("token") String token) {
        String user = redisTemplate.opsForValue().get(token);
        return user;
    }

    /**
     * 登录页
     */
    @GetMapping("/login.html")
    public String loginPage(Model model,
                            @RequestParam("redirect_url") String url,
                            @CookieValue(value = "sso_token", required = false) String token) {
        //如果浏览器携带了cookie，则直接返回当前页面
        if (!StringUtils.isEmpty(token)) {
            return "redirect:" + url + "?token=" + token;
        }
        model.addAttribute("url", url);
        return "login";
    }

    /**
     * 进行登录操作
     * @param username 用户名
     * @param password 密码
     * @param url 跳转到登录页的原地址
     * @param response 向浏览器响应的数据
     * @return 登录成功 ? 原页面 : 登录页
     */
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("url") String url,
                          HttpServletResponse response) {
        //登录成功,跳回之前页面
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            //把登录成功的账号信息存储到redis，为每一个用户生成一个唯一的key
            String uuid = UUID.randomUUID().toString().replace("-", "");
            redisTemplate.opsForValue().set(uuid, username);

            //将token信息存储到cookie中
            Cookie ssoToken = new Cookie("sso_token", uuid);
            response.addCookie(ssoToken);

            return "redirect:" + url + "?token=" + uuid;
        }
        //登录失败，展示登录页面
        return "";
    }
}
