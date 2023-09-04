package com.ssamz.web.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import com.ssamz.biz.user.UserDTO;
import com.ssamz.biz.user.UserDAO;
import com.ssamz.biz.evaluation.EvaluationDAO;
import com.ssamz.biz.evaluation.EvaluationDTO;
import com.ssamz.biz.likey.LikeyDAO;
import com.ssamz.biz.likey.LikeyDTO;
import util.SHA256;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;
import util.Gmail;

// DispatcherServlet : 브라우저의 모든 요청(*.do)을 받는 프런트 컨트롤러
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
    // handlerMapping, viewResolver 매개변수 선언
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;

    // init()에서 초기화
    public void init() throws ServletException {
        handlerMapping = new HandlerMapping();
        viewResolver = new ViewResolver();
        viewResolver.setPrefix("./WEB-INF/board/");
        viewResolver.setSuffix(".jsp");
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 사용자요청 path를 추출한다.
        String uri = request.getRequestURI();
        String path = uri.substring(uri.lastIndexOf("/")); //lastIndexOf:문자열 뒤에서부터 탐색하여 해당 index를 리턴. 파일 확장자에 흔히 사용됨.

        //2. HandlerMapping을 통해 path에 해당하는 Controller를 검색한다.
        Controller ctrl = handlerMapping.getController(path);

        //3. 검색된 Controller를 실행한다.
        String viewName = ctrl.handleRequest(request, response);

        //4. ViewResolver를 통해 viewName에 해당하는 경로를 완성한다.
        // .do 가 없으면 .jsp , .do 면 그대로 매핑
        String view = null;
        if (!viewName.contains(".do")) {
            if (viewName.equals("index")) {
                view = viewName + ".jsp";
            } else {
                view = viewResolver.getView(viewName);
            }
        } else {
            view = viewName;
        }

        //5. 검색된 화면으로 포워딩한다.
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

}