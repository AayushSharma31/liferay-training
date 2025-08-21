/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.ignek.employee.service.impl;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.base.EmployeeLocalServiceBaseImpl;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(property = "model.class.name=com.ignek.employee.model.Employee", service = AopService.class)
public class EmployeeLocalServiceImpl extends EmployeeLocalServiceBaseImpl {

	public Employee addEmployee(String employeeFirstName, String employeeLastName, String emailAddress,
			String addressLine1, String addressLine2, String phoneNumber, String zipCode, String designation,
			String city, ServiceContext serviceContext, String employeePassword) {
		try {
			if (isUserEmailAddressExists(emailAddress, serviceContext.getCompanyId())) {
				_log.debug("User Email already exists!!");
				return null;
			}
			User user = userLocalService.addUser(serviceContext.getUserId(), serviceContext.getCompanyId(), false,
					employeePassword, employeePassword, true, employeeFirstName, emailAddress, LocaleUtil.getDefault(),
					employeeFirstName, StringPool.BLANK, employeeLastName, 0l, 0l, true, 1, 1, 1, designation, 1, null,
					null, null, null, false, serviceContext);

			if (isEmployeeEmailAddressExists(emailAddress)) {
				_log.debug("Email already exists!!");
				return null;
			}
			long employeeId = counterLocalService.increment(Employee.class.getName());
			Employee employee = employeeLocalService.createEmployee(employeeId);

			employee.setAddressLine1(addressLine1);
			employee.setAddressLine2(addressLine2);
			employee.setCity(city);
			employee.setFirstName(employeeFirstName);
			employee.setLastName(employeeLastName);
			employee.setDesignation(designation);
			employee.setEmailAddress(emailAddress);
			employee.setZipCode(zipCode);
			employee.setPhoneNumber(phoneNumber);
			employee.setUserId(user.getUserId());
			employee.setGroupId(serviceContext.getScopeGroupId());
			employee.setCompanyId(serviceContext.getCompanyId());
			employee.setCreateDate(serviceContext.getCreateDate(new Date()));
			employee.setModifiedDate(serviceContext.getModifiedDate(new Date()));
			Employee savedEmployee = employeeLocalService.addEmployee(employee);
			_log.info("Employee added successfully");

			return savedEmployee;
		} catch (Exception e) {
			_log.error("Error creating employee", e);
			return null;
		}

	}

	public boolean isEmployeeEmailAddressExists(String emailAddress) {

		boolean isEmailAddressExists = false;
		try {
			employeePersistence.findByEmailAddress(emailAddress);
			_log.info("Employee with this mail already exists =============  " + emailAddress);
			isEmailAddressExists = true;
		} catch (Exception e) {
			isEmailAddressExists = false;
			_log.info("No user exists with this mail please proceede");
		}
		return isEmailAddressExists;
	}

	public boolean isUserEmailAddressExists(String emailAddress, long companyId) {

		boolean isUserEmailAddressExists = false;
		try {
			userLocalService.getUserByEmailAddress(companyId, emailAddress);
			isUserEmailAddressExists = true;
		} catch (PortalException e) {
			isUserEmailAddressExists = false;
			_log.error("something went wrong === " + e.getMessage());
		}
		return isUserEmailAddressExists;
	}

	public Employee getEmployeeById(long employeeId) {

		try {
			Employee employee = employeeLocalService.getEmployee(employeeId);
			return employee;

		} catch (PortalException e) {
			_log.error("unable to fetch the employee =======================  " + e.getMessage());
			;
			return null;
		}

	}

	public User getUserById(long userId) {

		try {
			User user = userLocalService.getUserById(userId);
			_log.info("Fetching user for update " + user);
			return user;

		} catch (PortalException e) {
			_log.error("Unable to fetch user ====================== " + e.getMessage());
			return null;
		}

	}

	public String deleteEmployeeById(long employeeId) {
		Employee employee = getEmployeeById(employeeId);
		_log.info("getting the employee entity to delete ============================== " + employee.getUserId());
		User user = getUserById(employee.getUserId());
		_log.info("getting the user  entity to delete ==================== " + user.getUserId());

		_log.debug(" inside the delete employee method========================================== ");

		if (employee == null && user == null) {
			_log.debug("No employee or user found with id error in deleting==================================== "
					+ employeeId);
			return "No employee or User exists with this id";
		} else {
			try {
				Employee employee2 = employeeLocalService.deleteEmployee(employee);
				_log.info(" ==============================    Employee deleted successfull " + employee2.getUserId());

				User user2 = userLocalService.deleteUser(user);
				_log.info(" ============================    User deleted successfull " + user2.getUserId());

				return "Employee deleted successfully";
			} catch (Exception e) {
				_log.error("Something went wrong in deleting user ===================== " + e.getMessage());
				return "Error in deleting user ";
			}

		}

	}

	public String updateEmployee(String employeeFirstName, String employeeLastName, String emailAddress,
			String addressLine1, String addressLine2, String phoneNumber, String zipCode, String designation,
			String city, long employeeId, ServiceContext serviceContext) {
		_log.info(" ===============  inside the update method");

		Employee employee = getEmployeeById(employeeId);
		User user = getUserById(employee.getUserId());
		user.setFirstName(employeeFirstName);
		user.setLastName(employeeLastName);
		user.setJobTitle(designation);
		user.setModifiedDate(serviceContext.getModifiedDate(new Date()));
		user.setEmailAddress(emailAddress);
		userLocalService.updateUser(user);
		_log.info("User updated successfully =======================");

		_log.info("=====================  inside the updatig user");

		employee.setAddressLine1(addressLine1);
		employee.setAddressLine2(addressLine2);
		employee.setCity(city);
		employee.setFirstName(employeeFirstName);
		employee.setLastName(employeeLastName);
		employee.setDesignation(designation);
		employee.setEmailAddress(emailAddress);
		employee.setZipCode(zipCode);
		employee.setPhoneNumber(phoneNumber);

		employee.setModifiedDate(serviceContext.getModifiedDate(new Date()));

		employeeLocalService.updateEmployee(employee);

		_log.info("Employee updated successfully ==");

		return "Emaployee updated successfully!!";

	}

	public List<Employee> getAllEmployess() {

		return employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
	}

	private static final Log _log = LogFactoryUtil.getLog(EmployeeLocalServiceImpl.class);

}