package com.ssamz.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 모든 컨트롤러 클래스는 반드시 Controller 인터페이스를 구현(implements)해야 한다. : handleRequest
public interface Controller {
    String handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
