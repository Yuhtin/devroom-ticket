package com.yuhtin.devroom.util;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class WaitingEvent<T extends GenericEvent> {
    final Predicate<T> condition;
    final Consumer<T> action;

    WaitingEvent(Predicate<T> condition, Consumer<T> action) {
        this.condition = condition;
        this.action = action;
    }

    boolean attempt(T event) {
        if (this.condition.test(event)) {
            this.action.accept(event);
            return true;
        } else {
            return false;
        }
    }
}
