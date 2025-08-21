package com.ignek.employee.web.managementToolbar.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItemListBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

public class EmployeeManagementToolbarDisplayContext extends SearchContainerManagementToolbarDisplayContext {

	public EmployeeManagementToolbarDisplayContext(HttpServletRequest httpServletRequest,
			LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse,
			EmployeeDisplayContext employeeDisplayContext) throws PortalException {
		super(httpServletRequest, liferayPortletRequest, liferayPortletResponse,
				employeeDisplayContext.getSearchContainer());
		_employeeDisplayContext = employeeDisplayContext;
		_httpServletRequest = httpServletRequest;
		_themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
	}

	@Override
	public Boolean isSelectable() {
		return true;
	}

	@Override
	public Boolean isShowCreationMenu() {
		return false;
	}

	@Override
	public Boolean isShowSearch() {
		return true;
	}

	@Override
	protected String[] getOrderByKeys() {
		return new String[] { "city", "designation" };
	}

	@Override
	public String getSearchActionURL() {
		PortletURL searchActionURL = getPortletURL();
		return searchActionURL.toString();

	}

	@Override
	public String getSearchContainerId() {
		return "employeesEntries";
	}

	@Override
	public String getSearchFormName() {

		return "searchFm";
	}

	@Override
	public Boolean isDisabled() {
		return false;
	}

	@Override
	protected PortletURL getPortletURL() {
		return _employeeDisplayContext.getPortletURL();
	}

	@Override
	public List<DropdownItem> getFilterDropdownItems() {

		return DropdownItemListBuilder.addGroup(dropdownGroupItem -> {
			dropdownGroupItem.setDropdownItems(getFilterByEmployeeField());
			dropdownGroupItem.setLabel("type");
		}).build();
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemListBuilder.add(dropdownItem -> {
			dropdownItem.putData("action", "filterSelectedType");
			dropdownItem.setLabel(LanguageUtil.get(_httpServletRequest, "software developer"));
			dropdownItem.setQuickAction(true);
		}).add(dropdownItem -> {
			dropdownItem.putData("action", "filterSelectedType");
			dropdownItem.setLabel(LanguageUtil.get(_httpServletRequest, "junior software developer"));
			dropdownItem.setQuickAction(true);
		}).add(dropdownItem -> {
			dropdownItem.putData("action", "filterSelectedType");
			dropdownItem.setLabel(LanguageUtil.get(_httpServletRequest, "senior software developer"));
			dropdownItem.setQuickAction(true);
		}).add(dropdownItem -> {
			dropdownItem.putData("action", "filterSelectedType");
			dropdownItem.setLabel(LanguageUtil.get(_httpServletRequest, "all"));
			dropdownItem.setQuickAction(true);
		}).build();

	}

	private List<DropdownItem> getFilterByEmployeeField() {

		return DropdownItemListBuilder.add(dropdownItem -> {
			dropdownItem.setActive(Objects.equals(_employeeDisplayContext.getFilterByType(), "software developer"));
			dropdownItem.setHref(getPortletURL(), _employeeDisplayContext.getFilterByTypeParam(), "software developer");
			dropdownItem.setLabel("Software developer");
		}).add(dropdownItem -> {
			dropdownItem
					.setActive(Objects.equals(_employeeDisplayContext.getFilterByType(), "junior software developer"));
			dropdownItem.setHref(getPortletURL(), _employeeDisplayContext.getFilterByTypeParam(),
					"junior software developer");
			dropdownItem.setLabel("Junior Software Developer");
		}).add(dropdownItem -> {
			dropdownItem
					.setActive(Objects.equals(_employeeDisplayContext.getFilterByType(), "senior software developer"));
			dropdownItem.setHref(getPortletURL(), _employeeDisplayContext.getFilterByTypeParam(),
					"senior software developer");
			dropdownItem.setLabel(LanguageUtil.get(_httpServletRequest, "Senior Software Developer"));
		}).add(dropdownItem -> {
			dropdownItem.setActive(Objects.equals(_employeeDisplayContext.getFilterByType(), "all"));
			dropdownItem.setHref(getPortletURL(), _employeeDisplayContext.getFilterByTypeParam(), "all");
			dropdownItem.setLabel(LanguageUtil.get(_httpServletRequest, "All"));
		}).build();

	}

	@Override
	public List<LabelItem> getFilterLabelItems() {

		return LabelItemListBuilder.add(_employeeDisplayContext::isFilteredByType, labelItem -> {
			PortletURL removeLabelURL = PortletURLUtil.clone(currentURLObj, liferayPortletResponse);

			removeLabelURL.setParameter(_employeeDisplayContext.getFilterByTypeParam(), (String) null);

			labelItem.putData("removeLabelURL", removeLabelURL.toString());

			labelItem.setCloseable(true);

			String type = _employeeDisplayContext.getFilterByType();

			String message = (type == "name") ? "Name"
					: (type == "designation" ? "Designation" : (type == "city") ? "City" : "All");

			labelItem.setLabel("type" + " : " + LanguageUtil.get(_httpServletRequest, message));

		})

				.build();
	}

	private final EmployeeDisplayContext _employeeDisplayContext;
	private final ThemeDisplay _themeDisplay;
	private final HttpServletRequest _httpServletRequest;
	private static final Log _log = LogFactoryUtil.getLog(EmployeeManagementToolbarDisplayContext.class);

}
