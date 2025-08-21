package com.ignek.employee.web.activators;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "key=logout.events.pre" }, service = LifecycleAction.class)
public class LogoutPreAction implements LifecycleAction {

	public Map<String, Serializable> getStringSerializableHashMap() {
		return new HashMap<>();
	}

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		try {
			long userId = 0;
			HttpServletRequest request = lifecycleEvent.getRequest();

			userId = PortalUtil.getUser(request).getUserId();
			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

			String iPAddress = serviceContext.getRemoteAddr();

			ObjectDefinition activity = objectDefinitionLocalService
					.getObjectDefinitionByExternalReferenceCode("activity", serviceContext.getCompanyId());

			Map<String, Serializable> activityObjectValues = getStringSerializableHashMap();

			activityObjectValues.put("activityType", "Employee Logout");
			activityObjectValues.put("details",
					"This is the message to show that emplyee logged out and its activity details are here");
			activityObjectValues.put("iPAddress", iPAddress);
			activityObjectValues.put("employeeId", userId);

			ObjectEntry activityEntry = objectEntryLocalService.addObjectEntry(serviceContext.getUserId(), 0,
					activity.getObjectDefinitionId(), activityObjectValues, serviceContext);

			_log.info("User logged out successfully with userId " + userId);

		}

		catch (Exception e) {
			_log.info(e.getMessage(),e);
		}

	}

	@Reference
	private ObjectDefinitionLocalService objectDefinitionLocalService;

	private final Log _log = LogFactoryUtil.getLog(LogoutPreAction.class);

	@Reference
	private ObjectEntryLocalService objectEntryLocalService;

}
