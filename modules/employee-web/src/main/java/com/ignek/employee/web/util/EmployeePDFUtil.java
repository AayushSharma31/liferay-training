package com.ignek.employee.web.util;

import com.ignek.employee.model.Employee;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class EmployeePDFUtil {

	public static byte[] generateEmployeePDF(Employee employee, Locale locale) throws Exception {
		try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
			contentStream.setLeading(20f);
			contentStream.newLineAtOffset(50, 750);

			String title = LanguageUtil.get(locale, "employee-details");

			contentStream.showText(title);
			contentStream.newLine();
			contentStream.newLine();
			contentStream.setFont(PDType1Font.HELVETICA, 12);

			contentStream.showText("First Name: " + employee.getFirstName());
			contentStream.newLine();
			contentStream.showText("Last Name: " + employee.getLastName());
			contentStream.newLine();
			contentStream.showText("Email: " + employee.getEmailAddress());
			contentStream.newLine();
			contentStream.showText("Phone: " + employee.getPhoneNumber());
			contentStream.newLine();
			contentStream.showText("Designation: " + employee.getDesignation());
			contentStream.newLine();
			contentStream.showText("City: " + employee.getCity());
			contentStream.newLine();

			contentStream.endText();
			contentStream.close();

			document.save(out);
			return out.toByteArray();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(EmployeePDFUtil.class);

}
