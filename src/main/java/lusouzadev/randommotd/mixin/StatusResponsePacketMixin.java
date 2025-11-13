package lusouzadev.randommotd.mixin;

import lusouzadev.randommotd.RandomMotd;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Mixin to intercept status response packets and replace MOTD/icon with random values.
 * This is the NeoForge/Forge equivalent of QueryResponseS2CPacketMixin from Fabric.
 */
@Mixin(ClientboundStatusResponsePacket.class)
public class StatusResponsePacketMixin {
    @Shadow
    @Final
    @Mutable
    private ServerStatus status;

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/network/protocol/status/ServerStatus;)V")
    private void onInit(ServerStatus status, CallbackInfo ci) {
        try {
            // In 1.20.1, ServerStatus constructor has 6 parameters
            ServerStatus newStatus = new ServerStatus(
                RandomMotd.getRandomMotd(),                                                    // description
                status.players(),                                                              // players
                status.version(),                                                              // version
                RandomMotd.useRandomIcons() ? Optional.of(RandomMotd.getRandomIcon()) : status.favicon(), // favicon
                status.enforcesSecureChat(),                                                   // enforcesSecureChat
                Optional.empty()                                                               // forgeData (empty for now)
            );

            this.status = newStatus;
        } catch (Throwable e) {
            RandomMotd.LOGGER.error("Failed to modify server status packet", e);
        }
    }
}
