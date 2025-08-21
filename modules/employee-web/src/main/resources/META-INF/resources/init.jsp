<%@page
	import="com.ignek.employee.web.managementToolbar.display.context.EmployeeDisplayContext"%>
<%@ page
	import="com.ignek.employee.web.managementToolbar.display.context.EmployeeManagementToolbarDisplayContext"%>
<%@page import="java.util.List"%>
<%@page import="com.ignek.employee.model.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="clay" uri="http://liferay.com/tld/clay"%>
<%@ taglib uri="http://liferay.com/tld/frontend"
	prefix="liferay-frontend"%>


<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%><%@
taglib
	uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%><%@
taglib
	uri="http://liferay.com/tld/theme" prefix="liferay-theme"%><%@
taglib
	uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700&display=swap"
	rel="stylesheet">

<liferay-theme:defineObjects />
<liferay-frontend:defineObjects />
<portlet:defineObjects />

<%

EmployeeDisplayContext employeeDisplayContext = EmployeeDisplayContext.create(request, liferayPortletRequest,
		liferayPortletResponse);
EmployeeManagementToolbarDisplayContext employeeManagementToolbarDisplayContext = new EmployeeManagementToolbarDisplayContext(
		request, liferayPortletRequest, liferayPortletResponse, employeeDisplayContext);
%>