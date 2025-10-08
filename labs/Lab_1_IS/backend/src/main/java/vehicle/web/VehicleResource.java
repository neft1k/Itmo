package vehicle.web;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import vehicle.dto.VehicleDto;
import vehicle.mapper.VehicleMapper;
import vehicle.model.Vehicle;
import vehicle.repo.VehicleRepository;
import vehicle.service.VehicleService;

import java.util.HashMap;
import java.util.Map;

@Path("/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleResource {

    @Inject VehicleService service;

    @GET
    public VehicleRepository.Page<VehicleDto> list(@QueryParam("page") Integer page,
                                                   @QueryParam("size") Integer size,
                                                   @QueryParam("sort") @DefaultValue("id") String sort,
                                                   @QueryParam("dir") @DefaultValue("asc") String dir,
                                                   @QueryParam("name") String name,
                                                   @QueryParam("type") String type,
                                                   @QueryParam("fuelType") String fuelType) {
        boolean asc = !"desc".equalsIgnoreCase(dir);
        Map<String,String> filters = new HashMap<>();
        if (name != null) filters.put("name", name);
        if (type != null) filters.put("type", type);
        if (fuelType != null) filters.put("fuelType", fuelType);

        var p = service.list(page == null ? 0 : page, size == null ? 20 : size, sort, asc, filters);
        return new VehicleRepository.Page<>(p.content.stream().map(VehicleMapper::toDto).toList(), p.page, p.size, p.total);
    }

    @GET
    @Path("/{id}")
    public VehicleDto get(@PathParam("id") Long id) {
        Vehicle v = service.get(id);
        return VehicleMapper.toDto(v);
    }

    @POST
    public VehicleDto create(@Valid VehicleDto dto) {
        return VehicleMapper.toDto(service.create(dto));
    }

    @PUT
    @Path("/{id}")
    public VehicleDto update(@PathParam("id") Long id, @Valid VehicleDto dto) {
        return VehicleMapper.toDto(service.update(id, dto));
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) { service.delete(id); }
}
