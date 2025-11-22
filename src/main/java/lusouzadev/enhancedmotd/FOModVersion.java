package lusouzadev.enhancedmotd;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FOModVersion {
    public final int major;
    public final int minor;
    public final int patch;
    public final String suffix;  // e.g., "+neoforge-1.21.x"

    public FOModVersion(@NotNull String version) {
        // Extract suffix if present (e.g., "1.0.0+neoforge-1.21.x" -> "+neoforge-1.21.x")
        String versionCore = version;
        String versionSuffix = "";

        int plusIndex = version.indexOf('+');
        if (plusIndex != -1) {
            versionCore = version.substring(0, plusIndex);
            versionSuffix = version.substring(plusIndex);
        }

        this.suffix = versionSuffix;

        // Parse semantic version (major.minor.patch)
        boolean index0 = false;
        int[] indexes = new int[2];

        for (int i = 0; i < versionCore.length(); i += 1) {
            if (versionCore.charAt(i) == '.') {
                if (!index0) {
                    indexes[0] = i;
                    index0 = true;
                } else {
                    indexes[1] = i;
                }
            }
        }

        this.major = substringToInt(versionCore, 0, indexes[0]);
        this.minor = substringToInt(versionCore, indexes[0] + 1, indexes[1]);
        this.patch = substringToInt(versionCore, indexes[1] + 1, versionCore.length());
    }

    private static int substringToInt(@NotNull String str, int index1, int index2) {
        return Integer.parseInt(
            str.substring(index1, index2)
        );
    }
    
    public String toString() {
        return major + "." + minor + "." + patch + suffix;
    }

    @Contract("_ -> new")
    public static @NotNull FOModVersion fromString(String version) {
        return new FOModVersion(version);
    }
}
