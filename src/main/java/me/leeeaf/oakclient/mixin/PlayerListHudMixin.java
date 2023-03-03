package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.systems.social.Relationship;
import me.leeeaf.oakclient.systems.social.SocialManager;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Inject(method = "applyGameModeFormatting", at=@At("HEAD"))
    void formatNamesByRelationship(PlayerListEntry entry, MutableText name, CallbackInfoReturnable<Text> cir){
        Relationship relationship = SocialManager.getRelationship(name.getString());
        if(relationship != null) name.formatted(relationship.getColor());
    }
}
