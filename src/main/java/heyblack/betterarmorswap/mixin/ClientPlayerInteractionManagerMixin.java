package heyblack.betterarmorswap.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin
{
    @Shadow public abstract ItemStack clickSlot(int syncId, int slotId, int clickData, SlotActionType actionType, PlayerEntity player);

    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    public void swap(int syncId, int slotId, int clickData, SlotActionType actionType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir)
    {
        if (slotId >= 0)
        {
            Slot slot = player.currentScreenHandler.getSlot(slotId);
            ItemStack itemStack = slot.getStack();
            if (actionType == SlotActionType.QUICK_MOVE && itemStack.getItem() instanceof Wearable)
            {
                int equipmentSlotId = 8 - MobEntity.getPreferredEquipmentSlot(itemStack).getEntitySlotId();
                if (slotId != equipmentSlotId && clickData == 1 && player.currentScreenHandler.getSlot(equipmentSlotId).getStack().getItem() instanceof Wearable)
                {
                    clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, player);
                    clickSlot(syncId, equipmentSlotId, 0, SlotActionType.PICKUP, player);
                    clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, player);
                    cir.cancel();
                }
            }
        }
    }
}
