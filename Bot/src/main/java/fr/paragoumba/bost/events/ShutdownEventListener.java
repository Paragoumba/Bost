package fr.paragoumba.bost.events;

import fr.paragoumba.bost.PluginManager;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ShutdownEventListener extends ListenerAdapter {

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event){

        PluginManager.disablePlugins();

    }
}
