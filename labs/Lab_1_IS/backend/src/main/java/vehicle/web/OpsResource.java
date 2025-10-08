package vehicle.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vehicle.dto.VehicleDto;
import vehicle.mapper.VehicleMapper;
import vehicle.service.VehicleService;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/ops")
@Produces(MediaType.APPLICATION_JSON)
public class OpsResource {

    @Inject VehicleService service;

    @GET
    @Path("/avg-fuel")
    public Double avgFuel() { return service.avgFuelConsumption(); }

    private static final Logger LOG = Logger.getLogger(OpsResource.class.getName());

    @GET
    @Path("/any-with-max-type")
    @Produces(MediaType.APPLICATION_JSON)
    public VehicleDto anyWithMaxType() {
        try {
            var v = service.anyWithMaxType();
            if (v == null) {
                return null;
            }

            var dto = VehicleMapper.toDto(v);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path("/name-contains")
    public List<?> nameContains(@QueryParam("q") String q) {
        return service.findByNameContains(q).stream().map(VehicleMapper::toDto).toList();
    }

    @GET
    @Path("/wheels-range")
    public List<?> wheelsRange(@QueryParam("from") long from, @QueryParam("to") long to) {
        return service.findByWheelsRange(from, to).stream().map(VehicleMapper::toDto).toList();
    }

    @POST
    @Path("/reset-distance/{id}")
    public void reset(@PathParam("id") Long id) { service.resetDistanceToZero(id); }
}
