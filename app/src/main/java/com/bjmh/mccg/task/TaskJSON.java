package com.bjmh.mccg.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public abstract class TaskJSON extends Task {
    protected Map<String, String> variables;

    @Override
    public void run(ConfigSection section) {
        variables = new HashMap<>();

        for (ConfigNode node : section.getChildren()) {
            if (node instanceof ConfigOption)
                variables.put(node.getName(), ((ConfigOption) node).getValue());
        }

        variables.put("modid", modid);

        super.run(section);
    }

    public List<String> loadJson(String path) {
        List<String> json = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            while (reader.ready()) {
                json.add(reader.readLine());
            }
        } catch (IOException e) {
            System.err.println("An exception occurred while loading template from: " + path);
            e.printStackTrace();
        }
        return json;
    }

    public void saveJson(List<String> json, String path) {
        try (FileWriter writer = new FileWriter(new File(path))) {
            for (String line : json) {
                writer.write(line);
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("An exception occurred while saving to: " + path);
            e.printStackTrace();
        }
    }

    public void updateJsonVariables(List<String> json, Map<String, String> variables) {
        for (int i = 0; i < json.size(); i++) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                json.set(i, json.get(i).replace("${" + entry.getKey() + "}", entry.getValue()));
            }
        }
    }

    public void appendToJson(String path, String data) {
        List<String> json = loadJson(path);
        if (!json.get(json.size() - 2).equals("{")) json.set(json.size() - 2, json.get(json.size() - 2) + ",");
        json.set(json.size() - 1, data);
        json.add("}");
        saveJson(json, path);
    }

    public void createJson(String path) {
        saveJson(java.util.Arrays.asList("{", "}"), path);
    }

    public void createJsonIfAbsent(String path) {
        if (new File(path).exists()) return;
        createJson(path);
    }
}
