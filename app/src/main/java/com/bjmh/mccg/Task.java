package com.bjmh.mccg;

import java.io.FileWriter;
import java.io.IOException;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public class Task {
    private String name;
    private String type;
    private String path;
    private String[] template;

    /**
     * @param name
     * @param type
     * @param path
     * @param template
     */
    public Task(String name, String type, String path, String[] template) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.template = template;
    }

    public void run(ConfigSection section) {
        try (FileWriter writer = new FileWriter(Main.USER_DIR + "/assets/"
                + ((ConfigOption) Main.GLOBAL_CONFIG.getChild(Main.MODID)).getValue() + Main.FILE_SEPARATOR
                + path.replace("${name}", section.getName())
                        .replace("${modid}", ((ConfigOption) Main.GLOBAL_CONFIG.getChild(Main.MODID)).getValue())
                        .replace("${path}", ((ConfigOption) section.getChild(Main.PATH)).getValue())
                        .replace("${type}", ((ConfigOption) section.getChild(Main.TYPE)).getValue()))) {
            for (String line : template) {
                writer.write(line.replace("${name}", section.getName())
                        .replace("${modid}", ((ConfigOption) Main.GLOBAL_CONFIG.getChild(Main.MODID)).getValue())
                        .replace("${path}", ((ConfigOption) section.getChild(Main.PATH)).getValue())
                        .replace("${type}", ((ConfigOption) section.getChild(Main.TYPE)).getValue()));
                writer.write(10);
            }
        } catch (IOException e) {
            System.out.println("An error occured during task: " + name + ", on: " + section.getName());
            e.printStackTrace();
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the template
     */
    public String[] getTemplate() {
        return template;
    }
}
