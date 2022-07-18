package com.bjmh.mccg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.bjmh.lib.io.config.ConfigConsumer;
import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.lib.io.config.Configuration;
import com.bjmh.lib.io.config.ParserMethods;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Main {
    public static final String MODID = "modid";
    public static final String LOCALE = "locale";
    public static final String BLOCK_STATE = "blockstate";
    public static final String BLOCK = "block";
    public static final String ITEM = "item";
    public static final String TRUE = "true";
    public static final String MODEL = "model";
    public static final String TYPE = "type";
    public static final String FILE_SEPARATOR = "/";
    public static final String PATH = "path";

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final Configuration GLOBAL_CONFIG = new Configuration("global");
    public static final Configuration CONTENT_CONFIG = new Configuration("content");
    public static final Map<String, Task> TASKS = new HashMap<>();
    public static final Scanner SCANNNER = new Scanner(System.in);

    public static void main(String[] args) {
        redirectexceptionStream();

        createConfigFile();
        parseConfigFiles();

        parseTasks();

        runTasks();

        // oldMethod();

        SCANNNER.close();
    }

    private static void parseTasks() {
        new File(USER_DIR + "/templates/").mkdirs();

        try (InputStream in = Main.class.getResourceAsStream("/templates/genBlockModel.json");
                FileOutputStream out = new FileOutputStream(USER_DIR + "/templates/genBlockModel.json")) {
            out.write(in.readAllBytes());
        } catch (IOException e) {
            System.err.println("+- An exception occured during task file extraction.");
            e.printStackTrace();
        }

        try (InputStream in = Main.class.getResourceAsStream("/templates/genItemModel.json");
                FileOutputStream out = new FileOutputStream(USER_DIR + "/templates/genItemModel.json")) {
            out.write(in.readAllBytes());
        } catch (IOException e) {
            System.err.println("+- An exception occured during task file extraction.");
            e.printStackTrace();
        }

        try (InputStream in = Main.class.getResourceAsStream("/templates/genBlockState.json");
                FileOutputStream out = new FileOutputStream(USER_DIR + "/templates/genBlockState.json")) {
            out.write(in.readAllBytes());
        } catch (IOException e) {
            System.err.println("+- An exception occured during task file extraction.");
            e.printStackTrace();
        }

        ((ConfigSection) GLOBAL_CONFIG.getChild("Tasks")).foreach(new ConfigConsumer() {
            @Override
            public void accept(ConfigNode node) {
                if (!(node instanceof ConfigSection))
                    return;

                ConfigSection section = (ConfigSection) node;

                if (section.getChild(PATH) == null)
                    return;

                String path = ((ConfigOption) section.getChild(PATH)).getValue();

                try {
                    Task task = new Gson().fromJson(new FileReader(USER_DIR + FILE_SEPARATOR + path), Task.class);

                    TASKS.put(task.getName(), task);
                } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                    System.err.println("+- An exception occured while loading task: " + section.getName());
                    e.printStackTrace();
                }
            }
        });
    }

    private static void runTasks() {
        CONTENT_CONFIG.foreach(new ConfigConsumer() {
            @Override
            public void accept(ConfigNode node) {
                System.err.println("+- Loading node: " + node.getName() + ", of type: " + node.getType()
                        + ", with parent: " + node.getParent().getName());

                if (!node.getType().equals(ConfigNode.Type.COMPLEX_OPTION)) {
                    System.err.println("| This option is not a complex option.");
                    return;
                }

                ConfigSection section = (ConfigSection) node;

                if (section.getChild(TYPE) == null) {
                    System.err.println("| This option has no type variable.");
                    return;
                }

                System.out.println("+- Loading option: " + section.getName());

                for (String task : TASKS.keySet()) {
                    if (section.getChild(task) != null
                            && ((ConfigOption) section.getChild(task)).getValue().equals(TRUE)) {
                        System.err.println("| Running task: " + task);
                        System.out.println("| Running task: " + task);
                        TASKS.get(task).run(section);
                    }
                }
            }
        });
    }

    private static void parseConfigFiles() {
        System.err.println("+- Parsing global config file.");
        GLOBAL_CONFIG.parse(USER_DIR + "/mccg.ini", ParserMethods.INI_PARSER_WITH_COMPLEX_OPTIONS);

        if (((ConfigOption) GLOBAL_CONFIG.getChild(MODID)) == null) {
            System.err.println("| No modid variable was found in the global config. Requesting user input.");
            System.out.println("Please enter a modid.");
            GLOBAL_CONFIG.addChild(
                    new ConfigOption(GLOBAL_CONFIG, MODID, ConfigNode.Type.SIMPLE_OPTION, SCANNNER.nextLine()));
        }

        if (((ConfigOption) GLOBAL_CONFIG.getChild(PATH)) == null) {
            System.err.println("| No path variable was found in the global config. Requesting user input.");
            System.out.println("Please enter the content config file path.");
            GLOBAL_CONFIG.addChild(
                    new ConfigOption(GLOBAL_CONFIG, PATH, ConfigNode.Type.SIMPLE_OPTION, SCANNNER.nextLine()));
        }

        System.err.println("| Global config file: " + GLOBAL_CONFIG);

        System.err.println("+- Parsing content config file.");
        CONTENT_CONFIG.parse(((ConfigOption) GLOBAL_CONFIG.getChild(PATH)).getValue(),
                ParserMethods.INI_PARSER_WITH_INHERITANCE);

        System.err.println("| Content config file: " + CONTENT_CONFIG);
    }

    private static void redirectexceptionStream() {
        try {
            System.setErr(new PrintStream(new File(USER_DIR + "/latest.log")));
        } catch (FileNotFoundException e) {
            System.err.println("+- An exception occured during exception stream redirection.");
            e.printStackTrace();
        }
    }

    private static void createConfigFile() {
        File globalConfigFile = new File(USER_DIR + "/mccg.ini");

        if (globalConfigFile.exists())
            return;

        try (InputStream in = Main.class.getResourceAsStream("/mccg.ini");
                FileOutputStream out = new FileOutputStream(globalConfigFile)) {

            out.write(in.readAllBytes());

        } catch (IOException e) {
            System.err.println("+- An exception occured during config file creation.");
            e.printStackTrace();
        }
    }
}
