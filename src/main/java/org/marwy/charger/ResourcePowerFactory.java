package org.marwy.charger;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.util.Identifier;

public class ResourcePowerFactory {
    public PowerFactory<Power> createFactory() {
        return new PowerFactory<>(
            new Identifier("wads_class", "marwy/robot_energy"),
            new SerializableData()
                .add("min", SerializableDataTypes.INT, 0)
                .add("max", SerializableDataTypes.INT, 20)
                .add("start_value", SerializableDataTypes.INT, 20),
            data -> (powerType, entity) -> new VariableIntPower(
                powerType,
                entity,
                data.getInt("start_value"),
                data.getInt("min"),
                data.getInt("max")
            )
        ).allowCondition();
    }
}