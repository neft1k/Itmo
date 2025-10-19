package vehicle.mapper;

import vehicle.dto.ImportJobDto;
import vehicle.model.ImportJob;

public class ImportJobMapper {
    public static ImportJobDto toDto(ImportJob job) {
        if (job == null) {
            return null;
        }
        ImportJobDto dto = new ImportJobDto();
        dto.id = job.getId();
        dto.status = job.getStatus() != null ? job.getStatus().name() : null;
        dto.requestedBy = job.getRequestedBy();
        dto.requestedRole = job.getRequestedRole();
        dto.fileName = job.getFileName();
        dto.startedAt = job.getStartedAt() != null ? job.getStartedAt().toString() : null;
        dto.finishedAt = job.getFinishedAt() != null ? job.getFinishedAt().toString() : null;
        dto.createdCount = job.getCreatedCount();
        dto.errorMessage = job.getErrorMessage();
        return dto;
    }
}

