package io.github._4drian3d.unsignedvelocity.updatechecker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github._4drian3d.unsignedvelocity.utils.Constants;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class UpdateChecker {
    private final ComponentLogger logger;

    public UpdateChecker(ComponentLogger logger) {
        this.logger = logger;
    }

    public void checkForUpdates() {
        try {
            Version latestVersion = getLatestVersion();
            if (isUpdateAvailable(latestVersion)) {
                logger.info("There is an update available for UnSignedVelocity: {} (Your version: {})", latestVersion, Constants.VERSION);
            } else {
                logger.info(miniMessage().deserialize(
                        "<#6892bd>You are using the latest version of <gradient:#166D3B:#7F8C8D:#A29BFE>UnSignedVelocity</gradient>"));
            }
        } catch (Exception e) {
            logger.error("Cannot check for updates", e);
        }
    }

    private static Version getLatestVersion() throws Exception {
        String url = "https://api.github.com/repos/MemencioPerez/UnSignedVelocity/releases/latest";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            return Version.parse(jsonObject.get("tag_name").getAsString());
        }
    }

    private static boolean isUpdateAvailable(Version latestVersion) {
        return Version.parse(Constants.VERSION).isEqualOrLowerThan(latestVersion);
    }
}