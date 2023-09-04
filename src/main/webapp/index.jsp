<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="com.ssamz.biz.evaluation.EvaluationDAO"%>
<%@ page import="com.ssamz.biz.evaluation.EvaluationDTO"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.List" %>

<!-- navbar -->
<%@ include file="WEB-INF/layout/header.jsp" %>

<!-- 홈페이지 content -->
<section class="container">
    <form method="get" action="index.jsp" class="form-inline mt-3">
        <select name="lectureDivide" class="form-control mx-1 mt-2">
            <option value="전체">전체</option>
            <option value="전공" <% if(lectureDivide.equals("전공")) out.println("selected"); %>>전공</option>
            <option value="교양" <% if(lectureDivide.equals("교양")) out.println("selected"); %>>교양</option>
            <option value="기타" <% if(lectureDivide.equals("기타")) out.println("selected"); %>>기타</option>
        </select>
        <select name="searchType" class="form-control mx-1 mt-2">
            <option value="최신순">최신순</option>
            <option value="추천순" <% if(searchType.equals("추천순")) out.println("selected"); %>>추천순</option>
        </select>
        <input type="text" name="search" class="form-control mx-1 mt-2" placeholder="내용을 입력하세요."/>
        <button type="submit" class="btn btn-primary mx-1 mt-2">검색</button>
        <a class="btn btn-primary mx-1 mt-2" data-toggle="modal" href="#registerModal">등록하기</a>
        <a class="btn btn-danger mx-1 mt-2" data-toggle="modal" href="#reportModal">신고</a>
    </form>

    <%
        List<EvaluationDTO> evaluationList = (List) request.getAttribute("evaluationList");
        if(evaluationList != null) {
            for(int i = 0; i < evaluationList.size(); i++) {
                if(i==5) break;  //6개의 강의평가글 중 5개까지만 출력
                EvaluationDTO evaluation = evaluationList.get(i);
    %>

    <div class="card bg-light mt-3">
        <div class="card-header bg-light">
            <div class="row">
                <div class="col-8 text-left"><%= evaluation.getLectureName()%>&nbsp;<small><%= evaluation.getProfessorName()%></small></div>
                <div class="col-4 text-right">
                    종합<span style="color:red;"><%= evaluation.getTotalScore()%></span>
                </div>
            </div>
        </div>
        <div class="card-body">
            <h5 class="card-title">
                <%= evaluation.getEvaluationTitle()%>&nbsp;<small>(<%=evaluation.getLectureYear()%> <%=evaluation.getSemesterDivide()%>)</small>
            </h5>
            <p class="card-text"><%= evaluation.getEvaluationContent()%></p>
            <div class="row">
                <div class="col-9 text-left">
                    성적<span style="color:red;"><%=evaluation.getCreditScore()%></span>
                    널널<span style="color:red;"><%=evaluation.getComfortableScore()%></span>
                    강의<span style="color:red;"><%=evaluation.getLectureScore()%></span>
                    <span style="color:green;">(추천: <%=evaluation.getLikeCount()%>)</span>
                </div>
                <div class="col-3 text-right">
                    <a onclick="return confirm('추천하시겠습니까?')" href="like.do?evaluationID=<%=evaluation.getEvaluationID()%>">추천</a>
                    <a onclick="return confirm('삭제하시겠습니까?')" href="deleteBoard.do?evaluationID=<%=evaluation.getEvaluationID()%>">삭제</a>
                </div>
            </div>
        </div>
    </div>

    <% }} %>

</section>
<ul class="pagination justify-content-center mt-3">
    <li class="page-item">
        <%
            if(pageNumber<=0) {
        %>
            <a class="page-link disabled">이전</a>
        <%
            } else {
        %>
        <a class="page-link" href="./index.jsp?lectureDivide=<%=URLEncoder.encode(lectureDivide,"UTF-8")%>&searchType=<%=URLEncoder.encode(searchType,"UTF-8")%>&search=<%=URLEncoder.encode(search,"UTF-8")%>&pageNumber=<%=pageNumber - 1%>"
        >
                이전
            </a>
        <%
            }
        %>
    </li>
    <li class="page-item">
        <%
            if(evaluationList.size() < 6) {
        %>
        <a class="page-link disabled">다음</a>
        <%
        } else {
        %>
        <a class="page-link" href="./index.jsp?lectureDivide=<%=URLEncoder.encode(lectureDivide,"UTF-8")%>&searchType=<%=URLEncoder.encode(searchType,"UTF-8")%>&search=<%=URLEncoder.encode(search,"UTF-8")%>&pageNumber=<%=pageNumber + 1%>"
        >
            다음
        </a>
        <%
            }
        %>
    </li>
