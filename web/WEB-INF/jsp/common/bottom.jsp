

</div>

<div id="Footer">

    <div id="PoweredBy">&nbsp
        <a href="http://www.csu.edu.cn">www.csu.edu.cn</a>
    </div>

    <div id="Banner">
        <!--登录用户的banner -->
        <c:if test="${sessionScope.loginAccount != null }">
            <c:if test="${sessionScope.loginAccount.bannerOption}">
                ${sessionScope.loginAccount.bannerName}
            </c:if>
        </c:if>
    </div>

</div>
</body>
</html>