<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- <%@include file="/common/include/directives.jsp"%> --> 

   <center><h4>배너</h4></center>

   <p align="right">
      <security:authentication property="principal.username"/>&nbsp;
      <!-- ${SPRING_SECURITY_CONTEXT.authentication.principal.username} -->
      [<a href="/main.html?i18n=en">English</a>]
   </p>
