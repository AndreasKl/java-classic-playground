<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
</head>
<body>
  <p>
    <a href="<s:url action='composition'/>">Link to composition</a>
  </p>
  <s:url var="compositionLink" action="composition">
    <s:param name="userName">Sample User</s:param>
  </s:url>
  <p>
    <a href="${compositionLink}">Hello Sample User</a>
  </p>
  <s:form action="composition">
    <s:textfield name="userName" label="Your name" />
    <s:submit value="Submit" />
  </s:form>
</body>
</html>