package lusouzadev.randommotd;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple text formatter to replace PlaceholderAPI functionality.
 * Supports Minecraft color codes, basic formatting, and gradients.
 */
public class TextFormatter {
    private static final Pattern COLOR_PATTERN = Pattern.compile("&([0-9a-fk-or])");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("<rb>(.*?)</rb>");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:#([0-9a-fA-F]{6}):#([0-9a-fA-F]{6})>(.*?)</gradient>");

    public static Component formatText(String text) {
        if (text == null || text.isEmpty()) {
            return Component.literal("");
        }

        // First, process gradients and rainbow
        text = processGradients(text);

        // Then convert hex colors to Minecraft format
        text = convertHexColors(text);

        // Finally convert legacy color codes
        return parseLegacyText(text);
    }

    private static String processGradients(String text) {
        // Process custom gradients first
        Matcher gradientMatcher = GRADIENT_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (gradientMatcher.find()) {
            String startHex = gradientMatcher.group(1);
            String endHex = gradientMatcher.group(2);
            String content = gradientMatcher.group(3);
            String replacement = applyGradient(content, startHex, endHex);
            gradientMatcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        gradientMatcher.appendTail(result);
        text = result.toString();

        // Process rainbow gradients
        Matcher rainbowMatcher = RAINBOW_PATTERN.matcher(text);
        result = new StringBuffer();

        while (rainbowMatcher.find()) {
            String content = rainbowMatcher.group(1);
            String replacement = applyRainbow(content);
            rainbowMatcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        rainbowMatcher.appendTail(result);

        return result.toString();
    }

    private static String applyRainbow(String text) {
        if (text.isEmpty()) return text;

        // Extract formatting codes from the beginning
        StringBuilder formats = new StringBuilder();
        String cleanText = text;

        while (cleanText.length() >= 2 && cleanText.charAt(0) == '&' &&
               "klmnor".indexOf(cleanText.charAt(1)) >= 0) {
            formats.append(cleanText.substring(0, 2));
            cleanText = cleanText.substring(2);
        }

        // Rainbow colors: Red -> Orange -> Yellow -> Green -> Blue -> Purple
        String[] rainbowColors = {"FF0000", "FF7F00", "FFFF00", "00FF00", "0000FF", "8B00FF"};

        StringBuilder result = new StringBuilder();
        int length = cleanText.length();
        int charIndex = 0; // Index for non-space characters

        for (int i = 0; i < length; i++) {
            char c = cleanText.charAt(i);
            if (c == ' ') {
                result.append(c);
                continue;
            }

            // Calculate position in rainbow (0.0 to 1.0) based on non-space characters
            float position = (float) charIndex / Math.max(1, countNonSpace(cleanText) - 1);
            int colorIndex = (int) (position * (rainbowColors.length - 1));
            float localPos = (position * (rainbowColors.length - 1)) - colorIndex;

            String color1 = rainbowColors[Math.min(colorIndex, rainbowColors.length - 1)];
            String color2 = rainbowColors[Math.min(colorIndex + 1, rainbowColors.length - 1)];

            String interpolatedColor = interpolateColor(color1, color2, localPos);
            result.append("&#").append(interpolatedColor).append(formats).append(c);
            charIndex++;
        }

        return result.toString();
    }

    private static String applyGradient(String text, String startHex, String endHex) {
        if (text.isEmpty()) return text;

        // Extract formatting codes from the beginning
        StringBuilder formats = new StringBuilder();
        String cleanText = text;

        while (cleanText.length() >= 2 && cleanText.charAt(0) == '&' &&
               "klmnor".indexOf(cleanText.charAt(1)) >= 0) {
            formats.append(cleanText.substring(0, 2));
            cleanText = cleanText.substring(2);
        }

        StringBuilder result = new StringBuilder();
        int length = cleanText.length();
        int charIndex = 0; // Index for non-space characters

        for (int i = 0; i < length; i++) {
            char c = cleanText.charAt(i);
            if (c == ' ') {
                result.append(c);
                continue;
            }

            float position = (float) charIndex / Math.max(1, countNonSpace(cleanText) - 1);
            String interpolatedColor = interpolateColor(startHex, endHex, position);
            result.append("&#").append(interpolatedColor).append(formats).append(c);
            charIndex++;
        }

        return result.toString();
    }

    private static int countNonSpace(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c != ' ') count++;
        }
        return Math.max(1, count);
    }

