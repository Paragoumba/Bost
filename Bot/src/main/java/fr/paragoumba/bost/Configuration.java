package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Plugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class Configuration {

    Configuration(){

        this(configFile);

        if (map.isEmpty()){

            logger.info("Created config file. Please enter a valid Discord Bot token.");

        }
    }

    public Configuration(Plugin plugin){

        this(PluginManager.pluginsDir + "/" +
                plugin.getInfo().getName().replaceAll(
                        "[\\x00-\\x1F\\x7F\"*/:<>\\\\?|\\u0000]", "_") +
                "/" + configFile);

    }

    Configuration(String configPath){

        map = new LinkedHashMap<>();

        File configFile = new File(configPath);

        if (!configFile.exists()){

            File parent = configFile.getParentFile();

            if (!parent.exists()){

                if (!parent.mkdirs()){

                    new IOException("Could not create " + configFile.getName() + " config file.").printStackTrace();
                    return;

                }
            }

            save(configFile);
            return;

        }

        load(configFile);

    }

    private static final String configFile = "config.yml";
    private static final char separator = '.';
    private static final Logger logger = Bot.getLogger();

    private LinkedHashMap<Object, Object> map;

    public Object set(String key, Object value){

        if (key == null){

            throw new NullPointerException("Key cannot be null.");

        }

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

        return ((HashMap) section).put(key.substring(startIndex), value);

    }

    public Object get(String key){

        if (key == null){

            throw new NullPointerException("Key cannot be null.");

        }

        if (key.length() == 0){

            return this;

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

    public String getString(String key){

        return (String) get(key);

    }

    public void setString(String key, String value){

        set(key, value);

    }

    public Integer getInt(String key){

        return (Integer) get(key);

    }

    public void setInt(String key, int value){

        set(key, value);

    }

    private void load(File configFile){

        Yaml yaml = new Yaml();

        try {

            map = yaml.load(new FileReader(configFile));

        } catch (FileNotFoundException e){

            logger.warning("Error while trying to load config. Default one will be used.");
            e.printStackTrace();

        }
    }

    public void save(File configFile){

        DumperOptions options = new DumperOptions();

        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        try {

            yaml.dump(map, new FileWriter(configFile));

        } catch (IOException e){

            logger.warning("Error while trying to save config.");
            e.printStackTrace();

        }
    }
}
