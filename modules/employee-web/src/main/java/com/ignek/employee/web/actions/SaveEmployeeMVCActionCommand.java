package com.ignek.employee.web.actions;

import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.AppConstants;
import com.ignek.employee.web.constants.EmployeePortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = { "javax.portlet.name=" + EmployeePortletKeys.EMPLOYEE,
		"mvc.command.name=/save_employee" }, service = MVCActionCommand.class)
public class SaveEmployeeMVCActionCommand extends BaseMVCActionCommand {
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		long employeeId = ParamUtil.getLong(actionRequest, AppConstants.EMPLOYEE_ID);
		try {

			String employeeFirstName = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_FIRST_NAME);
			String employeeLastName = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_LAST_NAME);
			String employeeDesignation = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_DESIGANATION);
			String employeeEmailAddress = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_EMAIL_ADDRESS);
			String employeePhoneNumber = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_PHONE_NUMBER);
			String employeeAddressLine1 = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_ADDRESS_LINE_1);
			String employeeAddressLine2 = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_ADDRESS_LINE_2);
			String employeeCity = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_CITY);
			String employeeZipCode = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_ZIP_CODE);
			String employeePassword = ParamUtil.getString(actionRequest, AppConstants.EMPLOYEE_PASSWORD);

			ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);
			if (employeeId > 0) {
				employeeLocalService.updateEmployee(employeeFirstName, employeeLastName, employeeEmailAddress,
						employeeAddressLine1, employeeAddressLine2, employeePhoneNumber, employeeZipCode,
						employeeDesignation, employeeCity, employeeId, serviceContext);
				_log.info("== " + employeeFirstName.concat(employeeLastName)
						+ "password : " + employeePassword);

				_log.info("Employee update successfully ");

			} else {
				employeeLocalService.addEmployee(employeeFirstName, employeeLastName, employeeEmailAddress,
						employeeAddressLine1, employeeAddressLine2, employeePhoneNumber, employeeZipCode,
						employeeDesignation, employeeCity, serviceContext, employeePassword);
				_log.info("Employye create successfully");

			}

		} catch (Exception e) {
			_log.info("something went wrong " + e.getMessage());
		}

	}

	@Reference
	private EmployeeLocalService employeeLocalService;

	private static final Log _log = LogFactoryUtil.getLog(SaveEmployeeMVCActionCommand.class);

}
