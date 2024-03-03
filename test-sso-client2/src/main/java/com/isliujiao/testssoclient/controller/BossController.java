package com.isliujiao.testssoclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class BossController {

    @Value("${sso.server.url}")
    public String serverUrl;

    /**
     * 无需登录，公共页面
     * @return 页面数据
     */
    @ResponseBody
    @GetMapping("/commonPage")
    public String commonPage(){
        return "CommonPage";
    }


    /**
     * 感知是否登录成功
     * @param model 对象存储数据
     * @param session 浏览器数据
     * @param token 令牌(非必须携带)，去认证服务器认证成功跳转回来时携带
     * @return 具体数据
     */
    @GetMapping("/boss")
    public String employees(Model model,
                            HttpSession session,
                            @RequestParam(value = "token",required = false) String token){
        if(!StringUtils.isEmpty(token)){
            //TODO 在认证中心获取token对应真正的用户信息
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/userInfo?token=" + token, String.class);
            String username = entity.getBody();
            session.setAttribute("loginUser",username);
        }

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            //没登录，跳转到登录服务器登录
            //跳转过去以后，使用url上的查询参数标识自己本来的页面：http://localhost:8081/employees
            return "redirect:" + serverUrl + "?redirect_url=http://localhost:8082/boss";
        }else{
            List<String> emps = new ArrayList<>();
            emps.add("张三");
            emps.add("李四");
            model.addAttribute("emps",emps);
            return "list";
        }
    }
}
