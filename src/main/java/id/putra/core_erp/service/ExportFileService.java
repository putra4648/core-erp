package id.putra.core_erp.service;

import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface ExportFileService {
	public void exportFile(FileUpload file) throws Exception;
}
