package fr.paragoumba.bost.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

import static fr.paragoumba.bost.CommandManager.interpretMessageAsCommand;

public class MessageReceivedEventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event){

        Message msg = event.getMessage();

        interpretMessageAsCommand(msg);

    }
}
