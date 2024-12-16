package org.marwy.charger;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.*;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.marwy.charger.ChargingStationMod.ROBOT_ENERGY;
import java.util.List;

public class ChargingStationBlockEntity extends KineticBlockEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargingStationBlockEntity.class);
    private static final float STRESS_IMPACT = 4.0f; // Влияние на стресс системы
    private static final float MIN_SPEED = 16.0f; // Минимальная скорость для работы
    private static final float BASE_CHARGE_AMOUNT = 0.5f; // Базовое количество здоровья для восстановления
    private static final float MAX_CHARGE_AMOUNT = 10.0f; // Максимальное количество здоровья для восстановления
    private static final float MAX_SPEED = 256.0f; // Максимальная скорость для расчета лечения

    private static final Identifier ORIGIN_LAYER_ID = new Identifier("origins", "origin");
    private static final Identifier ROBOT_ORIGIN_ID = new Identifier("wads_class", "marwy");

    public ChargingStationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (world != null && !world.isClient && world.getTime() % 20 == 0) {
            float speed = Math.abs(getSpeed());
            if (speed >= MIN_SPEED) {
                chargePlayersAbove(speed);
            }
        }
    }

    private void chargePlayersAbove(float speed) {
        Box areaAbove = new Box(getPos().up());
        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, areaAbove, player -> true);
        if (players.isEmpty()) return;
        float chargeAmount = calculateChargeAmount(speed);

        for (PlayerEntity player : players) {
            Origin playerOrigin = ModComponents.ORIGIN.get(player).getOrigin(OriginLayers.getLayer(ORIGIN_LAYER_ID));
            if (playerOrigin != null && playerOrigin.getIdentifier().equals(ROBOT_ORIGIN_ID)) {
                // Заряжаем робота
                PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
                VariableIntPower variableIntPower = component.getPower(ROBOT_ENERGY);
                if (variableIntPower != null) {
                    int currentValue = variableIntPower.getValue();
                    int maxValue = variableIntPower.getMax();
                    LOGGER.warn(currentValue + "/" + maxValue);
                    if (currentValue < maxValue) {
                        int energyChargeAmount = Math.min(maxValue - currentValue, Math.max(1, (int)chargeAmount));
                        int newValue = Math.min(maxValue, currentValue + energyChargeAmount);
                        variableIntPower.setValue(newValue);
                        component.sync();
                        player.sendMessage(Text.literal(String.format("Энергия робота заряжена на %d!", newValue - currentValue)).formatted(Formatting.GREEN), true);
                    }
                    if (player.getHealth() < player.getMaxHealth()) {
                        player.heal(chargeAmount);
                        player.sendMessage(Text.literal(String.format("Вы исцелены на %.1f!", chargeAmount)).formatted(Formatting.GREEN), true);
                    }
                } else {
                    LOGGER.warn("Сила робота не найдена для игрока: " + player.getName().getString());
                }
            }
        }
    }


    private float calculateChargeAmount(float speed) {
        float normalizedSpeed = (speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED);
        normalizedSpeed = Math.min(1.0f, Math.max(0.0f, normalizedSpeed)); // Ограничиваем значение от 0 до 1
        return BASE_CHARGE_AMOUNT + normalizedSpeed * (MAX_CHARGE_AMOUNT - BASE_CHARGE_AMOUNT);
    }

    @Override
    public float calculateStressApplied() {
        return STRESS_IMPACT;
    }

    @Override
    public float calculateAddedStressCapacity() {
        return 0;
    }

    @Override
    public boolean addToGoggleTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Text.literal("    Зарядная станция").formatted(Formatting.WHITE));
        tooltip.add(Text.literal(String.format("Текущая скорость: %.2f рад/с", getSpeed())).formatted(Formatting.GRAY));
        tooltip.add(Text.literal(String.format("Скорость лечения: %.2f", calculateChargeAmount(Math.abs(getSpeed())))).formatted(Formatting.GRAY));
        return true;
    }
}
