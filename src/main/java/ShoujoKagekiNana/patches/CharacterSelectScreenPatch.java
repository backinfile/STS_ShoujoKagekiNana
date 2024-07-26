package ShoujoKagekiNana.patches;

import ShoujoKagekiNana.charactor.NanaCharacter;
import ShoujoKagekiNana.stances.patches.StancePatches;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.stances.NeutralStance;

public class CharacterSelectScreenPatch {
    private static final float START_X = Settings.WIDTH + 600 * Settings.xScale;
    private static final float DEST_X = Settings.isMobile ? 160.0F * Settings.scale : 200.0F * Settings.scale + Settings.WIDTH * 0.55f;

    @SpirePatch2(
            clz = CharacterOption.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, AbstractPlayer.class, Texture.class, Texture.class}
    )
    public static class _InitPatch {
        public static void Postfix(CharacterOption __instance) {
            if (__instance.c instanceof NanaCharacter) {
                ReflectionHacks.setPrivate(__instance, __instance.getClass(), "infoX", START_X);
            }
        }
    }

    @SpirePatch2(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {boolean.class}
    )
    public static class _DiePatch {
        public static void Postfix(AbstractMonster __instance) {
            StancePatches.Field.stance.set(__instance, new NeutralStance());
        }
    }

    @SpirePatch2(
            clz = CharacterOption.class,
            method = "updateInfoPosition"
    )
    public static class Patch {

        public static SpireReturn<Void> Prefix(CharacterOption __instance) {
            if (!(__instance.c instanceof NanaCharacter)) {
                return SpireReturn.Continue();
            }

            float infoX = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "infoX");
            if (__instance.selected) {
                infoX = MathHelper.uiLerpSnap(infoX, DEST_X);
            } else {
                infoX = MathHelper.uiLerpSnap(infoX, START_X);
            }
            ReflectionHacks.setPrivate(__instance, __instance.getClass(), "infoX", infoX);
            return SpireReturn.Return();
        }
    }
}
