package org.meteordev.meteorbot;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Utils {
    public static final Color COLOR = new Color(145, 61, 226);

    public static EmbedBuilder embedTitle(String title, String format, Object... args) {
        return new EmbedBuilder()
            .setColor(COLOR)
            .setTitle(title)
            .setDescription(String.format(format, args));
    }

    public static EmbedBuilder embed(String format, Object... args) {
        return embedTitle(null, format, args);
    }

    public static GetRequest apiGet(String path, boolean auth) {
        GetRequest req = Unirest.get(MeteorBot.API_BASE + path);
        if (auth) req.header("Authorization", MeteorBot.BACKEND_TOKEN);
        return req;
    }

    public static HttpRequestWithBody apiPost(String path) {
        HttpRequestWithBody req = Unirest.post(MeteorBot.API_BASE + path);
        req.header("Authorization", MeteorBot.BACKEND_TOKEN);
        return req;
    }
}