</ul>
<!-- 등록하기 모달창 -->
<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" > <!-- id="modal" --> 평가등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="insertBoard.do" method="post">
                    <div class="form-row">
                        <div class="form-group col-sm-6">
                            <label>강의명</label>
                            <input type="text" name="lectureName" class="form-control" maxlength="20">
                        </div>
                        <div class="form-group col-sm-6">
                            <label>교수명</label>
                            <input type="text" name="professorName" class="form-control" maxlength="20">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-sm-4">
                            <label>수강 연도</label>
                            <select name="lectureYear" class="form-control">
                                <option value="2015">2015</option>
                                <option value="2016">2016</option>
                                <option value="2017">2017</option>
                                <option value="2018">2018</option>
                                <option value="2019">2019</option>
                                <option value="2020">2020</option>
                                <option value="2021">2021</option>
                                <option value="2022">2022</option>
                                <option value="2023" selected>2023</option>
                                <option value="2024">2024</option>
                                <option value="2025">2025</option>
                                <option value="2026">2026</option>
                                <option value="2027">2027</option>
                            </select>
                        </div>
                        <div class="form-group col-sm-4">
                            <label>수강 학기</label>
                            <select name="semesterDivide" class="form-control">
                                <option value="1학기" selected>1학기</option>
                                <option value="여름학기" >여름학기</option>
                                <option value="2학기" >2학기</option>
                                <option value="겨울학기" >겨울학기</option>
                            </select>
                        </div>
                        <div class="form-group col-sm-4">
                            <label>강의 구분</label>
                            <select name="lectureDivide" class="form-control">
                                <option value="전공" selected>전공</option>
                                <option value="교양" >교양</option>
                                <option value="기타" >기타</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>제목</label>
                        <input type="text" name="evaluationTitle" class="form-control" maxlength="30">
                    </div>
                    <div class="form-group">
                        <label>내용</label>
                        <textarea name="evaluationContent" class="form-control" maxlength="2048" style="height:180px;"></textarea>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-sm-3">
                            <label>종합</label>
                            <select name="totalScore" class="form-control">
                                <option value="A" selected>A</option>
                                <option value="B">B</option>
                                <option value="C">C</option>
                                <option value="D">D</option>
                                <option value="F">F</option>
                            </select>
                        </div>
                        <div class="form-group col-sm-3">
                            <label>성적</label>
                            <select name="creditScore" class="form-control">
                                <option value="A" selected>A</option>
                                <option value="B">B</option>
                                <option value="C">C</option>
                                <option value="D">D</option>
                                <option value="F">F</option>
                            </select>
                        </div>
                        <div class="form-group col-sm-3">
                            <label>널널</label>
                            <select name="comfortableScore" class="form-control">
                                <option value="A" selected>A</option>
                                <option value="B">B</option>
                                <option value="C">C</option>
                                <option value="D">D</option>
                                <option value="F">F</option>
                            </select>
                        </div>
                        <div class="form-group col-sm-3">
                            <label>강의</label>
                            <select name="lectureScore" class="form-control">
                                <option value="A" selected>A</option>
                                <option value="B">B</option>
                                <option value="C">C</option>
                                <option value="D">D</option>
                                <option value="F">F</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                        <button type="submit" class="btn btn-primary">등록하기</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 신고 모달창 -->
<div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modal">신고하기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="report.do" method="post">
                    <div class="form-group">
                        <label>신고 제목</label>
                        <input type="text" name="reportTitle" class="form-control" maxlength="30">
                    </div>
                    <div class="form-group">
                        <label>신고 내용</label>
                        <textarea name="reportContent" class="form-control" maxlength="2048" style="height:180px;"></textarea>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                        <button type="submit" class="btn btn-danger">신고하기</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 홈페이지 footer -->
<%@ include file="WEB-INF/layout/footer.jsp" %>
