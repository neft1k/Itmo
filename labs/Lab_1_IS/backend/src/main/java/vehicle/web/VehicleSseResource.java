package vehicle.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import vehicle.events.VehicleChangedEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Path("/stream/vehicles")
@ApplicationScoped
public class VehicleSseResource {
    @Context
    Sse sse;
    private final Set<SseEventSink> sinks = ConcurrentHashMap.newKeySet();

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void stream(@Context SseEventSink sink) {
        sinks.add(sink);
        sink.send(sse.newEventBuilder().name("init").data("ok").build());
    }

    public void onChanged(@Observes VehicleChangedEvent evt) {
        var event = sse.newEventBuilder().name("vehicle").data(evt.type() + ":" + evt.id()).build();
        sinks.removeIf(SseEventSink::isClosed);
        sinks.forEach(s -> {
            try {
                s.send(event);
            } catch (IllegalStateException ignored) {}
        });
    }
}
