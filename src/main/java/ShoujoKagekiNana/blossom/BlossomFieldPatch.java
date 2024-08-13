package ShoujoKagekiNana.blossom;

import ShoujoKagekiCore.shine.DisposableFieldUpgradePatch;
import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.actions.BlossomAction;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.powers.BasePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;


public class BlossomFieldPatch {

//    @SpirePatch(clz = AbstractCard.class, method = "triggerOnGlowCheck")
//    public static class MakeStatEquivalentCopy {
//        public static void Prefix(AbstractCard __instance) {
//            if (!BlossomField.blossom.get(__instance)) return;
//
//        }
//    }


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
        if (card instanceof BaseCard) {
            ((BaseCard) card).triggerBlossom();
        }
        EnergyPanel.useEnergy(1);
        BlossomField.blossomed.set(card, true);

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
