package com.yuhtin.devroom.core;

import com.yuhtin.devroom.configuration.YamlConfiguration;
import com.yuhtin.devroom.util.Logger;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public abstract class Bot {

    @Getter private final Logger logger = Logger.getLogger();
    @Getter @Setter private YamlConfiguration config;

    public void onLoad() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

}
