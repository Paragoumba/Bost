package fr.paragoumba.bost.api;

import java.util.List;

public class PluginInfo {

    public PluginInfo(String name, String version, String author, List<String> permissions){

        this.name = name;
        this.version = version;
        this.author = author;
        this.permissions = permissions;

    }

    private String name;
    private String version;
    private String author;
    private List<String> permissions;

    public String getName(){

        return name;

    }

    public String getVersion(){

        return version;

    }

    public String getAuthor(){

        return author;

    }

    public List<String> getPermissions(){

        return permissions;

    }

    @Override
    public String toString(){

        return name + (version != null ? " (v" + version + ")" : "") + " by " + (author != null ? author : "Unknown");

    }
}
