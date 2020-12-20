package minegame159.meteorbot.webserver;

import minegame159.meteorbot.webserver.controllers.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.File;
import java.io.StringWriter;
import java.util.Properties;

import static spark.Spark.*;

public class WebServer {
    public static void init() {
        MainController.init();
        ApiController.updateCapes();

        Properties velocityProperties = new Properties();
        if (System.getenv("MY_PC") == null) {
            staticFiles.location("public");
            staticFiles.expireTime(600);

            velocityProperties.put(RuntimeConstants.RESOURCE_LOADER, "class");
            velocityProperties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        } else {
            staticFiles.externalLocation(new File(System.getProperty("user.dir"), "src/main/resources/public").getAbsolutePath());

            velocityProperties.put(RuntimeConstants.RESOURCE_LOADER, "file");
            velocityProperties.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, new File(System.getProperty("user.dir"), "src/main/resources").getAbsolutePath());
        }
        Velocity.init(velocityProperties);
        Mail.init();

        port(8082);

        before((request, response) -> LoginController.onRequest());

        get("/", MainController.SERVER_INDEX);
        get("/info", MainController.SERVER_INFO);
        get("/download", MainController.HANDLE_DOWNLOAD);

        get("/account", AccountController.SERVE_ACCOUNT);
        post("/selectCape", AccountController.HANDLE_SELECT_CAPE);
        get("/changeUsername", AccountController.SERVE_CHANGE_USERNAME);
        post("/changeUsername", AccountController.HANDLE_CHANGE_USERNAME);
        get("/changePassword", AccountController.SERVE_CHANGE_PASSWORD);
        post("/changePassword", AccountController.HANDLE_CHANGE_PASSWORD);
        get("/changeEmail", AccountController.SERVE_CHANGE_EMAIL);
        post("/changeEmail", AccountController.HANDLE_CHANGE_EMAIL);
        get("/discordAuth", AccountController.HANDLE_DISCORD_AUTH);
        post("/unlinkDiscord", AccountController.HANDLE_UNLINK_DISCORD);
        post("/addMcAccount", AccountController.HANDLE_ADD_MC_ACCOUNT);
        post("/removeMcAccount", AccountController.HANDLE_REMOVE_MC_ACCOUNT);

        get("/register", LoginController.SERVE_REGISTER);
        post("/register", LoginController.HANDLE_REGISTER);
        get("/confirm/:token", LoginController.HANDLE_CONFIRM);
        get("/confirm", LoginController.SERVE_CONFIRM);
        get("/login", LoginController.SERVE_LOGIN);
        post("/login", LoginController.HANDLE_LOGIN);
        get("/forgotPassword", LoginController.SERVE_FORGOT_PASSWORD);
        post("/forgotPassword", LoginController.HANDLE_FORGOT_PASSWORD);
        post("/logout", LoginController.HANDLE_LOGOUT);

        get("/discord", RedirectController.HANDLE_DISCORD);
        get("/donate", RedirectController.HANDLE_DONATE);
        get("/youtube", RedirectController.HANDLE_YOUTUBE);
        get("/github", RedirectController.HANDLE_GITHUB);

        get("/capes/:name", CapesController.SERVE_CAPES);
        post("/capes/uploadCustom", CapesController.HANDLE_UPLOAD_CUSTOM);

        get("/api/version", ApiController.HANDLE_VERSION);
        get("/api/capeowners", ApiController.HANDLE_CAPE_OWNERS);
        get("/api/capes", ApiController.HANDLE_CAPES);
        get("/api/togglediscord", ApiController.HANDLE_TOGGLE_DISCORD);
        get("/api/online/ping", ApiController.HANDLE_ONLINE_PING);
        get("/api/online/leave", ApiController.HANDLE_ONLINE_LEAVE);
    }

    public static String render(VelocityContext context, String template) {
        StringWriter writer = new StringWriter();
        Velocity.getTemplate(template).merge(context, writer);
        return writer.toString();
    }

    public static void close() {
        stop();
        awaitStop();
    }
}