package com.bjmh.mccg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.lib.io.config.Configuration;
import com.bjmh.lib.io.config.ParserMethod;
import com.bjmh.lib.io.config.ParserMethods;
import com.bjmh.mccg.task.Task;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class Main {
    private static final String PATH_KEY = "path";
    private static final String MODID_KEY = "modid";

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final Configuration GLOBAL_CONFIG = new Configuration("global");
    public static final Configuration CONTENT_CONFIG = new Configuration("content");
    public static final Map<String, Map<String, Task>> TASKS = new HashMap<>();
    public static final Scanner SCANNNER = new Scanner(System.in);

    public static void main(String[] args) {
        redirectExceptionStream();
        extractResources();

        parseGlobalConfig();
        checkGlobalVars();
        parseContentConfig();

        loadTasks();

        runOnStartTasks();
        runOnEachTasks();
        runOnEndTasks();

        SCANNNER.close();
    }

    private static void parseContentConfig() {
        System.err.println("Parsing content config.");
        CONTENT_CONFIG.parse(GLOBAL_CONFIG.getChildValue(PATH_KEY),
                new ParserMethod() {
                    private ConfigSection current = null;

                    @Override
                    public void parse(String line, Configuration config) {
                        line = ParserMethods.removeComments(line);

                        if (line.isEmpty())
                            return;

                        if (ParserMethods.isSubHeader(line)) {
                            current = ParserMethods.inheritOptions(ParserMethods.parseSubHeader(line, config));
                        } else if (ParserMethods.isHeader(line)) {
                            current = ParserMethods.parseHeader(line, config);
                        } else if (ParserMethods.isComplexOption(line)) {
                            ConfigSection section = ParserMethods.inheritOptions(
                                    ParserMethods.parseComplexOption(line,
                                            ParserMethods.firstNonNull(current, config)));

                            section.addChild(
                                    new ConfigOption(section, "id", ConfigNode.Type.SIMPLE_OPTION, section.getName()));

                            ParserMethods.firstNonNull(current, config)
                                    .addChild(section);
                        } else if (ParserMethods.isNullMapOrArray(line)) {
                            // Catch the case of an empty array or map
                        } else if (ParserMethods.isMap(line)) {
                            ParserMethods.firstNonNull(current, config)
                                    .addChild(ParserMethods.inheritOptions(
                                            ParserMethods.parseMap(line, ParserMethods.firstNonNull(current, config))));
                        } else if (ParserMethods.isArray(line)) {
                            ParserMethods.firstNonNull(current, config)
                                    .addChild(ParserMethods.inheritOptions(
                                            ParserMethods.parseArray(line,
                                                    ParserMethods.firstNonNull(current, config))));
                        } else {
                            ParserMethods.firstNonNull(current, config)
                                    .addChild(
                                            ParserMethods.parseSimpleOption(line,
                                                    ParserMethods.firstNonNull(current, config)));
                        }
                    }
                });
        System.err.println("Content config parsed.");
        }

    private static void checkGlobalVars() {
        if (GLOBAL_CONFIG.getChild(MODID_KEY) == null) {
            System.err.println("No modid variable was found. Requesting user input.");
            System.out.println("Please enter mod ID.");
            GLOBAL_CONFIG.addChild(
                    new ConfigOption(GLOBAL_CONFIG, MODID_KEY, ConfigNode.Type.SIMPLE_OPTION, SCANNNER.nextLine()));
        }

        if (GLOBAL_CONFIG.getChild(PATH_KEY) == null) {
            System.err.println("No path variable was found. Requesting user input.");
            System.out.println("Please enter content config file path.");
            GLOBAL_CONFIG.addChild(
                    new ConfigOption(GLOBAL_CONFIG, PATH_KEY, ConfigNode.Type.SIMPLE_OPTION, SCANNNER.nextLine()));
        }
    }

    private static void parseGlobalConfig() {
        System.err.println("Parsing global config.");
        GLOBAL_CONFIG.parse(USER_DIR + "/mccg.ini", ParserMethods.INI_PARSER_WITH_COMPLEX_OPTIONS);
        System.err.println("Global config parsed.");
    }

    private static void runOnStartTasks() {
        System.err.println("Running start-up tasks.");
        if (TASKS.get("start") == null) return;
        for (String task : TASKS.get("start").keySet()) {
            System.err.println("Running task " + task);
            System.out.println("Running task " + task);
            TASKS.get("start").get(task).run();
        }
    }

    private static void runOnEachTasks() {
        System.err.println("Parsing content config.");
        CONTENT_CONFIG.foreach(node -> {
            if (!node.getType().equals(ConfigNode.Type.COMPLEX_OPTION))
                return;

            ConfigSection section = (ConfigSection) node;

            if (section.getChild("type") == null) {
                return;
            }

            System.err.println("Parsing content " + section);
            System.out.println("Parsing content " + section.getName());

            for (String task : TASKS.get("each").keySet()) {
                try {
                    if (!section.getChildValue(task).equals("true"))
                        continue;
                } catch (NullPointerException e) {
                    System.err.println("Failed to complete task " + task + ". This may be because it was not set.");
                    e.printStackTrace();
                }

                System.err.println("Running task " + task);
                System.out.println("Running task " + task);

                TASKS.get("each").get(task).run(section);
            }
        });
    }

    private static void runOnEndTasks() {
        System.err.println("Running shutdown tasks.");
        if (TASKS.get("start") == null) return;
        for (String task : TASKS.get("end").keySet()) {
            System.err.println("Running task " + task);
            System.out.println("Running task " + task);
            TASKS.get("end").get(task).run();
        }
    }

    private static void loadTasks() {
        System.err.println("Loading tasks.");
        for (ConfigNode node : GLOBAL_CONFIG.getChildAsSection("Tasks").getChildren()) {
            if (!node.getType().equals(ConfigNode.Type.COMPLEX_OPTION))
                continue;

            ConfigSection section = (ConfigSection) node;

            CompilerConfiguration config = new CompilerConfiguration();

            String scriptClass = section.getChildValue("is");

            System.err.println("Loading script class " + scriptClass);

            config.setScriptBaseClass(scriptClass);

            GroovyShell shell = new GroovyShell(Main.class.getClassLoader(), new Binding(), config);

            String taskPath = USER_DIR + "/" + section.getChildValue(PATH_KEY);

            try {
                System.err.println("Compiling task " + taskPath);

                Task task = (Task) shell.parse(new File(taskPath));

                TASKS.computeIfAbsent(section.getChildValue("on"), v -> new HashMap<>());

                TASKS.get(section.getChildValue("on")).put(section.getName(), task);
            } catch (CompilationFailedException e) {
                System.err.println("Failed to compile task. Task " + taskPath + " could not be compiled.");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("An IO exception occured while loading task " + taskPath);
                e.printStackTrace();
            }
        }

        System.err.println("Task loading complete. " + TASKS.size() + "/"
                + GLOBAL_CONFIG.getChildAsSection("Tasks").getChildren().size());
    }

    private static void extractResources() {
        System.err.println("Extracting resources from jar.");
        new File(USER_DIR + "/tasks/").mkdir();
        new File(USER_DIR + "/templates/").mkdir();

        extractResource("/mccg.ini", USER_DIR + "/mccg.ini");

        extractResource("/tasks/genBlockState.groovy", USER_DIR + "/tasks/genBlockState.groovy");
        extractResource("/tasks/genModel.groovy", USER_DIR + "/tasks/genModel.groovy");
        extractResource("/tasks/genTexture.groovy", USER_DIR + "/tasks/genTexture.groovy");
        extractResource("/tasks/genLocale.groovy", USER_DIR + "/tasks/genLocale.groovy");
        extractResource("/tasks/onStart.groovy", USER_DIR + "/tasks/onStart.groovy");

        extractResource("/templates/templateBlockState.json", USER_DIR + "/templates/templateBlockState.json");
        extractResource("/templates/templateBlockModel.json", USER_DIR + "/templates/templateBlockModel.json");
        extractResource("/templates/templateBlockItemModel.json", USER_DIR + "/templates/templateBlockItemModel.json");
        extractResource("/templates/templateItemModel.json", USER_DIR + "/templates/templateItemModel.json");

        System.err.println("Resource extraction complete.");
    }

    private static void extractResource(String name, String to) {
        File file = new File(to);

        if (file.exists())
            return;

        System.err.println("Extracting resource " + name);
        try (InputStream in = Main.class.getResourceAsStream(name);
                FileOutputStream out = new FileOutputStream(file)) {
            out.write(in.readAllBytes());
            System.err.println("Resource Extracted.");
        } catch (IOException e) {
            System.err.println(
                    "Failed to extract resourcse from jar. The resource " + name + " could not be extracted to " + to);
            e.printStackTrace();
        }
    }

    private static void redirectExceptionStream() {
        try {
            System.setErr(new PrintStream(new File(USER_DIR + "/latest.log")));
            System.err.println("Error stream redirected to file.");
        } catch (FileNotFoundException e) {
            System.err.println(
                    "Error stream redirection failed. The file " + USER_DIR + "/latest.log" + " was not found.");
            e.printStackTrace();
        }
    }
}
