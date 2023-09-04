package com.ssamz.web.controller;

import com.ssamz.biz.evaluation.EvaluationDAO;
import com.ssamz.biz.evaluation.EvaluationDTO;
import com.ssamz.biz.likey.LikeyDAO;
import com.ssamz.biz.user.UserDAO;
import com.ssamz.biz.user.UserDTO;
import util.Gmail;
import util.SHA256;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

// DispatcherServlet : 브라우저의 모든 요청(*.do)을 받는 프런트 컨트롤러
// @WebServlet("*.do")
public class DispatcherServletBackup extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 사용자요청 path를 추출한다.
        String uri = request.getRequestURI();
        String path = uri.substring(uri.lastIndexOf("/")); //lastIndexOf:문자열 뒤에서부터 탐색하여 해당 index를 리턴. 파일 확장자에 흔히 사용됨.

        //2. 추출된 path 정보에 따라 요청을 분기 처리한다.
        if(path.equals("/login.do")) {
            System.out.println("로그인 처리");
            request.setCharacterEncoding("UTF-8");  //사용자로부터 입력받은 정보는 모두 UTF8로 처리
            String userID = null;
            String userPassword = null;
            if(request.getParameter("userID") != null) {
                userID = request.getParameter("userID");
            }
            if(request.getParameter("userPassword") != null) {
                userPassword = request.getParameter("userPassword");
            }
            if(userID == null || userPassword == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('입력이 안 된 사항이 있습니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }

            UserDAO userDAO = new UserDAO();
            int result = userDAO.login(userID, userPassword);
            HttpSession session = request.getSession();
            if (result == 1) {
                session.setAttribute("userID", userID);
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("location.href='index.jsp'");
                script.println("</script>");
                script.close();
                return;
            } else if (result == 0) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('비밀번호가 틀립니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            } else if (result == -1) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('존재하지 않는 아이디입니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            } else if (result == -2) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('데이터베이스 오류가 발생했습니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }
        }

        if(path.equals("/insertUser.do")) {
            System.out.println("회원가입 처리");
            request.setCharacterEncoding("UTF-8");  //사용자로부터 입력받은 정보는 모두 UTF8로 처리
            String userID = null;
            HttpSession session = request.getSession();
            if (session.getAttribute("userID") != null) {
                userID = (String) session.getAttribute("userID");
            }
            if (userID != null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인이 된 상태입니다.');");
                script.println("location.href='index.jsp'");
                script.println("</script>");
                script.close();
                return;
            }

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
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('입력이 안 된 사항이 있습니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }

            UserDAO userDAO = new UserDAO();
            int result = userDAO.join(new UserDTO(userID, userPassword, userEmail, SHA256.getSHA256(userEmail), false));
            if (result == -1) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('이미 존재하는 아이디입니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            } else {
                session.setAttribute("userID", userID);  //가입 성공 시 바로 로그인상태로
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("location.href = 'sendEmail.do'");
                script.println("</script>");
                script.close();
                //return;
            }
        }

        if(path.equals("/logout.do")) {
            System.out.println("로그아웃 처리");
            PrintWriter script = response.getWriter();
            HttpSession session = request.getSession();
            session.invalidate();
            script.println("<script>");
            script.println("location.href = 'index.jsp';");
            script.println("</script>");
            script.close();
            return;
        }

        if(path.equals("insertBoard.do")) {
            System.out.println("글 등록 처리");
            request.setCharacterEncoding("UTF-8");  //사용자로부터 입력받은 정보는 모두 UTF8로 처리
            String userID = null;
            HttpSession session = request.getSession();
            if (session.getAttribute("userID") != null) {
                userID = (String) session.getAttribute("userID");
            }
            if (userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요.');");
                script.println("location.href='loginView.do';");
                script.println("</script>");
                script.close();
                return;
            }

            String lectureName = null;
            String professorName = null;
            int lectureYear = 0;
            String semesterDivide = null;
            String lectureDivide = null;
            String evaluationTitle = null;
            String evaluationContent = null;
            String totalScore = null;
            String creditScore = null;
            String comfortableScore = null;
            String lectureScore = null;

            if(request.getParameter("lectureName") != null) {
                lectureName = request.getParameter("lectureName");
            }
            if(request.getParameter("professorName") != null) {
                professorName = request.getParameter("professorName");
            }
            //사용자가 입력한 값은 기본적으로 문자열이기 때문에 int값으로 바꿔서 받아야 한다.
            if(request.getParameter("lectureYear") != null) {
                try {
                    lectureYear = Integer.parseInt(request.getParameter("lectureYear"));
                } catch (Exception e) {
                    System.out.println("강의 연도 데이터 오류");
                }
            }
            if(request.getParameter("semesterDivide") != null) {
                semesterDivide = request.getParameter("semesterDivide");
            }
            if(request.getParameter("semesterDivide") != null) {
                semesterDivide = request.getParameter("semesterDivide");
            }
            if(request.getParameter("lectureDivide") != null) {
                lectureDivide = request.getParameter("lectureDivide");
            }
            if(request.getParameter("evaluationTitle") != null) {
                evaluationTitle = request.getParameter("evaluationTitle");
            }
            if(request.getParameter("evaluationContent") != null) {
                evaluationContent = request.getParameter("evaluationContent");
            }
            if(request.getParameter("totalScore") != null) {
                totalScore = request.getParameter("totalScore");
            }
            if(request.getParameter("creditScore") != null) {
                creditScore = request.getParameter("creditScore");
            }
            if(request.getParameter("comfortableScore") != null) {
                comfortableScore = request.getParameter("comfortableScore");
            }
            if(request.getParameter("lectureScore") != null) {
                lectureScore = request.getParameter("lectureScore");
            }

            if (lectureName==null || professorName==null || lectureYear==0 || semesterDivide==null ||
                    lectureDivide==null || evaluationTitle==null || evaluationContent==null || totalScore==null ||
                    creditScore==null || comfortableScore==null || lectureScore == null || evaluationTitle.equals("") || evaluationContent.equals("")) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('입력이 안 된 사항이 있습니다.')");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }

            EvaluationDAO evaluationDAO = new EvaluationDAO();
            int result = evaluationDAO.write(
                    new EvaluationDTO(0, userID, lectureName, professorName, lectureYear, semesterDivide, lectureDivide,
                            evaluationTitle, evaluationContent, totalScore, creditScore, comfortableScore, lectureScore, 0));

            if (result == -1) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('강의 평가 등록에 실패했습니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            } else {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("location.href = 'index.jsp'");
                script.println("</script>");
                script.close();
                //return;
            }
        }

        if(path.equals("/updateBoard.do")) {
            System.out.println("글 수정 처리");
        }

        if(path.equals("/deleteBoard.do")) {
            System.out.println("글 삭제 처리");
            String userID = null;
            HttpSession session = request.getSession();
            //session.getAttribute("userID") : 로그인이 되어있는 상태
            if(session.getAttribute("userID") != null) {
                //userID에 해당 session 값을 넣어준다.
                userID = (String) session.getAttribute("userID");
            }
            //로그인하지 않은 상태
            if(userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요.');");
                script.println("location.href = 'loginView.do'");
                script.println("</script>");
                script.close();
                return;
            }

            request.setCharacterEncoding("UTF-8");
            String evaluationID = null;
            if(request.getParameter("evaluationID") != null) {
                evaluationID = request.getParameter("evaluationID");
            }
            EvaluationDAO evaluationDAO = new EvaluationDAO();
            if(userID.equals(evaluationDAO.getUserID(evaluationID))) {
                int result = new EvaluationDAO().delete(evaluationID);
                if(result == 1) {
                    PrintWriter script = response.getWriter();
                    script.println("<script>");
                    script.println("alert('삭제가 완료되었습니다.')");
                    script.println("location.href='index.jsp';");
                    script.println("</script>");
                    script.close();
                    return;
                } else {
                    PrintWriter script = response.getWriter();
                    script.println("<script>");
                    script.println("alert('데이터베이스 오류가 발생했습니다.')");
                    script.println("history.back()");
                    script.println("</script>");
                    script.close();
                    return;
                }
            } else {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('본인이 작성한 글만 삭제할 수 있습니다.')");
                script.println("history.back()");
                script.println("</script>");
                script.close();
                return;
            }
        }

        if(path.equals("/like.do")) {
            String ip = request.getHeader("X-FORWARDED-FOR");
            if(ip == null || ip.length() == 0) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0) {
                ip = request.getRemoteAddr();
            }

            String userID = null;
            HttpSession session = request.getSession();
            //session.getAttribute("userID") : 로그인이 되어있는 상태
            if(session.getAttribute("userID") != null) {
                //userID에 해당 session 값을 넣어준다.
                userID = (String) session.getAttribute("userID");
            }
            //로그인하지 않은 상태
            if(userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요.');");
                script.println("location.href = 'loginView.do'");
                script.println("</script>");
                script.close();
                return;
            }

            request.setCharacterEncoding("UTF-8");
            String evaluationID = null;
            if(request.getParameter("evaluationID") != null) {
                evaluationID = request.getParameter("evaluationID");
            }
            EvaluationDAO evaluationDAO = new EvaluationDAO();
            LikeyDAO likeyDAO = new LikeyDAO();
            int result = likeyDAO.like(userID, evaluationID, ip);
            if(result == 1) {
                result = evaluationDAO.like(evaluationID);
                if(result == 1) {
                    PrintWriter script = response.getWriter();
                    script.println("<script>");
                    script.println("alert('추천이 완료되었습니다.')");
                    script.println("location.href='index.jsp';");
                    script.println("</script>");
                    script.close();
                    return;
                } else {
                    PrintWriter script = response.getWriter();
                    script.println("<script>");
                    script.println("alert('데이터베이스 오류가 발생했습니다.')");
                    script.println("history.back();");
                    script.println("</script>");
                    script.close();
                    return;
                }
            } else {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('이미 추천을 누른 글입니다.')");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }
        }

        if(path.equals("/getBoardList.do")) {
            System.out.println("글 목록 검색 처리");
            request.setCharacterEncoding("UTF-8");
            String lectureDivide = "전체";
            String searchType = "최신순";
            String search = "";
            int pageNumber = 0;
            if(request.getParameter("lectureDivide") != null) {
                lectureDivide = request.getParameter("lectureDivide");
            }
            if(request.getParameter("searchType") != null) {
                searchType = request.getParameter("searchType");
            }
            if(request.getParameter("search") != null) {
                search = request.getParameter("search");
            }
            if(request.getParameter("pageNumber") != null) {
                try {
                    pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
                } catch (Exception e) {
                    System.out.println("검색 페이지 번호 오류");
                }
            }

            String userID = null;
            HttpSession session = request.getSession();
            if(session.getAttribute("userID") != null) {
                userID = (String) session.getAttribute("userID");
            }
            if(userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요');");
                script.println("location.href='loginView.do';");
                script.println("</script>");
                script.close();
                return;
            }
            boolean emailChecked = new UserDAO().getUserEmailChecked(userID);
            if (emailChecked == false) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("location.href='emailSendConfirmView.do';");
                script.println("</script>");
                script.close();
                return;
            }
            ArrayList<EvaluationDTO> evaluationList = new ArrayList<EvaluationDTO>();
            evaluationList = new EvaluationDAO().getList(lectureDivide, searchType, search, pageNumber);

            request.setAttribute("evaluationList", evaluationList);
        }

        if(path.equals("/report.do")) {
            String userID = null;
            //session.getAttribute("userID") : 로그인이 되어있는 상태
            HttpSession session = request.getSession();
            if(session.getAttribute("userID") != null) {
                //userID에 해당 session 값을 넣어준다.
                userID = (String) session.getAttribute("userID");
            }
            //로그인하지 않은 상태
            if(userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요.');");
                script.println("location.href = 'loginView.do'");
                script.println("</script>");
                script.close();
                return;
            }

            request.setCharacterEncoding("UTF-8");
            String reportTitle = null;
            String reportContent = null;
            if(request.getParameter("reportTitle") != null) {
                reportTitle = request.getParameter("reportTitle");
            }
            if(request.getParameter("reportContent") != null) {
                reportContent = request.getParameter("reportContent");
            }
            if(reportTitle == null || reportContent == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('입력이 안 된 사항이 있습니다.')");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }

            //구글 smtp가 기본적으로 제공하는 양식을 그대로 사용
            String host = "http://localhost:8080/";
            String from = "TESTsjh8924@gmail.com";
            String to = "TESTsjh8924@gmail.com";  //받는 사람
            String subject = "강의평가 사이트에서 접수된 신고 메일입니다.";
            String content = "신고자: " + userID + "<br>제목: " + reportTitle + "<br>내용: " + reportContent;

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
                Authenticator auth = new Gmail();
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
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('오류가 발생했습니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('정상적으로 신고되었습니다.');");
            script.println("location.href='index.jsp';");
            script.println("</script>");
            script.close();
            return;
        }

        if (path.equals("/sendEmail.do")) {
            UserDAO userDAO = new UserDAO();
            String userID = null;
            //session.getAttribute("userID") : 로그인이 되어있는 상태
            HttpSession session = request.getSession();
            if(session.getAttribute("userID") != null) {
                //userID에 해당 session 값을 넣어준다.
                userID = (String) session.getAttribute("userID");
            }
            //로그인하지 않은 상태
            if(userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요.');");
                script.println("location.href = 'loginView.do'");
                script.println("</script>");
                script.close();
                return;
            }

            boolean emailChecked = userDAO.getUserEmailChecked(userID);
            if(emailChecked == true) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('이미 이메일 인증이 완료된 회원입니다.');");
                script.println("location.href = 'index.jsp'");
                script.println("</script>");
                script.close();
                return;
            }

            //구글 smtp가 기본적으로 제공하는 양식을 그대로 사용
            String host = "http://localhost:8080/";
            String from = "TESTsjh8924@gmail.com";  //보내는 사람
            String to = userDAO.getUserEmail(userID);  //받는 사람
            String subject = "강의평가를 위한 이메일 인증 메일입니다.";
            String content = "다음 링크에 접속하여 이메일 인증을 진행하세요." +
                    "<a href='" + host + "emailCheck.do?code=" + new SHA256().getSHA256(to) + "'>이메일 인증하기</a>";

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
                Authenticator auth = new Gmail();
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
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('오류가 발생했습니다.');");
                script.println("history.back();");
                script.println("</script>");
                script.close();
                return;
            }
        }

        if(path.equals("/emailCheck.do")) {
            request.setCharacterEncoding("UTF-8");  //사용자로부터 입력받은 정보는 모두 UTF8로 처리
            String code = null;
            if(request.getParameter("code") != null) {
                code = request.getParameter("code");
            }
            UserDAO userDAO = new UserDAO();
            String userID = null;
            HttpSession session = request.getSession();
            if (session.getAttribute("userID") != null) {
                userID = (String) session.getAttribute("userID");
            }
            if (userID == null) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('로그인을 해주세요.');");
                script.println("location.href='loginView.do'");
                script.println("</script>");
                script.close();
                return;
            }
            String userEmail = userDAO.getUserEmail(userID);
            boolean isRight = (new SHA256().getSHA256(userEmail).equals(code)) ? true : false;
            if (isRight == true) {
                userDAO.setUserEmailChecked(userID);
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('인증에 성공했습니다.');");
                script.println("location.href='index.jsp'");
                script.println("</script>");
                script.close();
                return;
            } else {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('유효하지 않은 코드입니다.');");
                script.println("location.href='loginView.do'");
                script.println("</script>");
                script.close();
                //return;
            }
        }
        if(path.equals("/loginView.do")) {
            System.out.println("로그인 화면으로 이동");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/board/userLogin.jsp");
            dispatcher.forward(request, response);
        }
        if(path.equals("/joinView.do")) {
            System.out.println("회원가입 화면으로 이동");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/board/userJoin.jsp");
            dispatcher.forward(request, response);
        }
        if(path.equals("/emailSendView.do")) {
            System.out.println("이메일 발송 화면으로 이동");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/board/sendEmail.jsp");
            dispatcher.forward(request, response);
        }
        if(path.equals("/emailSendConfirmView.do")) {
            System.out.println("이메일 재발송 화면으로 이동");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/board/emailSendConfirm.jsp");
            dispatcher.forward(request, response);
        }
    }

}
