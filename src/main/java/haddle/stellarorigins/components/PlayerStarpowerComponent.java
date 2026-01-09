package haddle.stellarorigins.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.registry.SOComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerStarpowerComponent implements StarpowerComponent, AutoSyncedComponent {
    private double starpower = 0.0; // random initial value because why not
    private final PlayerEntity player;
    private double maximum_starpower = 100.0;

    public PlayerStarpowerComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override public double getValue() { return this.starpower; }
    @Override public void change(double amount) {
        this.starpower = Math.min(Math.max(amount + this.starpower, 0.0),maximum_starpower);
        SOComponents.STARPOWER.sync(player);
    }
    @Override public void readFromNbt(NbtCompound tag) { this.starpower = tag.getDouble("starpower"); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putDouble("starpower", this.starpower); }
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.player; // only sync with the provider itself
    }
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeDouble(this.starpower); // only synchronize the information you need!
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.starpower = buf.readDouble();
        StellarOrigins.STARPOWER_HELPER.updateStarpower(player);

    }

}
