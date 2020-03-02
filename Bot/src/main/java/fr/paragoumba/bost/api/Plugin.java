package fr.paragoumba.bost.api;

import fr.paragoumba.bost.Configuration;

public abstract class Plugin {

    private PluginInfo info;
    private Configuration config;

    public void onEnable(){}
    public void onDisable(){}

    public Configuration getConfig(){

        if (config == null){

            config = new Configuration(this);
            //setConfigDefaults();
            // TODO Fix defaults

        }

        return config;

    }

    protected void setConfigDefaults(){}

    public PluginInfo getInfo(){

        return info;

    }

    public void setInfo(PluginInfo info){

        this.info = info;

    }
}
