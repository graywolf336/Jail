package com.graywolf336.jail.legacy;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class OldSettings {

    public static Object getGlobalProperty(YamlConfiguration config, OldSetting setting) {
        Object property = config.get(setting.getString());
        if (property == null) {
            property = setting.getDefault();
        }

        return property;
    }

    public static Boolean getGlobalBoolean(YamlConfiguration config, OldSetting setting) {
        return 	(Boolean) getGlobalProperty(config, setting);
    }

    public static Integer getGlobalInt(YamlConfiguration config, OldSetting setting) {
        return 	(Integer) getGlobalProperty(config, setting);
    }

    public static String getGlobalString(YamlConfiguration config, OldSetting setting) {
        return 	(String) getGlobalProperty(config, setting);
    }

    public static List<?> getGlobalList(YamlConfiguration config, OldSetting setting) {
        return 	(List<?>) getGlobalProperty(config, setting);
    }
}
