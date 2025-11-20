package lusouzadev.enhancedmotd;

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
        EnhancedMotd.log("Server started, config loaded");
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
                EnhancedMotd.log("Reloading EnhancedMOTD config due to /reload command");
                EnhancedMotd.loadConfigFromFile();
            } catch (Exception e) {
                EnhancedMotd.log("Failed to reload config: " + e.getMessage());
            }
        }
    }
}
