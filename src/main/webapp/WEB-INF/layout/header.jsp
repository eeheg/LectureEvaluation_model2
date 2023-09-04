<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>강의평가 웹 사이트</title>
    <link rel="stylesheet" href="css/custom.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="index.jsp">강의평가 웹 사이트</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div id="navbar" class="collapse navbar-collapse">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="index.jsp">메인</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" id="dropdown" data-toggle="dropdown">
                    회원 관리
                </a>
                <div class="dropdown-menu" aria-labelledby="dropdown">
                    <% if (userID == null) { %>
                    <a class="dropdown-item" href="loginView.do">로그인</a>
                    <a class="dropdown-item" href="insertUserView.do">회원가입</a>
                    <% } else { %>
                    <a class="dropdown-item" href="logout.do">로그아웃</a>
                    <% } %>
                </div>
            </li>
        </ul>
        <form action="index.jsp" method="get" class="form-inline my-2 my-lg-0">
            <input type="text" name="search" class="form-control mr-sm-2" type="search" placeholder="내용을 입력하세요." aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">검색</button>
        </form>
    </div>
</nav>

</body>
</html>
