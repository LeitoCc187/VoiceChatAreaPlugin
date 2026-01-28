package elixius.proxychat.voiceChatArea.updates;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String apiUrl;
    private String latestVersion = "";

    public UpdateChecker(JavaPlugin plugin, String apiUrl) {
        this.plugin = plugin;
        this.apiUrl = apiUrl;
    }

    /**
     * Check for updates
     * @return true if check was successful (doesn't mean update available)
     */
    public boolean check() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "VoiceChatArea-UpdateChecker");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();

                // For GitHub releases API, use "tag_name"
                if (json.has("tag_name")) {
                    latestVersion = json.get("tag_name").getAsString().replace("v", "");
                }
                // For custom JSON format {"version": "1.0.0"}
                else if (json.has("version")) {
                    latestVersion = json.get("version").getAsString();
                }

                return true;
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Update check failed: " + e.getMessage());
        }
        return false;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}