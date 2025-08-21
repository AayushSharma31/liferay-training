package com.ignek.employee.web.renderCommands;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.AppConstants;
import com.ignek.employee.web.constants.EmployeePortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = { "javax.portlet.name=" + EmployeePortletKeys.EMPLOYEE,
		"mvc.command.name=/save_employee" }, service = MVCRenderCommand.class)
public class EditEmployeeMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		try {
			long employeeId = ParamUtil.getLong(renderRequest, "editEmployeeId");
			if (employeeId > 0) {
				Employee employee = null;
				try {
					employee = employeeLocalService.getEmployee(employeeId);

				} catch (PortalException e) {

					e.printStackTrace();
				}
				renderRequest.setAttribute(AppConstants.EMPLOYEE, employee);
			}

		} catch (Exception e) {
			_log.info(  e.getMessage(),e);
		}
		return "/save_employee.jsp";
	}

	@Reference
	private EmployeeLocalService employeeLocalService;

	private static final Log _log = LogFactoryUtil.getLog(EditEmployeeMVCRenderCommand.class);
}
