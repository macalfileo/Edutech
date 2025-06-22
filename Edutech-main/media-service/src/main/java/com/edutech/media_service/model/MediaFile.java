package com.edutech.media_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "archivos_multimedia")
@Data
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del archivo no puede estar vacío")
    @Size(max = 255, message = "El nombre del archivo es demasiado largo")
    @Column(name = "nombre_archivo", nullable = false, length = 255 )
    private String nombreArchivo;

    @NotBlank(message = "El tipo de archivo no puede estar vacío")
    @Size(max = 50, message = "El tipo de archivo es demasiado largo")
    @Column(name = "tipo_archivo", nullable = false, length = 50)
    private String tipoArchivo;

    @NotNull(message = "El contenido no puede ser nulo")
    @Lob
    @Column(name = "contenido", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] contenido;

    public void setFilename(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFilename'");
    }

    public void setType(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setType'");
    }

    public String getFilename() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFilename'");
    }

    public String getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

    public Object getUsuarioId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsuarioId'");
    }

    public Object getCursoId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCursoId'");
    }
 

}