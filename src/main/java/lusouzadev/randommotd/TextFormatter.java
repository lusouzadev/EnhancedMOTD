package lusouzadev.randommotd;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple text formatter to replace PlaceholderAPI functionality.
 * Supports Minecraft color codes and basic formatting.
 */
public class TextFormatter {
    private static final Pattern COLOR_PATTERN = Pattern.compile("&([0-9a-fk-or])");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");

    public static Component formatText(String text) {
        if (text == null || text.isEmpty()) {
            return Component.literal("");
        }

        // First, convert hex colors to Minecraft format
        text = convertHexColors(text);

        // Then convert legacy color codes
        return parseLegacyText(text);
    }

    private static String convertHexColors(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            // For now, we'll convert hex to the nearest standard color
            // NeoForge/Minecraft supports hex colors in JSON but not in legacy format
            String replacement = "&" + nearestColorCode(hexColor);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private static Component parseLegacyText(String text) {
        String[] parts = text.split("(&[0-9a-fk-or])");
        MutableComponent component = Component.literal("");

        if (!text.startsWith("&")) {
            // Handle text before first color code
            int firstCode = text.indexOf('&');
            if (firstCode > 0) {
                component.append(Component.literal(text.substring(0, firstCode)));
                text = text.substring(firstCode);
            } else if (firstCode == -1) {
                return Component.literal(text);
            }
        }

        // Simple parsing: split and apply formatting
        String[] segments = text.split("(?=&[0-9a-fk-or])");
        Style currentStyle = Style.EMPTY;

        for (String segment : segments) {
            if (segment.isEmpty()) continue;

            if (segment.startsWith("&") && segment.length() > 1) {
                char code = segment.charAt(1);
                String content = segment.length() > 2 ? segment.substring(2) : "";

                Style newStyle = getStyleForCode(code, currentStyle);
                if (!content.isEmpty()) {
                    component.append(Component.literal(content).setStyle(newStyle));
                }
                currentStyle = newStyle;
            } else {
                component.append(Component.literal(segment).setStyle(currentStyle));
            }
        }

        return component;
    }

    private static Style getStyleForCode(char code, Style currentStyle) {
        switch (code) {
            case '0': return Style.EMPTY.withColor(ChatFormatting.BLACK);
            case '1': return Style.EMPTY.withColor(ChatFormatting.DARK_BLUE);
            case '2': return Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
            case '3': return Style.EMPTY.withColor(ChatFormatting.DARK_AQUA);
            case '4': return Style.EMPTY.withColor(ChatFormatting.DARK_RED);
            case '5': return Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE);
            case '6': return Style.EMPTY.withColor(ChatFormatting.GOLD);
            case '7': return Style.EMPTY.withColor(ChatFormatting.GRAY);
            case '8': return Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
            case '9': return Style.EMPTY.withColor(ChatFormatting.BLUE);
            case 'a': return Style.EMPTY.withColor(ChatFormatting.GREEN);
            case 'b': return Style.EMPTY.withColor(ChatFormatting.AQUA);
            case 'c': return Style.EMPTY.withColor(ChatFormatting.RED);
            case 'd': return Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);
            case 'e': return Style.EMPTY.withColor(ChatFormatting.YELLOW);
            case 'f': return Style.EMPTY.withColor(ChatFormatting.WHITE);
            case 'k': return currentStyle.withObfuscated(true);
            case 'l': return currentStyle.withBold(true);
            case 'm': return currentStyle.withStrikethrough(true);
            case 'n': return currentStyle.withUnderlined(true);
            case 'o': return currentStyle.withItalic(true);
            case 'r': return Style.EMPTY;
            default: return currentStyle;
        }
    }

    private static String nearestColorCode(String hex) {
        // Simple mapping of hex to nearest Minecraft color
        // This is a simplified version - a full implementation would calculate color distance
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        // Simple brightness-based mapping
        int brightness = (r + g + b) / 3;

        if (brightness < 50) return "0"; // Black
        if (brightness < 100) return "8"; // Dark gray
        if (brightness < 150) return "7"; // Gray
        return "f"; // White
    }
}
