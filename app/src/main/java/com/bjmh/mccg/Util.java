package com.bjmh.mccg;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public class Util {
    public static final Util INSTANCE = new Util();

    private Util() {}

    public ConfigSection getChildAsSection(String name, ConfigSection from) {
        if (!(from.getChild(name) instanceof ConfigSection))
            return null;

        return (ConfigSection) from.getChild(name);
    }

    public ConfigOption getChildAsOption(String name, ConfigSection from) {
        if (!(from.getChild(name) instanceof ConfigOption))
            return null;

        return (ConfigOption) from.getChild(name);
    }

    public String getChildValue(String name, ConfigSection from) {
        if (!(from.getChild(name) instanceof ConfigOption))
            return null;

        return ((ConfigOption) from.getChild(name)).getValue();
    }
}
