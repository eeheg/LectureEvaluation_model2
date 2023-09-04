package com.ssamz.web.controller;

import com.ssamz.web.controller.user.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    //Controller를 구현한 객체들을 저장하는 Map
    private Map<String, Controller> mappings;

    public HandlerMapping() {
        //Key-Value 형태로 수많은 Controller를 등록한다.
        mappings = new HashMap<>();
        mappings.put("/insertUserView.do", new InsertUserViewController());
        mappings.put("/insertUser.do", new InsertUserController());
        mappings.put("/loginView.do", new LoginViewController());
        mappings.put("/login.do", new LoginController());
        mappings.put("/sendEmail.do", new SendEmailController());
        mappings.put("/sendEmailView.do", new SendEmailViewController());
    }

    public Controller getController(String path) {
        //Map에 등록된 Controller 들 중에서 특정 경로(path)에 해당하는 Controller를 리턴한다.
        return mappings.get(path);
    }
}
