package ShoujoKagekiNana.blossom;

import ShoujoKagekiCore.base.BasePlayer;
import ShoujoKagekiNana.actions.BlossomAction;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.powers.BananicePower;
import ShoujoKagekiNana.powers.BasePower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.function.Consumer;


public class BlossomFieldPatch {

    private static final Color BlossomGlowColor = new Color(1f, 0.5f, 0f, 0.25f);
    private static final Color BlossomGlowColor_CANNOTUSE = new Color(1f, 0.5f, 0f, 0.1f);

    public static final int DEFAULT_BLOSSOM_COST = 1;

    @SpirePatch(clz = CardGroup.class, method = "glowCheck")
    public static class _Patch {
        public static void Postfix(CardGroup __instance) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) return;
            if (!(AbstractDungeon.player instanceof BasePlayer)) return;

            for (AbstractCard card : __instance.group) {
                if (!BlossomField.isBlossomCard(card)) continue;
                if (BlossomFieldPatch.canTriggerBlossom(card)) {
                    if (card.isGlowing) {
                        card.glowColor = BlossomGlowColor.cpy();
                    } else {
                        card.glowColor = BlossomGlowColor_CANNOTUSE.cpy();
                        card.beginGlowing();
                    }
                    _GlowFlagField.blossomGlowFlag.set(card, true);
                } else {
//                    if (card.glowColor.equals(BlossomGlowColor) || card.glowColor.equals(BlossomGlowColor_CANNOTUSE)) {
//                        card.glowColor = ReflectionHacks.getPrivateStatic(AbstractCard.class, "BLUE_BORDER_GLOW_COLOR");
//                    }
                    if (_GlowFlagField.blossomGlowFlag.get(card)) {
                        card.glowColor = ReflectionHacks.getPrivateStatic(AbstractCard.class, "BLUE_BORDER_GLOW_COLOR");
                        _GlowFlagField.blossomGlowFlag.set(card, false);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "<class>"
    )
    public static class _GlowFlagField {
        public static SpireField<Boolean> blossomGlowFlag = new SpireField<>(() -> false);
    }


    @SpirePatch2(
            clz = AbstractCard.class,
            method = "update"
    )
    public static class Update {
        public static void Postfix(AbstractCard __instance) {
            if (__instance.hb.hovered && InputHelper.justClickedRight
                    && !AbstractDungeon.isScreenUp
                    && __instance == AbstractDungeon.player.hoveredCard
            ) {
                if (BlossomFieldPatch.canTriggerBlossom(__instance)) {
                    __instance.superFlash();
                    AbstractDungeon.actionManager.addToBottom(new BlossomAction(__instance));
                } else if (BlossomField.blossom.get(__instance)) {
                    __instance.flash();
                }
            }
        }
    }

    public static boolean canTriggerBlossom(AbstractCard card) {
        if (!BlossomField.blossom.get(card)) return false;
        if (BlossomField.blossomed.get(card)) return false;

        if (!AbstractDungeon.player.hasPower(BananicePower.POWER_ID)) { // ignore blossom cost
            if (EnergyPanel.getCurrentEnergy() < DEFAULT_BLOSSOM_COST) return false;
        }

        return true;
    }

    public static void triggerBlossom(AbstractCard card) {
        if (!BlossomField.blossom.get(card)) return;
        if (BlossomField.blossomed.get(card)) return;

        // before
        BlossomField.blossomed.set(card, true);

        // trigger
        Consumer<AbstractCard> func = BlossomField.blossomFunc.get(card);
        if (func != null) {
            func.accept(card);
        }
        // replace description
        if (BlossomField.blossomFuncChangeDescDefault.get(card) && card instanceof BaseCard) {
            card.rawDescription = ((BaseCard) card).cardStrings.UPGRADE_DESCRIPTION;
            card.initializeDescription();
        }

        // afater
        if (!AbstractDungeon.player.hasPower(BananicePower.POWER_ID)) {
            EnergyPanel.useEnergy(DEFAULT_BLOSSOM_COST);
        }


        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof BasePower) {
                ((BasePower) power).triggerOnBlossom(card);
            }
        }
    }

//    @SpirePatch(
//            clz = AbstractCard.class,
//            method = "makeStatEquivalentCopy"
//    )
//    public static class MakeStatEquivalentCopy {
//        @SpireInsertPatch(locator = Locator.class, localvars = "card")
//        public static void Insert(AbstractCard __instance, AbstractCard card) {
//            BlossomField.blossomed.set(card, BlossomField.blossomed.get(__instance));
//        }
//
//        private static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
//                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "name");
//                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
//            }
//        }
//    }
}
