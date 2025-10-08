package vehicle.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class VehicleEvents {
    @Inject
    Event<VehicleChangedEvent> event;
    public void fireChanged(String type, Long id) {
        event.fire(new VehicleChangedEvent(type, id));
    }
}