    private static String interpolateColor(String hex1, String hex2, float ratio) {
        int r1 = Integer.parseInt(hex1.substring(0, 2), 16);
        int g1 = Integer.parseInt(hex1.substring(2, 4), 16);
        int b1 = Integer.parseInt(hex1.substring(4, 6), 16);

        int r2 = Integer.parseInt(hex2.substring(0, 2), 16);
        int g2 = Integer.parseInt(hex2.substring(2, 4), 16);
        int b2 = Integer.parseInt(hex2.substring(4, 6), 16);

        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return String.format("%02X%02X%02X", r, g, b);
    }

    private static String convertHexColors(String text) {
        // Hex colors are now kept as-is and will be processed in parseLegacyText
        return text;
    }

    private static Component parseLegacyText(String text) {
        MutableComponent component = Component.literal("");
        StringBuilder currentText = new StringBuilder();
        Style currentStyle = Style.EMPTY;

        for (int i = 0; i < text.length(); i++) {
            // Check for hex color code &#RRGGBB
            if (i < text.length() - 8 && text.charAt(i) == '&' && text.charAt(i + 1) == '#') {
                // Flush current text
                if (currentText.length() > 0) {
                    component.append(Component.literal(currentText.toString()).setStyle(currentStyle));
                    currentText = new StringBuilder();
                }

                // Parse hex color and preserve existing formatting
                String hexColor = text.substring(i + 2, i + 8);
                try {
                    int color = Integer.parseInt(hexColor, 16);
                    // Preserve bold, italic, underlined, strikethrough, obfuscated
                    currentStyle = Style.EMPTY
                        .withColor(TextColor.fromRgb(color))
                        .withBold(currentStyle.isBold())
                        .withItalic(currentStyle.isItalic())
                        .withUnderlined(currentStyle.isUnderlined())
                        .withStrikethrough(currentStyle.isStrikethrough())
                        .withObfuscated(currentStyle.isObfuscated());
                    i += 7; // Skip the &#RRGGBB part
                } catch (NumberFormatException e) {
                    currentText.append(text.charAt(i));
                }
            }
            // Check for legacy color/format code &X
            else if (i < text.length() - 1 && text.charAt(i) == '&') {
                char code = text.charAt(i + 1);

                // Flush current text
                if (currentText.length() > 0) {
                    component.append(Component.literal(currentText.toString()).setStyle(currentStyle));
                    currentText = new StringBuilder();
                }

                currentStyle = getStyleForCode(code, currentStyle);
                i++; // Skip the code character
            } else {
                currentText.append(text.charAt(i));
            }
        }

        // Flush remaining text
        if (currentText.length() > 0) {
            component.append(Component.literal(currentText.toString()).setStyle(currentStyle));
        }

        return component;
    }

    private static Style getStyleForCode(char code, Style currentStyle) {
        switch (code) {
            // Color codes - preserve formatting
            case '0': return currentStyle.withColor(ChatFormatting.BLACK);
            case '1': return currentStyle.withColor(ChatFormatting.DARK_BLUE);
            case '2': return currentStyle.withColor(ChatFormatting.DARK_GREEN);
            case '3': return currentStyle.withColor(ChatFormatting.DARK_AQUA);
            case '4': return currentStyle.withColor(ChatFormatting.DARK_RED);
            case '5': return currentStyle.withColor(ChatFormatting.DARK_PURPLE);
            case '6': return currentStyle.withColor(ChatFormatting.GOLD);
            case '7': return currentStyle.withColor(ChatFormatting.GRAY);
            case '8': return currentStyle.withColor(ChatFormatting.DARK_GRAY);
            case '9': return currentStyle.withColor(ChatFormatting.BLUE);
            case 'a': return currentStyle.withColor(ChatFormatting.GREEN);
            case 'b': return currentStyle.withColor(ChatFormatting.AQUA);
            case 'c': return currentStyle.withColor(ChatFormatting.RED);
            case 'd': return currentStyle.withColor(ChatFormatting.LIGHT_PURPLE);
            case 'e': return currentStyle.withColor(ChatFormatting.YELLOW);
            case 'f': return currentStyle.withColor(ChatFormatting.WHITE);
            // Formatting codes - preserve color
            case 'k': return currentStyle.withObfuscated(true);
            case 'l': return currentStyle.withBold(true);
            case 'm': return currentStyle.withStrikethrough(true);
            case 'n': return currentStyle.withUnderlined(true);
            case 'o': return currentStyle.withItalic(true);
            // Reset clears everything
            case 'r': return Style.EMPTY;
            default: return currentStyle;
        }
    }
}
