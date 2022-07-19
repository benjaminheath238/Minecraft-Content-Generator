package com.bjmh.mccg.task;

import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.mccg.Main;

import groovy.lang.Script;

public abstract class Task extends Script {
    protected final String modid = Main.GLOBAL_CONFIG.getChildValue("modid");
    protected final String dir = Main.USER_DIR;

    protected final String pathBlockStates = Main.USER_DIR + "assets/blockstates/";
    protected final String pathModelsBlock = Main.USER_DIR + "assets/models/block/";
    protected final String pathModelsItem = Main.USER_DIR + "assets/models/item/";
    protected final String pathTexturesBlock = Main.USER_DIR + "assets/textures/block/";
    protected final String pathTexturesItem = Main.USER_DIR + "assets/texutes/item/";
    protected final String pathLang = Main.USER_DIR + "assets/lang/";

    protected ConfigSection section;

    public void run(ConfigSection section) {
        this.section = section;
        this.run();
    }
}