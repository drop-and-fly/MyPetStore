<%@ include file="../common/top.jsp"%>

<div id="Catalog">

  <form action="signOn" method="post">
    <p>Please enter your username and password.</p>
    <c:if test="${requestScope.signOnMsg != null}">
      <p>
        <font color="red">${requestScope.signOnMsg}</font>
      </p>
    </c:if>
    <p>
      Username:<input type="text" name="username"/> <br />
      Password:<input type="password" name="password"/>
    </p>
    <input type="submit" value="Login">
  </form>
  Need a user name and password?
  <a href="registerForm">Register Now!</a>

</div>

<%@ include file="../common/bottom.jsp"%>