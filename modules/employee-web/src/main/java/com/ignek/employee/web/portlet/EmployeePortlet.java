package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.service.EmployeeLocalServiceUtil;
import com.ignek.employee.web.constants.AppConstants;
import com.ignek.employee.web.constants.EmployeePortletKeys;
import com.ignek.employee.web.util.EmployeePDFUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.persistence.PortletUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author ignek
 */
@Component(property = { "com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Employee", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp", "javax.portlet.name=" + EmployeePortletKeys.EMPLOYEE,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.version=3.0" }, service = Portlet.class)
public class EmployeePortlet extends MVCPortlet {

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {

		List<Employee> allEmployees = employeeLocalService.getAllEmployess();
		renderRequest.setAttribute(AppConstants.ALL_EMPLOYEES, allEmployees);

		super.doView(renderRequest, renderResponse);

	}

	@Override
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws IOException, PortletException {

		try {
			long employeeId = ParamUtil.getLong(resourceRequest, AppConstants.EMPLOYEE_ID);

			Employee employee = EmployeeLocalServiceUtil.getEmployee(employeeId);
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

			Locale locale = themeDisplay.getLocale();
			byte[] pdfData = EmployeePDFUtil.generateEmployeePDF(employee, locale);

			resourceResponse.setContentType("application/pdf");
			resourceResponse.addProperty("Content-Disposition",
					"attachment; filename=employee_" + employee.getFirstName() + "_" + employee.getLastName() + ".pdf");

			OutputStream out = resourceResponse.getPortletOutputStream();
			out.write(pdfData);
			out.flush();
			out.close();
		} catch (Exception exception) {
			_log.error("Something went wrong in pdf generation : " + exception.getMessage());
			resourceResponse.getWriter().write("PDF generation failed.");
		}
	}

	@ProcessAction(name = "/delete_employee")
	public void deleteEmployeeAction(ActionRequest actionRequest, ActionResponse actionResponse)
			throws PortalException {
		try {
			long employeeId = ParamUtil.getLong(actionRequest, AppConstants.EMPLOYEE_ID);
			employeeLocalService.deleteEmployeeById(employeeId);
			_log.info("Employee deleted successfully ");

		} catch (Exception e) {
			_log.info(e.getMessage(), e);
		}

	}

	@Reference
	private EmployeeLocalService employeeLocalService;
	@Reference
	private Portal portal;

	private static final Log _log = LogFactoryUtil.getLog(EmployeePortlet.class);
}