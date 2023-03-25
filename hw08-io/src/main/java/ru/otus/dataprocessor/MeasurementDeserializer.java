package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.otus.model.Measurement;

import java.io.IOException;

public class MeasurementDeserializer extends StdDeserializer<Measurement> {

    public static final String NAME_FIELD = "name";
    public static final String VALUE_FIELD = "value";

    public MeasurementDeserializer() {
        this(null);
    }

    protected MeasurementDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Measurement deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = context.readTree(parser);

        final String name = (node.has(NAME_FIELD)) ? node.get(NAME_FIELD).asText() : null;
        final double value = (node.has(VALUE_FIELD)) ? node.get(VALUE_FIELD).asDouble() : -1000.0;

        return new Measurement(name, value);
    }
}
