<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page
	import="com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="java.util.List"%>
<%@page
	import="com.ignek.employee.web.managementToolbar.display.context.EmployeeManagementToolbarDisplayContext"%>
<%@page import="com.ignek.employee.model.Employee"%>
<%@ include file="/init.jsp"%>

<%
SearchContainer<Employee> employeeSearchContainer = employeeDisplayContext.getSearchContainer();
List<Employee> e = employeeSearchContainer.getResults();
List<Employee> employees = (List<Employee>) request.getAttribute("employeeData");
int totalRecords = employees.size();
%>

<portlet:renderURL var="addEmployeeRenderURL">
	<portlet:param name="mvcPath" value="/save_employee.jsp" />
</portlet:renderURL>

<portlet:renderURL var="customSearchContainer">
	<portlet:param name="mvcPath" value="/custom_search_container.jsp" />
</portlet:renderURL>
<div class="content-div">

	<portlet:renderURL var="testEmployeeRenderURL">
		<portlet:param name="mvcPath" value="/details.jsp" />
	</portlet:renderURL>


	<liferay-portlet:renderURL varImpl="iteratorURL">


	</liferay-portlet:renderURL>
	<liferay-portlet:renderURL varImpl="clearURL">
	</liferay-portlet:renderURL>

	<div class="content-div">
		<div class="d-flex flex-row justify-content-between m-3">
			<h1>Employee</h1>
			<div class="text-right">
				<a href="<%=addEmployeeRenderURL%>" class="btn  custom-btn"> Add
					Employee </a>
			</div>
		</div>

		<div class="">
			<aui:form method="get" name="searchFm"
				action="<%=employeeManagementToolbarDisplayContext.getSearchActionURL()%>">

				<clay:management-toolbar
					displayContext="<%=employeeManagementToolbarDisplayContext%>"
					itemsTotal="<%=employees.size()%>" clearResultsURL="${clearURL }" />

				<liferay-ui:search-container
					emptyResultsMessage="No employee found, Please add Users"
					searchContainer="<%=employeeSearchContainer%>"
					total="<%=totalRecords%>" delta="10">

					<%
					int startResult = searchContainer.getStart();
					int endResult = searchContainer.getEnd();
					if (endResult >= totalRecords) {
						endResult = totalRecords;
					}
					%>
					<liferay-ui:search-container-results
						results="<%=employees.subList(startResult, endResult)%>" />


					<liferay-ui:search-container-row
						className="com.ignek.employee.model.Employee" modelVar="employee"
						keyProperty="employeeId" indexVar="indexNumber">
						<liferay-ui:search-container-column-text name="S.No"
							value="<%=String.valueOf(searchContainer.getStart() + indexNumber)%>" />

						<liferay-ui:search-container-column-text name="Name"
							value="<%=employee.getFirstName().concat("  ").concat(employee.getLastName())%>" />
						<liferay-ui:search-container-column-text name="Designation"
							value="${employee.getDesignation()}" />

						<liferay-ui:search-container-column-text name="Phone"
							value="${employee.getPhoneNumber()}" />
						<liferay-ui:search-container-column-text name="Email"
							value="${employee.getEmailAddress()}" />
						<liferay-ui:search-container-column-text name="City"
							value="${employee.getCity()}" />
						<liferay-ui:search-container-column-text name="Action">

							<div class="d-flex flex-row justify-content-around">
								<portlet:renderURL var="editEmployeeRenderURL">
									<portlet:param name="editEmployeeId"
										value="${employee.getEmployeeId()}" />
									<portlet:param name="mvcRenderCommandName"
										value="/save_employee" />
								</portlet:renderURL>
								<a href="<%=editEmployeeRenderURL%>"><img
									src="<%=request.getContextPath()%>/images/edit.png"> </a>


								<portlet:actionURL name="/delete_employee"
									var="deleteEmployeeActionURL">
									<portlet:param name="employeeId"
										value="${employee.getEmployeeId()}" />
								</portlet:actionURL>
								<a href="#" class="open-delete-modal" data-toggle="modal"
									data-target="#exampleModal"
									data-delete-url="<%=deleteEmployeeActionURL%>"> <img
									src="<%=request.getContextPath()%>/images/trash.png">
								</a>

								<portlet:resourceURL var="downloadPdfURL">
									<portlet:param name="employeeId"
										value="${employee.getEmployeeId()}" />
									<portlet:param name="downloadType" value="downloadEmployeePdf" />
								</portlet:resourceURL>

								<a href="<%=downloadPdfURL%>"><img
									src="<%=request.getContextPath()%>/images/vector.png"> </a>
							</div>
						</liferay-ui:search-container-column-text>

					</liferay-ui:search-container-row>
					<liferay-ui:search-iterator displayStyle="list"
						markupView="lexicon" />
				</liferay-ui:search-container>
			</aui:form>
		</div>

	</div>
	<a href="<%=testEmployeeRenderURL%>"> Testing jsp </a> <a
		href="<%=customSearchContainer%>"> Custom Search Container </a>

	<!-- Delete Confirmation Modal -->
	<div class="modal fade delete-modal-css" id="exampleModal"
		tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">

			<div class="modal-content border-0 shadow-lg rounded">

				<div class="card delete-modal-css mb-0">
					<div class="card-body">

						<h1 class="mb-4 modal-message ">Are you sure you want to
							delete this employee ?</h1>

						<div class="d-flex justify-content-end">
							<button type="button" class="btn delete-form-no-button mr-2"
								data-dismiss="modal">Cancel</button>
							<button type="button" id="confirmDelete"
								class="btn  delete-form-button">Delete</button>
						</div>

					</div>
				</div>

			</div>

		</div>
	</div>

	<style>
.delete-form-button {
	background-color: #00979E;
	color: white;
	width: 122px;
}

.delete-form-no-button {
	background-color: white;
	color: black;
	width: 122px;
	border: 1px solid black;
}

.modal-message {
	font-weight: 500;
	font-family: 'Poppins';
}

.delete-modal-css {
	border: 2px solid black;
}

.content-div {
	margin: 30px;
}

td {
	padding: 10px;
	border-bottom: 15px solid transparent;
	background-clip: padding-box;
}

.table thead {
	background-color: #F0F1F1;
	color: #ACACAC;
}

.table-data tr {
	font-weight: 400;
	font-family: 'Poppins';
}

.table-bordered td {
	border-style: none;
}

.table-striped tbody tr:nth-of-type(odd):not(.table-active):not(.table-disabled):not(.table-divider),
	.table-striped tbody tr:nth-of-type(odd):not(.table-active):not(.table-disabled):not(.table-divider) td,
	.table-striped tbody tr:nth-of-type(odd):not(.table-active):not(.table-disabled):not(.table-divider) th
	{
	background-color: white;
	margin-bottom: 5px;
}
</style>

	<script>
let deleteURL = '';

document.querySelectorAll('.open-delete-modal').forEach(btn => {
    btn.addEventListener('click', function () {
        deleteURL = this.dataset.deleteUrl;
    });
});

document.getElementById('confirmDelete').addEventListener('click', function () {
    if (deleteURL) {
        window.location.href = deleteURL;
    }
});
</script>