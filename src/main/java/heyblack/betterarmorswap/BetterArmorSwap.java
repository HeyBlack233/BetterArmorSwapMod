package heyblack.betterarmorswap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class BetterArmorSwap
{
    public static boolean swapArmor(PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
        if (!itemStack2.isEmpty())
        {
            ClickSlotC2SPacket packet = new ClickSlotC2SPacket
            (
                user.currentScreenHandler.syncId,
                8 - equipmentSlot.getEntitySlotId(), user.inventory.selectedSlot,
                SlotActionType.SWAP,
                itemStack,
                user.currentScreenHandler.getNextActionId(user.inventory)
            );
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            onEquipStack(itemStack, user);
            return true;
        }
        return false;
    }

    public static void onEquipStack(ItemStack stack, Entity player) {
        if (stack.isEmpty()) {
            return;
        }
        SoundEvent soundEvent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            soundEvent = ((ArmorItem)item).getMaterial().getEquipSound();
        } else if (item == Items.ELYTRA) {
            soundEvent = SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA;
        }
        player.playSound(soundEvent, 1.0f, 1.0f);
    }
}
