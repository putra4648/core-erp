package id.putra.core_erp.service;

import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface ImportFIleService {
	public void importFile(FileUpload file) throws Exception;
}
