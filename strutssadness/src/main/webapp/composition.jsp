<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${title}</title>
</head>
<body>
  <h2>
    <s:property value="messageStore.message" />
    <s:property value="userName" />
  </h2>
</body>
</html>