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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "key=login.events.pre" }, service = LifecycleAction.class)
public class LoginPreAction implements LifecycleAction {

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		long loggedInUserId = 0;
		try {
			HttpServletRequest httpServletRequest = lifecycleEvent.getRequest();
			loggedInUserId = PortalUtil.getUser(httpServletRequest).getUserId();
			_log.info("printing the user id ========" + loggedInUserId);
			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
			_log.info("getting current user from service  context object   "+ serviceContext.getRequest().getAuthType());
			String iPAddress = serviceContext.getRemoteAddr();
			ObjectDefinition activity = objectDefinitionLocalService
					.getObjectDefinitionByExternalReferenceCode("activity", serviceContext.getCompanyId());

			Map<String, Serializable> activityObjectValues = getStringSerializableHashMap();

			activityObjectValues.put("activityType", "Employee LoggedIn");
			activityObjectValues.put("details",
					"This is the message to show that emplyee is loggedin and its activity details are here");
			activityObjectValues.put("iPAddress", iPAddress);
			activityObjectValues.put("employeeId", loggedInUserId);
			User user = userLocalService.getUser(loggedInUserId);

			PermissionChecker checker = PermissionCheckerFactoryUtil.create(user);
			PermissionThreadLocal.setPermissionChecker(checker);

			ObjectEntry activityEntry = objectEntryLocalService.addObjectEntry(serviceContext.getUserId(), 0,
					activity.getObjectDefinitionId(), activityObjectValues, serviceContext);

			_log.info("User successfully logged in with user id as " + loggedInUserId);

		} catch (Exception e) {
			_log.info("Something went wrong in prelogin event " + e.getMessage());
		}

	}

	public Map<String, Serializable> getStringSerializableHashMap() {
		return new HashMap<>();
	}

	@Reference
	private ObjectDefinitionLocalService objectDefinitionLocalService;

	@Reference
	private UserLocalService userLocalService;

	private final Log _log = LogFactoryUtil.getLog(LoginPreAction.class);

	@Reference
	private ObjectEntryLocalService objectEntryLocalService;

}
