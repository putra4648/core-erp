package id.putra.core_erp.service;

import com.vaadin.flow.server.streams.DownloadHandler;

import id.putra.core_erp.exceptions.DownloadException;

public interface ReportService {
	<T> DownloadHandler generateReport(T entity, String type) throws DownloadException;
}
