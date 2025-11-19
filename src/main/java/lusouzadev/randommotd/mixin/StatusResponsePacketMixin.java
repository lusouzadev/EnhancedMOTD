package lusouzadev.randommotd.mixin;

import lusouzadev.randommotd.RandomMotd;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

/**
 * Mixin to intercept status requests and modify the ServerStatus before it's sent to the client.
 * This targets the ServerStatusPacketListenerImpl.handleStatusRequest method where it sends the response packet.
 */
@Mixin(ServerStatusPacketListenerImpl.class)
public class StatusResponsePacketMixin {

    @Shadow
    @Final
    private ServerStatus status;

    @ModifyArg(
        method = "handleStatusRequest",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"),
        index = 0
    )
    private net.minecraft.network.protocol.Packet<?> modifyStatusPacket(net.minecraft.network.protocol.Packet<?> packet) {
        try {
            if (packet instanceof ClientboundStatusResponsePacket) {
                RandomMotd.LOGGER.info("StatusResponsePacketMixin is being applied!");

                // Create a modified status with random MOTD
                ServerStatus newStatus = new ServerStatus(
                    RandomMotd.getRandomMotd(),                                                     // description
                    this.status.players(),                                                          // players
                    this.status.version(),                                                          // version
                    RandomMotd.useRandomIcons() ? Optional.of(RandomMotd.getRandomIcon()) : this.status.favicon(),   // favicon
                    this.status.enforcesSecureChat()                                               // enforcesSecureChat
                );

                RandomMotd.LOGGER.info("Successfully modified server status with random MOTD");
                return new ClientboundStatusResponsePacket(newStatus);
            }
        } catch (Throwable e) {
            RandomMotd.LOGGER.error("Failed to modify server status packet", e);
        }
        return packet;
    }
}
