package ShoujoKagekiNana.blossom;

import ShoujoKagekiCore.base.BasePlayer;
import ShoujoKagekiNana.actions.BlossomAction;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.charactor.NanaCharacter;
import ShoujoKagekiNana.powers.BasePower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

import java.util.function.Consumer;


public class BlossomFieldPatch {

    private static final Color BlossomGlowColor = new Color(1f, 0.5f, 0f, 0.25f);
    private static final Color BlossomGlowColor_CANNOTUSE = new Color(1f, 0.5f, 0f, 0.1f);

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
                } else {
                    if (card.glowColor.equals(BlossomGlowColor) || card.glowColor.equals(BlossomGlowColor_CANNOTUSE)) {
                        card.glowColor = ReflectionHacks.getPrivateStatic(AbstractCard.class, "BLUE_BORDER_GLOW_COLOR");
                    }
                }
            }
        }
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
        if (EnergyPanel.getCurrentEnergy() < 1) return false;
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
        EnergyPanel.useEnergy(1);
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof BasePower) {
                ((BasePower) power).triggerOnBlossom();
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
