package com.ignek.employee.web.managementToolbar.display.context;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.portlet.PortletURL;
import javax.portlet.RenderURL;
import javax.servlet.http.HttpServletRequest;

public class EmployeeDisplayContext {

	public static EmployeeDisplayContext create(HttpServletRequest httpServletRequest,
			LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse) {

		EmployeeDisplayContext employeeDisplayContext = (EmployeeDisplayContext) liferayPortletRequest
				.getAttribute("EMPLOYEE_DISPLAY_CONTEXT");

		if (employeeDisplayContext == null) {
			employeeDisplayContext = new EmployeeDisplayContext(httpServletRequest, liferayPortletRequest,
					liferayPortletResponse);
			liferayPortletRequest.setAttribute("EMPLOYEE_DISPLAY_CONTEXT", employeeDisplayContext);
		}

		return employeeDisplayContext;
	}

	private EmployeeDisplayContext(HttpServletRequest httpServletRequest, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse) {

		_httpServletRequest = httpServletRequest;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_themeDisplay = (ThemeDisplay) _httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
	}

	public RenderURL getRenderURL() {

		RenderURL renderURL = _liferayPortletResponse.createRenderURL();
		String redirect = PortalUtil.getCurrentURL(_httpServletRequest);
		if (Validator.isNotNull(redirect)) {
			renderURL.getRenderParameters().setValue("redirect", redirect);
		}

		return renderURL;
	}

	public boolean isSearch() {
		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}
		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");
		return _keywords;
	}

	public SearchContainer<Employee> getSearchContainer() throws PortalException {

		SearchContainer<Employee> employeeSearchContainer = new SearchContainer<>(_liferayPortletRequest,
				getPortletURL(), null, "emptyMessage");

		List<Employee> employeesList = new ArrayList<>(
				EmployeeLocalServiceUtil.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS));

		String keyWords = getKeywords();
		String filterBy = getFilterByType();
		String orderByCol = getOrderByType();

		if (Validator.isNotNull(filterBy) && !filterBy.equals("all")) {
			employeesList = employeesList.stream()
					.filter(e -> e.getDesignation().toLowerCase().contains(filterBy.toLowerCase()))
					.collect(Collectors.toList());
		}

		if (Validator.isNotNull(keyWords)) {
			employeesList = employeesList.stream()
					.filter(e -> e.getFirstName().toLowerCase().contains(keyWords.toLowerCase())
							|| e.getEmailAddress().toLowerCase().contains(keyWords.toLowerCase())
							|| e.getPhoneNumber().toLowerCase().contains(keyWords.toLowerCase())
							|| e.getCity().toLowerCase().contains(keyWords.toLowerCase())
							|| e.getDesignation().toLowerCase().contains(keyWords.toLowerCase()))
					.collect(Collectors.toList());
		}

		if (Validator.isNotNull(orderByCol)) {
			if (orderByCol.equals("designation")) {
				Collections.sort(employeesList, new Comparator<Employee>() {
					public int compare(Employee e1, Employee e2) {
						return e1.getDesignation().compareToIgnoreCase(e2.getDesignation());
					}
				});
			} else if (orderByCol.equals("city")) {
				Collections.sort(employeesList, new Comparator<Employee>() {
					public int compare(Employee e1, Employee e2) {
						return e1.getCity().compareToIgnoreCase(e2.getCity());
					}
				});
			}
		}

		employeeSearchContainer.setResultsAndTotal(employeesList);
		_httpServletRequest.setAttribute("employeeData", employeesList);

		_employeeSearchContainer = employeeSearchContainer;
		return _employeeSearchContainer;
	}

	public boolean isFilteredByType() {
		if (getFilterByType() != null) {
			return true;
		}
		return false;
	}

	public String getFilterByType() {
		if (_filterByType != null) {
			return _filterByType;
		}
		_filterByType = ParamUtil.getString(_httpServletRequest, getFilterByTypeParam(), "all");

		return _filterByType;
	}

	public String getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}
		_orderByType = ParamUtil.getString(_httpServletRequest, getOrderByTypeParam(), "designation");

		return _orderByType;
	}

	public String getFilterByTypeParam() {
		return "filterByType";
	}

	public String getOrderByTypeParam() {
		return "orderByCol";
	}

	public PortletURL getPortletURL() {

		PortletURL portletURL = _liferayPortletResponse.createRenderURL();

		String keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		if (Validator.isNotNull(keywords)) {
			portletURL.getRenderParameters().setValue("keywords", keywords);

		}

		String cur = ParamUtil.getString(_httpServletRequest, "cur");
		if (Validator.isNotNull(cur)) {
			portletURL.getRenderParameters().setValue("cur", cur);
		}

		String delta = ParamUtil.getString(_httpServletRequest, "delta");
		if (Validator.isNotNull(delta)) {
			portletURL.getRenderParameters().setValue("delta", delta);

		}

		String resetCur = ParamUtil.getString(_httpServletRequest, "resetCur");
		if (Validator.isNotNull(resetCur)) {
			portletURL.getRenderParameters().setValue("resetCur", resetCur);
		}

		String orderBy = ParamUtil.getString(_httpServletRequest, "orderBy");
		if (Validator.isNotNull(orderBy)) {
			portletURL.getRenderParameters().setValue("orderBy", orderBy);

		}

		String filterBy = ParamUtil.getString(_httpServletRequest, "filterBy");
		if (Validator.isNotNull(filterBy)) {
			portletURL.getRenderParameters().setValue("filterByType", filterBy);

		}

		return portletURL;
	}

	private SearchContainer<Employee> _employeeSearchContainer;
	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private String _orderByType;
	private String _keywords;
	private String _filterByType;
	private String _orderByCol;
	private final ThemeDisplay _themeDisplay;
	private static final Log _log = LogFactoryUtil.getLog(EmployeeDisplayContext.class);

}
