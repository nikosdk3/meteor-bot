package org.meteordev.meteorbot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.meteordev.meteorbot.MeteorBot;
import org.meteordev.meteorbot.command.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands extends ListenerAdapter {
    private static final Map<String, Command> commands = new HashMap<>();

    public static void add(Command command) {
        commands.put(command.name, command);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        add(new CatCommand());
        add(new MonkyCommand());
        add(new StatsCommand());
        if (MeteorBot.BACKEND_TOKEN != null) add(new LinkCommand());
        add(new MuteCommand());
        add(new UnmuteCommand());
        add(new LogsCommand());
        add(new FaqCommand());
        add(new InstallationCommand());
        add(new OldVersionCommand());
        add(new CapyCommand());
        add(new PandaCommand());
        add(new DogCommand());

        List<CommandData> commandData = new ArrayList<>();

        for (Command command : commands.values()) {
            commandData.add(command.build(new CommandDataImpl(command.name, command.description)));
        }

        MeteorBot.BOT.updateCommands().addCommands(commandData).complete();

        MeteorBot.LOG.info("Loaded {} commands", commands.size());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getName());
        if (command == null) return;

        command.run(event);
    }
}
