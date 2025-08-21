<%@page import="com.ignek.employee.web.constants.AppConstants"%>
<%@ page import="com.ignek.employee.model.Employee"%>
<%@ include file="/init.jsp"%>

<%
Employee employee = (Employee) renderRequest.getAttribute(AppConstants.EMPLOYEE);
%>

<portlet:actionURL name="/save_employee" var="saveEmployeeActionURL" />

<div class="card m-3">
	<div class="card-body">
		<div class="container">
			<h1 class="form-heading mt-3">Employee Form</h1>

			<div class="mt-5">
				<aui:form action="${saveEmployeeActionURL}" method="POST">

					<aui:input name="employeeId" type="hidden"
						value="<%=(employee != null) ? employee.getEmployeeId() : 0%>" />

					<aui:row cssClass="mb-3">
						<aui:col width="50">
							<aui:input label="First Name"
								cssClass="bg-white employee-form-input-field" name="firstName"
								type="text" placeholder="Enter your first name"
								value="<%=(employee != null) ? employee.getFirstName() : ""%>">
								<aui:validator name="required" />
							</aui:input>
						</aui:col>
						<aui:col width="50">
							<aui:input label="Last Name"
								cssClass="bg-white employee-form-input-field" name="lastName"
								type="text" placeholder="Enter your last name"
								value="<%=(employee != null) ? employee.getLastName() : ""%>" />
						</aui:col>
					</aui:row>

					<aui:row cssClass="mb-3">
						<aui:col width="100">
							<aui:input label="Designation"
								cssClass="bg-white employee-form-input-field" name="designation"
								type="text" placeholder="Enter your designation"
								value="<%=(employee != null) ? employee.getDesignation() : ""%>" />
						</aui:col>
					</aui:row>

					<aui:row cssClass="mb-3">
						<aui:col width="50">
							<aui:input label="Email"
								cssClass="bg-white employee-form-input-field"
								name="emailAddress" type="email" placeholder="Enter your email"
								value="<%=(employee != null) ? employee.getEmailAddress() : ""%>">
								<aui:validator name="required" />
							</aui:input>
						</aui:col>
						<aui:col width="50">
							<aui:input label="Phone"
								cssClass="bg-white employee-form-input-field" name="phoneNumber"
								type="text" placeholder="Enter your phone number"
								value="<%=(employee != null) ? employee.getPhoneNumber() : ""%>" />
						</aui:col>
					</aui:row>

					<aui:row cssClass="mb-3">
						<aui:col width="50">
							<aui:input label="Address Line 1"
								cssClass="bg-white employee-form-input-field"
								name="addressLine1" type="text"
								placeholder="Enter your house no / bldg."
								value="<%=(employee != null) ? employee.getAddressLine1() : ""%>" />
						</aui:col>
						<aui:col width="50">
							<aui:input label="Address Line 2"
								cssClass="bg-white employee-form-input-field"
								name="addressLine2" type="text"
								placeholder="Enter your street / area"
								value="<%=(employee != null) ? employee.getAddressLine2() : ""%>" />
						</aui:col>
					</aui:row>

					<aui:row cssClass="mb-4">
						<aui:col width="50">
							<aui:input label="City"
								cssClass="bg-white employee-form-input-field" name="city"
								type="text" placeholder="Enter your city"
								value="<%=(employee != null) ? employee.getCity() : ""%>" />
						</aui:col>
						<aui:col width="50">
							<aui:input label="Postal Code/Zip Code"
								cssClass="bg-white employee-form-input-field" name="zipCode"
								type="text" placeholder="Enter your post code/ zip code"
								value="<%=(employee != null) ? employee.getZipCode() : ""%>" />
						</aui:col>
					</aui:row>
					<%
					if (employee == null) {
					%>
					<aui:row cssClass="mb-3">
						<aui:col width="100">
							<aui:input label="Password"
								cssClass="bg-white employee-form-input-field" name="password"
								type="password" placeholder="Enter your password" />
						</aui:col>

					</aui:row>
					<%
					}
					%>

					<aui:button-row cssClass="text-right">
						<aui:button name="submitButton" type="submit"
							cssClass="custom-btn" value="Submit" />
					</aui:button-row>

				</aui:form>
			</div>
		</div>
	</div>
</div>

<style>
</style>
