package com.ssamz.web.controller.user;

import com.ssamz.biz.user.UserDAO;
import com.ssamz.web.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

// 로그인 인증 처리를 담당하는 컨트롤러
public class LoginController implements Controller {
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("로그인 처리");
        request.setCharacterEncoding("UTF-8");  //사용자로부터 입력받은 정보는 모두 UTF8로 처리
        PrintWriter script = response.getWriter();

        //1. 사용자 입력정보 추출
        String userID = null;
        String userPassword = null;
        if(request.getParameter("userID") != null) {
            userID = request.getParameter("userID");
        }
        if(request.getParameter("userPassword") != null) {
            userPassword = request.getParameter("userPassword");
        }
        if(userID == null || userPassword == null) {
            script.println("<script>alert('입력이 안 된 사항이 있습니다.');</script>");
        }

        UserDAO userDAO = new UserDAO();
        int result = userDAO.login(userID, userPassword);
        HttpSession session = request.getSession();
        if (result == 1) {
            //로그인에 성공했다면 세션에 인증된 ID정보를 등록하고 "index.jsp" 로 포워딩한다.
            session.setAttribute("userID", userID);
            return "index";
        } else if (result == 0) {
            script.println("<script>alert('비밀번호가 틀립니다.');</script>");
        } else if (result == -1) {
            script.println("<script>alert('존재하지 않는 아이디입니다.');</script>");
        } else if (result == -2) {
            script.println("<script>alert('데이터베이스 오류가 발생했습니다.');</script>");
        }
        script.close();
        return "/loginView.do";  //"loginView.do"로 포워딩
    }
}