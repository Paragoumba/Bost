package fr.paragoumba.bost.api;

import fr.paragoumba.bost.Configuration;

/**
 * This class loads and creates all the config files used by the plugins.
 */
public class PluginConfiguration extends Configuration {

    /**
     * Load the plugin's own configuration file. If no config exists for this plugin, it creates one.
     * @param plugin The class of the plugin for which you want to get the config
     */
    public PluginConfiguration(Plugin plugin){

        super(pluginConfigPathTemplate.replaceAll("%p", plugin.getInfo().getName().replaceAll(
                "[\\x00-\\x1F\\x7F\"*/:<>\\\\?|\\u0000]", "_")));

    }
}
