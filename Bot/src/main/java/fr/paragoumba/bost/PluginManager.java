package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class PluginManager {

    private static final Logger logger = Bot.getLogger();
    private static final HashSet<Plugin> plugins = new HashSet<>();

    static void loadPlugins(){

        plugins.add(new BostCommands());

        try {

            File pluginsDir = new File("plugins");

            if (!pluginsDir.exists()){

                pluginsDir.mkdirs();
                logger.info("Plugins' directory created.");

            } else {

                logger.info("Plugins' directory found.");

            }

            File[] files = pluginsDir.listFiles();

            if (files != null){

                Yaml yaml = new Yaml();

                for (File file : files){

                    if (file.getName().endsWith(".jar")){

                        JarFile jarFile = new JarFile(file);
                        JarEntry pluginYML = jarFile.getJarEntry("plugin.yml");

                        if (pluginYML != null){

                            try {

                                HashMap<String, String> props = yaml.load(jarFile.getInputStream(pluginYML));
                                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
                                Plugin plugin = (Plugin) Class.forName(props.get("main"), true, classLoader).getConstructor().newInstance();

                                Logger.getLogger("stdout").info("Loaded plugin " + props.get("name") + " (v" + props.get("version") + ") by " + props.get("author"));
                                plugins.add(plugin);

                            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException e){

                                System.err.println("Error in loading main class. Verify that it extends Plugin.");
                                e.printStackTrace();

                            } catch (ClassCastException e){

                                // TODO Log correctly
                                e.printStackTrace();

                            }

                        } else {

                            System.err.println("The file plugin.yml is missing or corrupted!");

                        }
                    }
                }
            }

        } catch (IOException e){

            e.printStackTrace();

        }
    }

    static void enablePlugins(){

        for (Plugin plugin : plugins){

            plugin.onEnable();

        }
    }
}
