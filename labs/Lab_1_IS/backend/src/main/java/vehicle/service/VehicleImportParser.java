package vehicle.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import vehicle.dto.CoordinatesDto;
import vehicle.dto.VehicleDto;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VehicleImportParser {

    public record ParsedRow(int index, VehicleDto dto) {}

    public List<ParsedRow> parse(InputStream stream) {
        List<String> errors = new ArrayList<>();
        List<ParsedRow> entries = new ArrayList<>();
        try (JsonReader reader = Json.createReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            JsonStructure root = reader.read();
            switch (root.getValueType()) {
                case ARRAY -> parseArray(root.asJsonArray(), entries, errors);
                case OBJECT -> parseItem(root.asJsonObject(), 1, entries, errors);
                default -> throw new ImportProcessingException(
                        "Некорректный формат файла",
                        List.of("Ожидался JSON-массив или объект"));
            }
        } catch (JsonException e) {
            throw new ImportProcessingException("Ошибка разбора JSON", List.of(e.getMessage()));
        }

        if (!errors.isEmpty()) {
            throw new ImportProcessingException("Ошибки при разборе файла", errors);
        }
        return entries;
    }

    private void parseArray(JsonArray array, List<ParsedRow> entries, List<String> errors) {
        for (int i = 0; i < array.size(); i++) {
            JsonValue value = array.get(i);
            int index = i + 1;
            if (value.getValueType() != JsonValue.ValueType.OBJECT) {
                errors.add("Запись " + index + ": ожидался JSON-объект");
                continue;
            }
            parseItem(value.asJsonObject(), index, entries, errors);
        }
    }

    private void parseItem(JsonObject obj, int index, List<ParsedRow> entries, List<String> errors) {
        VehicleDto dto = new VehicleDto();
        CoordinatesDto coordinatesDto = new CoordinatesDto();
        dto.coordinates = coordinatesDto;

        dto.name = requiredString(obj, "name", index, errors);
        dto.type = requiredString(obj, "type", index, errors);

        JsonObject coordinates = requiredObject(obj, "coordinates", index, errors);
        if (coordinates != null) {
            coordinatesDto.x = requiredDouble(coordinates, "x", index, errors);
            Float y = requiredFloat(coordinates, "y", index, errors);
            if (y != null) {
                coordinatesDto.y = y;
            }
        }

        dto.enginePower = requiredLong(obj, "enginePower", index, errors);
        dto.numberOfWheels = optionalLong(obj, "numberOfWheels", index, errors);
        dto.capacity = requiredFloat(obj, "capacity", index, errors);
        dto.distanceTravelled = requiredFloat(obj, "distanceTravelled", index, errors);
        dto.fuelConsumption = optionalLong(obj, "fuelConsumption", index, errors);
        dto.fuelType = optionalString(obj, "fuelType");

        entries.add(new ParsedRow(index, dto));
    }

    private JsonObject requiredObject(JsonObject obj, String field, int index, List<String> errors) {
        JsonValue value = obj.get(field);
        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            errors.add("Запись " + index + ": поле \"" + field + "\" обязательно");
            return null;
        }
        if (value.getValueType() != JsonValue.ValueType.OBJECT) {
            errors.add("Запись " + index + ": поле \"" + field + "\" должно быть объектом");
            return null;
        }
        return value.asJsonObject();
    }

    private String requiredString(JsonObject obj, String field, int index, List<String> errors) {
        String value = optionalString(obj, field);
        if (value == null || value.isBlank()) {
            errors.add("Запись " + index + ": поле \"" + field + "\" обязательно");
            return null;
        }
        return value;
    }

    private String optionalString(JsonObject obj, String field) {
        JsonValue value = obj.get(field);
        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return null;
        }
        if (value.getValueType() != JsonValue.ValueType.STRING) {
            return null;
        }
        String text = ((JsonString) value).getString().trim();
        return text.isEmpty() ? null : text;
    }

    private Long requiredLong(JsonObject obj, String field, int index, List<String> errors) {
        Long value = optionalLong(obj, field, index, errors);
        if (value == null) {
            errors.add("Запись " + index + ": поле \"" + field + "\" обязательно");
        }
        return value;
    }

    private Long optionalLong(JsonObject obj, String field, int index, List<String> errors) {
        JsonValue value = obj.get(field);
        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return null;
        }
        if (value.getValueType() != JsonValue.ValueType.NUMBER) {
            errors.add("Запись " + index + ": поле \"" + field + "\" должно быть числом");
            return null;
        }
        JsonNumber number = (JsonNumber) value;
        try {
            return number.longValueExact();
        } catch (ArithmeticException e) {
            errors.add("Запись " + index + ": поле \"" + field + "\" содержит некорректное целое число");
            return null;
        }
    }

    private Float requiredFloat(JsonObject obj, String field, int index, List<String> errors) {
        Float value = optionalFloat(obj, field, index, errors);
        if (value == null) {
            errors.add("Запись " + index + ": поле \"" + field + "\" обязательно");
        }
        return value;
    }

    private Float optionalFloat(JsonObject obj, String field, int index, List<String> errors) {
        JsonValue value = obj.get(field);
        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return null;
        }
        if (value.getValueType() != JsonValue.ValueType.NUMBER) {
            errors.add("Запись " + index + ": поле \"" + field + "\" должно быть числом");
            return null;
        }
        JsonNumber number = (JsonNumber) value;
        return (float) number.doubleValue();
    }

    private Double requiredDouble(JsonObject obj, String field, int index, List<String> errors) {
        Double value = optionalDouble(obj, field, index, errors);
        if (value == null) {
            errors.add("Запись " + index + ": поле \"" + field + "\" обязательно");
        }
        return value;
    }

    private Double optionalDouble(JsonObject obj, String field, int index, List<String> errors) {
        JsonValue value = obj.get(field);
        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return null;
        }
        if (value.getValueType() != JsonValue.ValueType.NUMBER) {
            errors.add("Запись " + index + ": поле \"" + field + "\" должно быть числом");
            return null;
        }
        JsonNumber number = (JsonNumber) value;
        return number.doubleValue();
    }
}

