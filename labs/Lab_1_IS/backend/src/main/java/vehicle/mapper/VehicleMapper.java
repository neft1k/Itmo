package vehicle.mapper;

import vehicle.dto.*;
import vehicle.model.*;

public class VehicleMapper {

    public static VehicleDto toDto(Vehicle v) {
        if (v == null) return null;
        VehicleDto d = new VehicleDto();
        d.id = v.getId();
        d.name = v.getName();
        if (v.getCoordinates() != null) {
            d.coordinates = new CoordinatesDto();
            d.coordinates.id = v.getCoordinates().getId();
            d.coordinates.x = v.getCoordinates().getX();
            d.coordinates.y = v.getCoordinates().getY();
        }
        d.creationDate = v.getCreationDate() != null ? v.getCreationDate().toString() : null;
        d.type = v.getType() != null ? v.getType().name() : null;
        d.enginePower = v.getEnginePower();
        d.numberOfWheels = v.getNumberOfWheels();
        d.capacity = v.getCapacity();
        d.distanceTravelled = v.getDistanceTravelled();
        d.fuelConsumption = v.getFuelConsumption();
        d.fuelType = v.getFuelType() != null ? v.getFuelType().name() : null;
        return d;
    }

    public static void apply(Vehicle v, VehicleDto d) {
        if (d.name != null)
            v.setName(d.name.trim());
        if (d.type != null)
            v.setType(VehicleType.valueOf(d.type));
        if (d.enginePower != null)
            v.setEnginePower(d.enginePower);
        if (d.numberOfWheels != null)
            v.setNumberOfWheels(d.numberOfWheels);
        if (d.capacity != null)
            v.setCapacity(d.capacity);
        if (d.distanceTravelled != null)
            v.setDistanceTravelled(d.distanceTravelled);
        if (d.fuelConsumption != null)
            v.setFuelConsumption(d.fuelConsumption);
        if (d.fuelType != null)
            v.setFuelType(FuelType.valueOf(d.fuelType));
        if (d.coordinates != null) {
            Coordinates c = v.getCoordinates();
            if (c == null) {
                c = new Coordinates();
                v.setCoordinates(c);
            }
            if (d.coordinates.id != null) {
                c.setX(null);
                c.setY(0);
            }
            if (d.coordinates.x != null)
                c.setX(d.coordinates.x);
            if (d.coordinates.y != null)
                c.setY(d.coordinates.y);
        }
    }
}
