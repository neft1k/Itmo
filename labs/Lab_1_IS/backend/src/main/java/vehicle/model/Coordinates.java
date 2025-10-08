package vehicle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Double x;

    @Column(nullable = false, columnDefinition = "real CHECK (y <= 751)")
    private float y;

    public Long getId() { return id; }
    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}
