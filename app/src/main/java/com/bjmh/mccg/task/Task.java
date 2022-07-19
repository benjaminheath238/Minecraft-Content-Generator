package com.bjmh.mccg.task;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.mccg.Main;

import groovy.lang.Script;

public abstract class Task extends Script {
    protected final String modid = ((ConfigOption) Main.GLOBAL_CONFIG.getChild(Main.MODID_KEY)).getValue();

    protected ConfigSection section;

    public void run(ConfigSection section) {
        this.section = section;
        this.run();
    }
}