package heyblack.betterarmorswap.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ElytraItem.class)
public class ElytraItemMixin
{
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void swap(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        System.out.println("injected");
        ItemStack itemStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
        if(!EnchantmentHelper.hasBindingCurse(itemStack2) && !ItemStack.areEqual(itemStack, itemStack2)){
            System.out.println("swap");
            user.equipStack(equipmentSlot, itemStack.copy());
            if (itemStack2.isEmpty())
                itemStack.setCount(0);
            user.setStackInHand(hand, itemStack2.copy());
            cir.setReturnValue(TypedActionResult.success(itemStack, world.isClient()));
        } else
            cir.setReturnValue(TypedActionResult.fail(itemStack));
    }
}
