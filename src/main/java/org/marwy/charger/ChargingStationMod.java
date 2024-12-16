package org.marwy.charger;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.VariableIntPower;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ChargingStationMod implements ModInitializer {
    public static final String MOD_ID = "charger";
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    
    public static final PowerTypeReference<VariableIntPower> ROBOT_ENERGY = 
        new PowerTypeReference<>(new Identifier("wads_class", "marwy/robot_energy"));

    @Override
    public void onInitialize() {
        ModBlocks.register();
        // Другая инициализация, если необходимо
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static Identifier asResource(String path) {
        return new Identifier(MOD_ID, path);
    }
}