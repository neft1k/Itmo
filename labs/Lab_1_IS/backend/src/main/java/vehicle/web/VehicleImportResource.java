package vehicle.web;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import vehicle.dto.ImportJobDto;
import vehicle.mapper.ImportJobMapper;
import vehicle.model.ImportJob;
import vehicle.service.ImportProcessingException;
import vehicle.service.VehicleImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.ws.rs.core.MultivaluedMap;

@Path("/vehicles/import")
@Produces(MediaType.APPLICATION_JSON)
public class VehicleImportResource {

    @Inject
    VehicleImportService importService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importVehicles(MultipartFormDataInput input) {
        InputPart filePart = extractFilePart(input);
        String fileName = resolveFileName(filePart);
        try (InputStream stream = filePart.getBody(InputStream.class, null)) {
            ImportJob job = importService.importVehicles(stream, fileName);
            ImportJobDto dto = ImportJobMapper.toDto(job);
            return Response.status(Response.Status.CREATED).entity(dto).build();
        } catch (ImportProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorPayload(e.getMessage(), e.getErrors()))
                    .build();
        } catch (ConstraintViolationException e) {
            List<String> violations = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorPayload("Ошибка валидации импортируемых данных", violations))
                    .build();
        } catch (IOException e) {
            throw new BadRequestException("Не удалось прочитать файл: " + e.getMessage(), e);
        }
    }

    @GET
    @Path("/history")
    public List<ImportJobDto> history() {
        return importService.history().stream()
                .map(ImportJobMapper::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public ImportJobDto get(@PathParam("id") Long id) {
        return ImportJobMapper.toDto(importService.getJob(id));
    }

    private InputPart extractFilePart(MultipartFormDataInput input) {
        if (input == null) {
            throw new BadRequestException("Форма не содержит данных");
        }
        Map<String, List<InputPart>> parts = input.getFormDataMap();
        List<InputPart> fileParts = parts != null ? parts.get("file") : null;
        if (fileParts == null || fileParts.isEmpty()) {
            throw new BadRequestException("Файл не найден в форме (ожидается параметр \"file\")");
        }
        return fileParts.get(0);
    }

    private Map<String, Object> errorPayload(String message, List<String> details) {
        return Map.of("message", message, "details", details);
    }

    private String resolveFileName(InputPart part) {
        MultivaluedMap<String, String> headers = part.getHeaders();
        if (headers == null) return null;
        String disposition = headers.getFirst("Content-Disposition");
        if (disposition == null) return null;
        String[] pieces = disposition.split(";");
        for (String piece : pieces) {
            String trimmed = piece.trim();
            if (trimmed.startsWith("filename=")) {
                String name = trimmed.substring("filename=".length()).trim();
                if (name.startsWith("\"") && name.endsWith("\"")) {
                    name = name.substring(1, name.length() - 1);
                }
                return name;
            }
        }
        return null;
    }
}
