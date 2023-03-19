package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.model.Measurement;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JavaType measurementListType;

    {
        SimpleModule module = new SimpleModule();
        measurementListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Measurement.class);
        module.addDeserializer(Measurement.class, new MeasurementDeserializer());
        objectMapper.registerModule(module);
    }

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> result;
        try {
            final URL resource = ResourcesFileLoader.class.getClassLoader().getResource(fileName);
            if (resource != null) {
                var file = Files.readString(Path.of(resource.getFile()));
                result = objectMapper.readValue(file, measurementListType);
            } else {
                result = Collections.emptyList();
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse input data from json file");
        }
        //читает файл, парсит и возвращает результат
        return result;
    }

}
