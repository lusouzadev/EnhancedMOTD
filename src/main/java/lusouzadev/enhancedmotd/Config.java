package lusouzadev.enhancedmotd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public class Config {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Config Default Values

    public String CONFIG_VERSION_DO_NOT_TOUCH_PLS = EnhancedMotd.VERSION.toString();

    public final List<List<String>> motds = defaultSimpleMotds();
    public final boolean use_randomized_icons = true;
    public final List<String> icons = defaultServerIcons();
    public final boolean log_when_loaded = false;
    
    private @NotNull List<List<String>> defaultSimpleMotds() {
        List<List<String>> motds = new ArrayList<>();
        
        List<String> motds_0 = new ArrayList<>();
        Collections.addAll(motds_0,
            "EnhancedMOTD",
            "<rb>EnhancedMOTD</rb>"
        );
        motds.add(motds_0);

        List<String> motds_1 = new ArrayList<>();
        Collections.addAll(motds_1,
            "This is an MOTD",
            "Another MOTD",
            "This is an MOTD with <rb>formatting</rb>"
        );
        motds.add(motds_1);
        
        return motds;
    }

    private @NotNull List<String> defaultServerIcons() {
        ArrayList<String> list = new ArrayList<>();
        list.add("server-icon.png");
        return list;
    }

    public static Config init() {
        Config config = null;

        try {
            Path configPath = Paths.get("", "config", EnhancedMotd.MODID + ".json");

            if (Files.exists(configPath)) {
                config = gson.fromJson(
                    new FileReader(configPath.toFile()),
                    Config.class
                );

                if (!config.CONFIG_VERSION_DO_NOT_TOUCH_PLS.equals(EnhancedMotd.VERSION.toString())) {
                    config.CONFIG_VERSION_DO_NOT_TOUCH_PLS = EnhancedMotd.VERSION.toString();

                    BufferedWriter writer = new BufferedWriter(
                        new FileWriter(configPath.toFile())
                    );

                    writer.write(gson.toJson(config));
                    writer.close();
                }

            } else {
                config = new Config();
                File configDir = Paths.get("", "config").toFile();
                if (!configDir.exists() && !configDir.mkdirs()) {
                    EnhancedMotd.LOGGER.warn("Failed to create config directory");
                }

                BufferedWriter writer = new BufferedWriter(
                    new FileWriter(configPath.toFile())
                );

                writer.write(gson.toJson(config));
                writer.close();
            }


        } catch (IOException exception) {
            EnhancedMotd.LOGGER.error("Failed to load or create config file", exception);
        }

        return config;
    }
}
