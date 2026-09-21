package id.putra.core_erp.shared.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.modules.procurement.model.AppFile;

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

    public static Path createFileFromFileUpload(String fileId, String folder, FileUpload file)
            throws IOException {
        Path path = Paths.get(serverPath, "data", folder);
        path.toFile().mkdirs();

        String newFilename = file.fileName().concat("_" + fileId).concat(".")
                .concat(file.fileName().substring(file.fileName().lastIndexOf(".") + 1));
        path = Paths.get(path.toString(), newFilename);
        if (!Files.exists(path)) {
            path = Files.write(path, Files.readAllBytes(file.filePath()));
        }

        return path;
    }

    public static Path createFileFromBase64(String fileId, String folder, AppFile file)
            throws IOException {
        Path path = Paths.get(serverPath, "data", folder);
        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }
        path = Paths.get(path.toString(), file.getName());
        if (!Files.exists(path)) {
            path = Files.write(path,
                    Base64.getDecoder().decode(file.getContent().substring(file.getContent().indexOf(",") + 1)));
        }

        return path;
    }

    public static Path recreateFileFromFileUpload(String fileId, FileUpload file, String destinationFolder,
            String currentPathToDelete)
            throws IOException {
        Path path = null;
        path = Paths.get(currentPathToDelete);
        File f = path.toFile();
        if (f.exists()) {
            Files.delete(path);
        } else {
            path = createFileFromFileUpload(fileId, destinationFolder, file);
        }
        return path;
    }

    public static Path recreateFileFromBase64(String fileId, AppFile file, String destinationFolder,
            String currentPathToDelete)
            throws IOException {
        Path path = null;
        path = Paths.get(currentPathToDelete);
        File f = path.toFile();
        if (f.exists()) {
            Files.delete(path);
        } else {
            path = createFileFromBase64(fileId, destinationFolder, file);
        }
        return path;
    }

    public static String readFileToBase64(String filePath) throws IOException {
        StringBuilder builder = new StringBuilder();

        Path path = Paths.get(filePath);

        if (Files.notExists(path)) {
            return null;
        } else {

            builder.append("data:");
            builder.append(Files.probeContentType(path)).append(";base64,");
            builder.append(Base64.getEncoder().encodeToString(Files.readAllBytes(path)));

            return builder.toString();
        }

    }

    public static String readFileToBase64(String filename, byte[] data) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("data:");
        builder.append(Files.probeContentType(new File(filename).toPath())).append(";base64,");
        builder.append(Base64.getEncoder().encodeToString(data));

        return builder.toString();

    }

}