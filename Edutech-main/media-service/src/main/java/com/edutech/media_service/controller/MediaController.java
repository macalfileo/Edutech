package com.edutech.media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.service.MediaService;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// OpenAPI/Swagger annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;


@RestController
@RequestMapping("/api/archivos")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService){
        this.mediaService = mediaService;

    }

    @Operation(summary = "Subir un archivo", description = "Permite subir un archivo al sistema.")
    @ApiResponse(responseCode = "200", description = "Archivo subido exitosamente", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @ApiResponse(responseCode = "400", description = "Error en el archivo proporcionado", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/subir")
    public ResponseEntity<MediaFile> subir(@RequestParam("archivo")MultipartFile archivo) throws IOException {
        MediaFile guardado = mediaService.guardarArchivo(archivo);
        return ResponseEntity.ok(guardado);
    
    }

    @Operation(summary = "Obtener todos los archivos", description = "Lista todos los archivos almacenados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de archivos obtenida exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MediaFile.class))))
    @GetMapping
    public List<MediaFile> listar() {
        return mediaService.listarArchivos();
    }

     // PUT: Actualizar archivo por ID
     @Operation(summary = "Actualizar archivo por ID", description = "Permite actualizar un archivo existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Archivo actualizado exitosamente", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
     @PutMapping("/{id}")
    public ResponseEntity<MediaFile> actualizar(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) throws IOException {

        MediaFile actualizado = mediaService.actualizarArchivo(id, archivo);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE: Eliminar archivo por ID
    @Operation(summary = "Eliminar archivo por ID", description = "Permite eliminar un archivo por su ID.")
    @ApiResponse(responseCode = "204", description = "Archivo eliminado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = mediaService.eliminarArchivo(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
