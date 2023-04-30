package heyblack.betterarmorswap.mixin;

import heyblack.betterarmorswap.BetterArmorSwap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ArmorItem.class)
public class ArmorItemMixin
{
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void swap(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
    {
        if (BetterArmorSwap.swapArmor(user, hand))
        {
            ItemStack itemStack = user.getStackInHand(hand);
            cir.setReturnValue(TypedActionResult.success(itemStack, world.isClient()));
        }
    }
}
