package lusouzadev.enhancedmotd;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(EnhancedMotd.MODID)
public class EnhancedMotd {
	public static final String MODID = "enhanced_motd";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static FOModVersion VERSION;

	private static Config CONFIG;
	private static final Random random = new Random();

	private static String[][] motds;
	private static ServerStatus.Favicon[] icons;

	public EnhancedMotd(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
		// Get a version from mod metadata
		VERSION = FOModVersion.fromString(modContainer.getModInfo().getVersion().toString());

		// Register the setup method for mod loading
		modEventBus.addListener(this::setup);

		// Register our event handler to the NeoForge event bus
		NeoForge.EVENT_BUS.register(new ServerEventHandler());

		LOGGER.info("EnhancedMOTD version: {}", VERSION);
	}

	private void setup(final FMLCommonSetupEvent ignoredEvent) {
		LOGGER.info("EnhancedMOTD initializing...");
		loadConfigFromFile();
	}

	public static Component getEnhancedMotd() {
		return getRandomMotd(random);
	}

	public static Component getRandomMotd(Random random) {
		StringBuilder s = new StringBuilder();

		for (String[] w : motds) {
			s.append(w[random.nextInt(w.length)]);
		}

		return TextFormatter.formatText(s.toString());
	}

	public static ServerStatus.Favicon getRandomIcon() {
		return getRandomIcon(random);
	}

	public static ServerStatus.Favicon getRandomIcon(@NotNull Random random) {
		LOGGER.info("Getting random icon");
		return icons[random.nextInt(icons.length)];
	}

	public static boolean useRandomIcons() {
		return icons.length > 0;
	}

	public static void log(String output) {
		LOGGER.info(output);
	}

	public static void loadConfigFromFile() {
		CONFIG = Config.init();
		if (CONFIG.log_when_loaded)
			log("Loaded config for " + MODID);

		cacheMotds();

		if (CONFIG.use_randomized_icons) {
			cacheIcons();
		} else {
			icons = new ServerStatus.Favicon[]{};
		}
	}

	private static void cacheMotds() {
		motds = CONFIG.motds.stream()
				.map(l -> l.toArray(String[]::new))
				.toArray(String[][]::new);
	}

	private static void cacheIcons() {
		ArrayList<ServerStatus.Favicon> list = new ArrayList<>();

		for (String iconPath : CONFIG.icons) {
			try {
				BufferedImage bufferedImage = null;

				// Check if the iconPath is an HTTPS URL
				if (iconPath.toLowerCase().startsWith("https://")) {
					bufferedImage = loadIconFromUrl(iconPath);
				} else if (iconPath.toLowerCase().startsWith("http://")) {
					LOGGER.warn("Icon `{}` uses insecure HTTP protocol. Please use HTTPS instead. Skipping.", iconPath);
					continue;
				} else {
					// Handle as a file path
					Optional<File> optional = Optional.of(new File(".", iconPath)).filter(File::isFile);

					if (optional.isEmpty()) {
						LOGGER.info("Icon `{}` does not exist!", iconPath);
						continue;
					}

					bufferedImage = ImageIO.read(optional.get());
				}

				if (bufferedImage != null) {
					Preconditions.checkState(bufferedImage.getWidth() == 64, "Must be 64 pixels wide");
					Preconditions.checkState(bufferedImage.getHeight() == 64, "Must be 64 pixels high");
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ImageIO.write((RenderedImage) bufferedImage, "PNG", byteArrayOutputStream);
					list.add(new ServerStatus.Favicon(byteArrayOutputStream.toByteArray()));
				}
			} catch (Exception exception) {
				LOGGER.error("Couldn't load server icon from `{}`", iconPath, exception);
			}
		}

		icons = list.toArray(ServerStatus.Favicon[]::new);
	}

	private static BufferedImage loadIconFromUrl(String urlString) throws Exception {
		URI uri = new URI(urlString);

		// Ensure the protocol is HTTPS
		if (!"https".equalsIgnoreCase(uri.getScheme())) {
			throw new IllegalArgumentException("Only HTTPS URLs are allowed for security reasons");
		}

		URL url = uri.toURL();
		try (InputStream inputStream = url.openStream()) {
			return ImageIO.read(inputStream);
		}
	}
}
