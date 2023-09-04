<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.PrintWriter" %>
<%
    String userID = null;
    if(session.getAttribute("userID") != null) {
        userID = (String) session.getAttribute("userID");
    }
    if(userID == null) {
        PrintWriter script = response.getWriter();
        script.println("<script>");
        script.println("alert('로그인을 해주세요.');");
        script.println("location.href='loginView.do'");
        script.println("</script>");
        script.close();
        return;
    }
%>

<!-- navbar -->
<%@ include file="/WEB-INF/layout/header.jsp" %>

<section class="container mt-3" style="max-width:560px;">
    <div class="alert alert-warning mt-4" role="alert">
        이메일 주소 인증을 하셔야 이용 가능합니다. 인증 메일을 받지 못하셨나요?
    </div>
    <a href="sendEmail.do" class="btn btn-primary">인증메일 다시 받기</a>
</section>

<!-- 홈페이지 footer -->
<%@ include file="/WEB-INF/layout/footer.jsp" %>