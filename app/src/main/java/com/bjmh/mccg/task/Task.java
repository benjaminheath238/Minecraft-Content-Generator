package com.bjmh.mccg.task;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.mccg.Main;
import com.bjmh.mccg.Util;

import groovy.lang.Script;

public abstract class Task extends Script {
    protected final Util util = Util.INSTANCE;

    protected final String modid = ((ConfigOption) Main.GLOBAL_CONFIG.getChild("modid")).getValue();

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