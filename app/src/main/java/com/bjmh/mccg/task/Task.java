package com.bjmh.mccg.task;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.mccg.Main;

import groovy.lang.Script;

public abstract class Task extends Script {
    protected final String modid = ((ConfigOption) Main.GLOBAL_CONFIG.getChild("modid")).getValue();

    protected final String blockStatesPath = Main.USER_DIR + "assets/blockstates/";
    protected final String blockModelsPath = Main.USER_DIR + "assets/models/block/";
    protected final String itemModelsPath = Main.USER_DIR + "assets/models/item/";
    protected final String blockTexturesPath = Main.USER_DIR + "assets/textures/block/";
    protected final String itemTexturesPath = Main.USER_DIR + "assets/texutes/item/";
    protected final String langPath = Main.USER_DIR + "assets/lang/";

    protected ConfigSection section;

    public void run(ConfigSection section) {
        this.section = section;
        this.run();
    }
}