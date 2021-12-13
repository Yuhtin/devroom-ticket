package com.yuhtin.devroom.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlConfiguration {

    @Getter private final Map<String, Object> entries;
    @Getter private final List<ActionRow> menus = new ArrayList<>();

    public static YamlConfiguration of(File file) {
        try {
            Yaml yaml = new Yaml();
            return new YamlConfiguration(yaml.load(new FileInputStream(file)));
        } catch (Exception exception) {
            return null;
        }
    }

    public static YamlConfiguration createDefault() {
        return new YamlConfiguration(new HashMap<String, Object>() {{
            put("token", "0000011111");
            put("ticketTypes", Arrays.asList("Payments", "Bug", "Other"));
            put("ticketCategoryID", 0L);
            put("supportRoleID", 0L);
        }});
    }

    public String getString(String path) {
        return (String) getObject(path);
    }

    public List<String> getStringList(String path) {
        return (List<String>) getObject(path);
    }

    public Object getObject(String path) {
        return entries.getOrDefault(path, null);
    }

    public void buildMenu() {
        List<Component> tempList = new ArrayList<>();
        List<String> ticketTypes = getStringList("ticketTypes");
        for (String entry : ticketTypes) {
            tempList.add(Button.success(entry.toLowerCase(), entry));

            if (tempList.size() == 5) {
                menus.add(ActionRow.of(tempList));
                tempList.clear();
            }
        }

        if (tempList.isEmpty()) return;

        menus.add(ActionRow.of(tempList));
        tempList.clear();
    }

    public void write(File file) {
        try {
            Yaml yaml = new Yaml();
            StringWriter stringWriter = new StringWriter();
            yaml.dump(entries, stringWriter);

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(stringWriter.toString());
            fileWriter.close();

            stringWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
