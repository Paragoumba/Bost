package fr.paragoumba.bost;

import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * This class loads and creates all the config files used by the bot. To load the config file of a plugin,
 * use {@link fr.paragoumba.bost.api.PluginConfiguration}.
 */
public class Configuration {

    /**
     * Loads the bot's configuration file. If the file doesn't exists, it creates it.
     */
    Configuration(){

        this(configFileName);

        if (map.isEmpty()){

            logger.info("Created config file. Please enter a valid Discord Bot token.");

        }
    }

    /**
     * Loads a config file with the specified path. If the file doesn't exists it is created.
     * @param configPath The path of the config file to load
     */
    protected Configuration(String configPath){

        map = new LinkedHashMap<>();
        configFile = new File(configPath);

        if (!configFile.exists()){

            File parent = configFile.getParentFile();

            if (!parent.exists()){
                if (!parent.mkdirs()){

                    logger.error("Could not create " + configFile.getName() + " config file.\n" +
                            "Config for this plugin will work but there is no default\n" +
                            "config and modifications will not be saved.");

                    return;

                }
            }

            save();
            return;

        }

        load();

    }

    private static final String configFileName = "config.yml";
    protected static final String pluginConfigPathTemplate = PluginManager.pluginsDir + "/%p/" + configFileName;
    private static final char separator = '.';
    private static final Logger logger = Bot.getLogger();

    private LinkedHashMap<Object, Object> map;
    private final File configFile;
    // TODO Implement default config copying
    private String defaultConfigPath;

    /**
     * Set the value for the given key in the config.
     * @param key The key for which to set the value
     * @param value The value to store in the config
     * @return Return the old value or null if there is no old value
     */
    public Object set(@Nonnull String key, Object value){

        Object lastSection;
        Object section = lastSection = map;
        int startIndex = 0;
        int endIndex = -1;

        if (key.length() != 0){

            int newEndIndex;

            while ((newEndIndex = key.indexOf(separator, startIndex = endIndex + 1)) != -1){

                endIndex = newEndIndex;

                if (section instanceof HashMap){

                    lastSection = section;
                    section = ((HashMap) section).get(key.substring(startIndex, endIndex));

                } else {

                    section = new HashMap<>();
                    ((HashMap) lastSection).put(key.substring(startIndex, endIndex), section);

                }
            }
        }

        if (section == null){

            section = new HashMap<>();
            ((HashMap) lastSection).put(key.substring(startIndex, endIndex), section);

        }

        Object o = ((HashMap) section).put(key.substring(startIndex), value);

        logger.info(map.toString());

        return o;

    }

    public Object get(@Nonnull String key){

        if (key.length() == 0){

            return map;

        }

        Object section = map;
        int startIndex;
        int endIndex= -1;

        while ((endIndex = key.indexOf(separator, startIndex = endIndex + 1)) != -1){

            if (section instanceof HashMap){

                section = ((HashMap) section).get(key.substring(startIndex, endIndex));

            } else {

                return null;

            }
        }

        if (section instanceof HashMap){

            return ((HashMap) section).get(key.substring(startIndex));

        } else {

            return null;

        }
    }

    public String getString(@Nonnull String key){

        return (String) get(key);

    }

    public Object setString(@Nonnull String key, String value) throws IllegalAccessException {

        return set(key, value);

    }

    public Integer getInt(@Nonnull String key){

        return (Integer) get(key);

    }

    public Object setInt(@Nonnull String key, int value) throws IllegalAccessException {

        return set(key, value);

    }

    private void setDefaultConfigPath(String defaultConfigPath){



    }

    private void load(){

        Yaml yaml = new Yaml();

        try {

            map = yaml.load(new FileReader(configFile));

        } catch (FileNotFoundException e){

            logger.warn("Error while trying to load config. Default one will be used.");

        }
    }

    public void save(){

        DumperOptions options = new DumperOptions();

        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        try {

            yaml.dump(map, new FileWriter(configFile));

        } catch (IOException e){

            logger.warn("Error while trying to save config.");
            e.printStackTrace();

        }
    }
}
