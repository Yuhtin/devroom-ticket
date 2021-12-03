package com.yuhtin.devroom.core;

import lombok.Getter;

import java.util.logging.Logger;

public abstract class Bot {

    @Getter private final Logger logger = Logger.getLogger("Bot");

    public void onEnable() {

    }

    public void onDisable() {

    }

}
