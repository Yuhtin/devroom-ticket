package com.yuhtin.devroom.configuration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlConfiguration {

    @Getter
    private final Map<String, Object> entries;

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
