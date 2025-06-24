package com.edutech.report_service.util;

import com.edutech.report_service.model.Reporte;
import java.io.ByteArrayOutputStream;

public class GeneradorPDF {
    public static byte[] generarPDF(Reporte reporte) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Aquí irá el contenido real del PDF (OpenPDF/iText)
        return out.toByteArray();
    }
}