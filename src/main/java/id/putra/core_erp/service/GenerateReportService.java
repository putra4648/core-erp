package id.putra.core_erp.service;

import java.io.ByteArrayOutputStream;

import id.putra.core_erp.exceptions.ReportException;

public interface GenerateReportService<T> {

	ByteArrayOutputStream createPDFReport(T data) throws ReportException;

	ByteArrayOutputStream createExcelReport(T data) throws ReportException;

	ByteArrayOutputStream createWordReport(T data) throws ReportException;

}
