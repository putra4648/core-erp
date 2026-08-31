package id.putra.core_erp.purchase_requisition.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtil {
    private AppUtil() {
    }

    public static String generatedIdFromZeros(String key, Long number) {
        StringBuilder builder = new StringBuilder();

        builder.append(key).append("-");
        builder.append(new SimpleDateFormat("yyyy").format(new Date())).append("-");
        builder.append(String.format("%1$" + 6 + "s", number).replace(' ', '0'));

        return builder.toString();
    }

}
