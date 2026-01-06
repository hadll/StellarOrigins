package haddle.stellarorigins.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerStarpowerComponent implements StarpowerComponent {
    private double starpower = 0.0; // random initial value because why not
    private final PlayerEntity player;
    private double maximum_starpower = 100.0;

    public PlayerStarpowerComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override public double getValue() { return this.starpower; }
    @Override public void change(double amount) {
        this.starpower = Math.min(Math.max(amount + this.starpower, 0.0),maximum_starpower);
    }
    @Override public void readFromNbt(NbtCompound tag) { this.starpower = tag.getDouble("starpower"); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putDouble("starpower", this.starpower); }
}
