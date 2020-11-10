package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Plugin;
import fr.paragoumba.bost.api.PluginInfo;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {

    public static final String pluginsDir = "plugins";

    private static final Logger logger = Bot.getLogger();
    private static final HashSet<Plugin> plugins = new HashSet<>();

    static void loadPlugins() throws IOException {

        logger.info("Loading plugins.");

        plugins.add(new BostCommands());

        File pluginsDir = new File(PluginManager.pluginsDir);

        if (!pluginsDir.exists()){

            if (!pluginsDir.mkdirs()){

                throw new IOException("Could not create plugins dir.");

            }

            logger.info("Plugins' directory created.");

        } else {

            logger.info("Plugins' directory found.");

        }

        File[] files = pluginsDir.listFiles();

        if (files != null){

            for (File file : files){

                if (file.getName().endsWith(".jar")){

                    try {

                        JarFile jarFile = new JarFile(file);
                        JarEntry pluginYML = jarFile.getJarEntry("plugin.yml");

                        if (pluginYML != null){

                            Yaml yaml = new Yaml();
                            HashMap<String, Object> props = yaml.load(jarFile.getInputStream(pluginYML));
                            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());

                            Object permissions = props.getOrDefault("permissions", new ArrayList<>());

                            Plugin plugin = (Plugin) Class.forName((String) props.get("main"), true, classLoader).getConstructor().newInstance();
                            PluginInfo pluginInfo = new PluginInfo((String) props.get("name"), (String) props.get("version"), (String) props.get("author"), List.copyOf((List) permissions));

                            plugin.setInfo(pluginInfo);
                            plugins.add(plugin);

                            logger.info("Loaded plugin " + pluginInfo);

                        } else {

                            logger.warn('[' + jarFile.getName() + "] The file plugin.yml is missing or corrupted!");

                        }

                    } catch (IllegalAccessException | ClassNotFoundException | InstantiationException |
                            InvocationTargetException | NoSuchMethodException e){

                        logger.warn("Error in loading main class. Verify that it extends Plugin.");

                    } catch (ClassCastException | IOException e){

                        logger.warn(e.getLocalizedMessage());

                    }
                }
            }
        }
    }

    public static void enablePlugins(){

        logger.info("Enabling plugins.");

        for (Plugin plugin : plugins){

            plugin.onEnable();

        }
    }

    public static void disablePlugins(){

        logger.info("Disabling plugins.");

        for (Plugin plugin : plugins){

            plugin.onDisable();

        }
    }

    public static HashSet<Plugin> getPermissions(){

        return plugins;

    }
}
