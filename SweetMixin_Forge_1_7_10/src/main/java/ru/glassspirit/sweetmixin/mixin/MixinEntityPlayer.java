package ru.glassspirit.sweetmixin.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.glassspirit.sweetmixin.EssenceContainer;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer implements EssenceContainer {

    /**
     * Наше новое поле в классе EntityPlayer - эссенция
     */
    private float essence;

    /**
     * Виртуальное поле, которое ссылается на реальное поле experienceLevel в классе EntityPlayer
     */
    @Shadow(remap = true)
    public int experienceLevel;

    @Override
    public float getEssence() {
        return this.essence;
    }

    /**
     * Изменяет текущее количество эссенции игрока. Если оно больше, чем максимально возможное для игрока - ставится максимальное
     */
    @Override
    public void setEssence(float value) {
        this.essence = Math.min(value, getMaxEssence());
    }

    /**
     * Максимальное количество эссенции игрока равно его уровню
     */
    @Override
    public float getMaxEssence() {
        return this.experienceLevel;
    }

    @Override
    public void setMaxEssence(float value) {
        // NOOP
        System.out.println("Максимальное количество эссенции зависит от уровня игрока!");
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void onWriteEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        compound.setFloat("Essence", this.getEssence());
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    private void onReadEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
		System.out.println("READING PLAYERDATA FROM NBT");
        setEssence(compound.getFloat("Essence"));
    }
}
