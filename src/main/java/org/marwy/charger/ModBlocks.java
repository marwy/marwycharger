package org.marwy.charger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.MapColor;
import net.minecraft.item.Item;

public class ModBlocks {

    private static final CreateRegistrate REGISTRATE = ChargingStationMod.registrate();

    public static final BlockEntry<ChargingStationBlock> CHARGING_STATION = REGISTRATE.block("charging_station", ChargingStationBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.IRON_GRAY).strength(4.0f, 6.0f).requiresTool())
            .simpleBlockEntity(ChargingStationBlockEntity::new)
            .item()
            .build()
            .register();

    public static final BlockEntityEntry<ChargingStationBlockEntity> CHARGING_STATION_BLOCK_ENTITY = REGISTRATE
            .blockEntity("charging_station", ChargingStationBlockEntity::new)
            .validBlocks(CHARGING_STATION)
            .register();

    public static void register() {
        // Убедимся, что регистрация выполняется
        REGISTRATE.register();
    }
}
