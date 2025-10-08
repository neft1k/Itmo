package vehicle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text CHECK (char_length(trim(name)) > 0)")
    private String name;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinates_id", nullable = false, foreignKey = @ForeignKey(name="fk_vehicle_coordinates"))
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private ZonedDateTime creationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @Positive
    @Column(name = "engine_power", nullable = false, columnDefinition = "bigint CHECK (engine_power > 0)")
    private long enginePower;

    @Column(name = "number_of_wheels", columnDefinition = "bigint CHECK (number_of_wheels IS NULL OR number_of_wheels > 0)")
    private Long numberOfWheels;

    @Positive
    @Column(nullable = false, columnDefinition = "real CHECK (capacity > 0)")
    private float capacity;

    @NotNull
    @Column(name = "distance_travelled", nullable = false, columnDefinition = "real CHECK (distance_travelled > 0)")
    private Float distanceTravelled;

    @Column(name = "fuel_consumption", columnDefinition = "bigint CHECK (fuel_consumption IS NULL OR fuel_consumption > 0)")
    private Long fuelConsumption;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) creationDate = ZonedDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public ZonedDateTime getCreationDate() { return creationDate; }
    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }
    public long getEnginePower() { return enginePower; }
    public void setEnginePower(long enginePower) { this.enginePower = enginePower; }
    public Long getNumberOfWheels() { return numberOfWheels; }
    public void setNumberOfWheels(Long numberOfWheels) { this.numberOfWheels = numberOfWheels; }
    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }
    public Float getDistanceTravelled() { return distanceTravelled; }
    public void setDistanceTravelled(Float distanceTravelled) { this.distanceTravelled = distanceTravelled; }
    public Long getFuelConsumption() { return fuelConsumption; }
    public void setFuelConsumption(Long fuelConsumption) { this.fuelConsumption = fuelConsumption; }
    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
}
