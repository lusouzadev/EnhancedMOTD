package lusouzadev.randommotd;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/**
 * Event handler to handle config reloading.
 * Handles /reload command interception.
 */
public class ServerEventHandler {

    /**
     * Called when the server finishes starting.
     * Ensures config is loaded.
     */
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        RandomMotd.log("Server started, config loaded");
    }

    /**
     * Intercepts commands to reload config when /reload is used.
     */
    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        String command = event.getParseResults().getReader().getString();

        // Check if this is a reload command
        if (command.startsWith("reload")) {
            try {
                RandomMotd.log("Reloading RandomMOTD config due to /reload command");
                RandomMotd.loadConfigFromFile();
            } catch (Exception e) {
                RandomMotd.log("Failed to reload config: " + e.getMessage());
            }
        }
    }
}
