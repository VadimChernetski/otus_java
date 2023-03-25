package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private ObjectMapper objectMapper;
    private JavaType measurementListType;


    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        initObjectMapper();
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> result;
        try(var resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
            var bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
          var resultAsString = new StringBuilder();
          bufferedReader.lines().forEach(resultAsString::append);
          result = objectMapper.readValue(resultAsString.toString(), measurementListType);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
        return result;
    }

    private void initObjectMapper() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        measurementListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Measurement.class);
        module.addDeserializer(Measurement.class, new MeasurementDeserializer());
        objectMapper.registerModule(module);
    }

}
