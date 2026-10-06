package id.putra.core_erp.util;

public class AppFileUtil {

	private static String serverPath;

	public static final String SUPPLIER_FOLDER = "supplier";

	// @Value("${app.server.path}")
	public void setServerPath(String path) {
		AppFileUtil.serverPath = path;
	}

	public static String getServerPath() {
		return serverPath;
	}

}