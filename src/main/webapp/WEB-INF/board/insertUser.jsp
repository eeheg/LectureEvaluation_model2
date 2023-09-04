<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.PrintWriter" %>
<%
    String userID = null;
    if(session.getAttribute("userID") != null) {
        userID = (String) session.getAttribute("userID");
    }
    if(userID != null) {
        PrintWriter script = response.getWriter();
        script.println("<script>");
        script.println("alert('로그인이 된 상태입니다.');");
        script.println("location.href='index.jsp'");
        script.println("</script>");
        script.close();
        return;
    }
%>

<!-- navbar -->
<%@ include file="/WEB-INF/layout/header.jsp" %>

<section class="container mt-3" style="max-width:560px;">
    <form method="post" action="insertUser.do">
        <div class="form-group  mt-3">
            <label>아이디</label>
            <input type="text" name="userID" class="form-control">
        </div>
        <div class="form-group  mt-3">
            <label>비밀번호</label>
            <input type="password" name="userPassword" class="form-control">
        </div>
        <div class="form-group  mt-3">
            <label>이메일</label>
            <input type="email" name="userEmail" class="form-control">
        </div>
        <button type="submit" class="btn btn-primary mt-3">회원가입</button>
    </form>
</section>

<!-- 홈페이지 footer -->
<%@ include file="/WEB-INF/layout/footer.jsp" %>