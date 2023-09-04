package com.ssamz.web.controller.user;

import com.ssamz.biz.user.UserDAO;
import com.ssamz.web.controller.Controller;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

public class SendEmailController implements Controller {
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDAO userDAO = new UserDAO();
        String userID = null;
        PrintWriter script = response.getWriter();
        HttpSession session = request.getSession();

        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }
        //로그인하지 않은 상태
        if(userID == null) {
            script.println("<script>alert('로그인을 해주세요.');</script>");
            script.close();
            return "loginView";
        }

        boolean emailChecked = userDAO.getUserEmailChecked(userID);
        if(emailChecked == true) {
            script.println("<script>alert('이미 이메일 인증이 완료된 회원입니다.');</script>");
            script.close();
            return "index";
        }

        //구글 smtp가 기본적으로 제공하는 양식을 그대로 사용
        String host = "http://localhost:8080/";
        String from = "TESTsjh8924@gmail.com";  //보내는 사람
        String to = userDAO.getUserEmail(userID);  //받는 사람
        String subject = "강의평가를 위한 이메일 인증 메일입니다.";
        String content = "다음 링크에 접속하여 이메일 인증을 진행하세요." +
                "<a href='" + host + "emailCheck.do?code=" + new util.SHA256().getSHA256(to) + "'>이메일 인증하기</a>";

        //구글 smtp서버를 이용하기 위해서 정보 설정하기
        //SMTP 서버 이용하기(22.05.30~) : 구글 2단계인증 > 앱비밀번호 생성(Gmail SMTP) > 앱 비밀번호 16자리를 Gmail 비밀번호로 사용
        Properties p = new Properties();
        p.put("mail.smtp.user", from);  //나의 구글 이메일 계정
        p.put("mail.smtp.host", "smtp.googlemail.com");  //구글에서 제공하는 smtp 서버
        p.put("mail.smtp.port", "465");  //465번 포트 사용 (정해져있음 - 구글서비스가 제공)
        p.put("mail.smtp.starttls.enable", "true");  //starttls의 사용 가능 => true로 설정
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.debug", "true");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.socketFactory.fallback", "false");

        //인증메일 발송하기
        try {
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("sendEmail");
//            requestDispatcher.include(request, response);
            Authenticator auth = new util.Gmail();
            Session ses = Session.getInstance(p, auth);  //구글 계정으로 Gmail 인증 수행
            ses.setDebug(true);  //디버깅 설정
            MimeMessage msg = new MimeMessage(ses);  //MimeMesssage 객체로 실제로 메일을 보낼 수 있게 함
            msg.setSubject(subject);  //메일 제목
            Address fromAddr = new InternetAddress(from);
            msg.setFrom(fromAddr);  //보내는 사람 정보 넣기
            Address toAddr = new InternetAddress(to);
            msg.addRecipient(Message.RecipientType.TO, toAddr);  //받는 사람 정보 넣기
            msg.setContent(content, "text/html;charset=UTF8");  //메일 내용 (UTF8 인코딩으로 전송)
            Transport.send(msg);  //메일 전송
        } catch (Exception e) {
            e.printStackTrace();
            script.println("<script>alert('오류가 발생했습니다.');</script>");
            script.close();
            return "sendEmail";
        }
        return "sendEmailView.do"; //화면이동
    }
}
