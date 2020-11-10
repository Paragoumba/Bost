package fr.paragoumba.bost.api;

public abstract class Plugin {

    private PluginInfo info;

    public void onEnable(){}
    public void onDisable(){}

    public PluginInfo getInfo(){

        return info;

    }

    public void setInfo(PluginInfo info){

        this.info = info;

    }
}
