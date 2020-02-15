package fr.paragoumba.bost;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class Configuration {

    public Configuration(){

        this("config.yml");

    }

    public Configuration(String configPath){

        token = "";
        customConfig = new HashMap<>();

        File configFile = new File(configPath);

        if (!configFile.exists()){

            save(configFile);
            logger.severe("Created config file. Please enter a valid Discord Bot token.");
            return;

        }

        load(configFile);

    }

    private static final Logger logger = Bot.getLogger();
    private String token;
    private HashMap<String, Object> customConfig;

    String getToken(){

        return token;

    }

    Object setString(String key, String value){

        return customConfig.put(key, value);

    }

    String getString(String key){

        return (String) customConfig.get(key);

    }

    private void load(File configFile){

        Yaml yaml = new Yaml();

        try {

            LinkedHashMap<Object, Object> configMap = yaml.load(new FileReader(configFile));

            token = (String) configMap.get("token");

        } catch (FileNotFoundException e){

            logger.warning("Error while trying to load config. Default one will be used.");
            e.printStackTrace();

        }
    }

    private void save(File configFile){

        DumperOptions options = new DumperOptions();

        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        try {

            yaml.dump(this, new FileWriter(configFile));

        } catch (IOException e){

            logger.severe("Error while trying to save config.");
            e.printStackTrace();

        }
    }
}
