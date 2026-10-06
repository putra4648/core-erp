package id.putra.core_erp.service;

import java.io.ByteArrayOutputStream;

import id.putra.core_erp.exceptions.ReportException;

public interface GenerateReportService<T> {

<<<<<<< HEAD
	ByteArrayOutputStream createExcelReport(T data) throws ReportException;

	ByteArrayOutputStream createPDFReport(T data) throws ReportException;
=======
	ByteArrayOutputStream createPDFReport(T data) throws ReportException;

	ByteArrayOutputStream createExcelReport(T data) throws ReportException;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	ByteArrayOutputStream createWordReport(T data) throws ReportException;

}
