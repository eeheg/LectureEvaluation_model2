package com.ssamz.web.controller.user;

import com.ssamz.biz.user.UserDAO;
import com.ssamz.biz.user.UserDTO;
import com.ssamz.web.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class InsertUserController implements Controller {

    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("회원가입 처리");
        request.setCharacterEncoding("UTF-8");  //사용자로부터 입력받은 정보는 모두 UTF8로 처리
        String userID = null;
        PrintWriter script = response.getWriter();
        HttpSession session = request.getSession();

        //로그인이 되어있다면 index.jsp로 보낸다.
        if (session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }
        if (userID != null) {
            script.println("<script>alert('로그인이 된 상태입니다.');</script>");
            script.close();
            return "index";
        }

        //사용자가 입력한 정보를 가져온다.
        String userPassword = null;
        String userEmail = null;
        if(request.getParameter("userID") != null) {
            userID = request.getParameter("userID");
        }
        if(request.getParameter("userPassword") != null) {
            userPassword = request.getParameter("userPassword");
        }
        if(request.getParameter("userEmail") != null) {
            userEmail = request.getParameter("userEmail");
        }
        if(userID == null || userPassword == null || userEmail == null) {
            script.println("<script>alert('입력이 안 된 사항이 있습니다.');</script>");
            script.close();
            return "insertUserView.do";
        }

        UserDAO userDAO = new UserDAO();
        int result = userDAO.join(new UserDTO(userID, userPassword, userEmail, util.SHA256.getSHA256(userEmail), false));
        if (result == -1) {
            script.println("<script>alert('이미 존재하는 아이디입니다.');</script>");
            script.close();
            return "insertUserView.do";
        } else {
            session.setAttribute("userID", userID);  //가입 성공 시 바로 로그인상태로
            script.close();
            return "sendEmailView.do";  //"sendEmail.do" 로 포워딩
        }
    }
}
