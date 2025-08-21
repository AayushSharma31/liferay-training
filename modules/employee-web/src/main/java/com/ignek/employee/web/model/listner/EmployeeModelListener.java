package com.ignek.employee.web.model.listner;

import com.ignek.employee.model.Employee;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = ModelListener.class)
public class EmployeeModelListener extends BaseModelListener<Employee> {

	public Map<String, Serializable> getStringSerializableHashMap() {
		return new HashMap<>();
	}

	@Override
	public void onAfterCreate(Employee employeeModel) throws ModelListenerException {

		try {
			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

			ObjectDefinition activity = getObjectDefinition(serviceContext.getCompanyId());

			Map<String, Serializable> activityObjectValues = getStringSerializableHashMap();

			String iPAddress = serviceContext.getRemoteAddr();
			activityObjectValues.put("activityType", "New Employee added");
			activityObjectValues.put("details",
					"This is the message to show that new emplyee is created and its activity details are here");
			activityObjectValues.put("iPAddress", iPAddress);
			activityObjectValues.put("employeeId", employeeModel.getUserId());

			ObjectEntry activityEntry = objectEntryLocalService.addObjectEntry(serviceContext.getUserId(), 0,
					activity.getObjectDefinitionId(), activityObjectValues, serviceContext);
		} catch (Exception e) {
			_log.info("Something went wrong in creating new  user creation object entry " + e.getMessage());
		}

		super.onAfterCreate(employeeModel);

		_log.info("New employee created in model listner ");
	}

	@Override
	public void onAfterRemove(Employee model) throws ModelListenerException {
		try {
			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
			ObjectDefinition activity = getObjectDefinition(serviceContext.getCompanyId());

			Map<String, Serializable> activityObjectValues = getStringSerializableHashMap();

			String iPAddress = serviceContext.getRemoteAddr();
			activityObjectValues.put("activityType", "Employee deleted");
			activityObjectValues.put("details",
					"This is the message to show that emplyee is deleted and its activity details are here");
			activityObjectValues.put("iPAddress", iPAddress);
			activityObjectValues.put("employeeId", model.getUserId());

			ObjectEntry activityEntry = objectEntryLocalService.addObjectEntry(serviceContext.getUserId(), 0,
					activity.getObjectDefinitionId(), activityObjectValues, serviceContext);
		} catch (Exception e) {
			_log.info("Something went wrong in creating delete user object entry " + e.getMessage());
		}

		_log.info("employee deleted in model listner");

		super.onAfterRemove(model);
	}

	@Override
	public void onAfterUpdate(Employee originalEmployeeModel, Employee updatedEmployeeModel)
			throws ModelListenerException {

		_log.info("exited employee updated in model listner");

		try {
			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
			_log.info("printing the serviceContext =================  " + serviceContext);
			ObjectDefinition activity = getObjectDefinition(serviceContext.getCompanyId());

			Map<String, Serializable> activityObjectValues = getStringSerializableHashMap();

			String iPAddress = serviceContext.getRemoteAddr();
			activityObjectValues.put("activityType", "Employee updated");
			activityObjectValues.put("details",
					"This is the message to show that emplyee is updated and its activity details are here");
			activityObjectValues.put("iPAddress", iPAddress);
			activityObjectValues.put("employeeId", updatedEmployeeModel.getUserId());

			ObjectEntry activityEntry = objectEntryLocalService.addObjectEntry(serviceContext.getUserId(), 0,
					activity.getObjectDefinitionId(), activityObjectValues, serviceContext);
		} catch (Exception e) {
			_log.info("Something went wrong in creating update user object entry " + e.getMessage());
		}

		super.onAfterUpdate(originalEmployeeModel, updatedEmployeeModel);
	}

	private ObjectDefinition getObjectDefinition(long companyId) {
		ObjectDefinition objectDefinition = null;

		try {
			objectDefinition = objectDefinitionLocalService.getObjectDefinitionByExternalReferenceCode("activity",
					companyId);

			return objectDefinition;
		} catch (Exception e) {
			_log.info("somethig went wrong in getting object definition " + e.getMessage());

			return objectDefinition;
		}
	}

	@Reference
	private ObjectDefinitionLocalService objectDefinitionLocalService;

	private final Log _log = LogFactoryUtil.getLog(EmployeeModelListener.class);

	@Reference
	private ObjectEntryLocalService objectEntryLocalService;

}