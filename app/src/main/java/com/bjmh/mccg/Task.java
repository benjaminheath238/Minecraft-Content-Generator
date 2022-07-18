package com.bjmh.mccg;

import java.io.FileWriter;
import java.io.IOException;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public class Task {
    private String name;
    private String forType;
    private String operationPath;
    private String fileType;
    private String[] template;

    public void run(ConfigSection section) {
        try (FileWriter writer = new FileWriter(Main.USER_DIR + "/assets/"
                + ((ConfigOption) Main.GLOBAL_CONFIG.getChild(Main.MODID)).getValue() + Main.FILE_SEPARATOR + operationPath
                + Main.FILE_SEPARATOR + section.getName() + "." + fileType)) {
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the forType
     */
    public String getForType() {
        return forType;
    }

    /**
     * @param forType the forType to set
     */
    public void setForType(String forType) {
        this.forType = forType;
    }

    /**
     * @return the operationPath
     */
    public String getOperationPath() {
        return operationPath;
    }

    /**
     * @param operationPath the operationPath to set
     */
    public void setOperationPath(String operationPath) {
        this.operationPath = operationPath;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the template
     */
    public String[] getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String[] template) {
        this.template = template;
    }
}
