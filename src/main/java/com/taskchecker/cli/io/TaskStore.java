package com.taskchecker.cli.io;
import com.taskchecker.cli.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskStore {
    private final Path file = Path.of("task.jason");
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Task> loadAll(){
        try{
            if(!Files.exists(file)){
                return new ArrayList<>();
            }
            String json = Files.readString(file,StandardCharsets.UTF_8);
            if(json.isBlank()) return new ArrayList<>();
            return mapper.readValue(json, new TypeReference<List<Task>>() {});
        }
        catch (Exception e){
            System.out.println("Error: tasks.json is invalid. Please fix or delete it.");
            return new ArrayList<>();
        }
    }
    public void saveAll(List<Task> tasks){
        try{
            String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks);
            Files.writeString(file,out,StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("error: could not write tasks.json ("+ e.getMessage() +")");
        }
    }
}
