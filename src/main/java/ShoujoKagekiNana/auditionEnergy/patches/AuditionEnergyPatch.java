package ShoujoKagekiNana.auditionEnergy.patches;

import ShoujoKagekiNana.actions.GainAuditionEnergyAction;
import ShoujoKagekiNana.auditionEnergy.AuditionEnergy;
import ShoujoKagekiNana.auditionEnergy.DoubleEnergyOrb;
import ShoujoKagekiNana.charactor.NanaCharacter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class AuditionEnergyPatch {

    @SpirePatch2(
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class UseReserves {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(EnergyManager.class.getName()) && m.getMethodName().equals("use")) {
                        m.replace(String.format("%s.use(c, $1);", UseReserves.class.getName()));
                    }

                }
            };
        }

        public static void use(AbstractCard c, int use) {
            if (AuditionEnergy.checkCanUseEnergy(c) && AuditionEnergy.count() > 0 && c.costForTurn > 0) {
                int used = Math.min(AuditionEnergy.count(), c.costForTurn);
                AbstractDungeon.actionManager.addToTop(new GainAuditionEnergyAction(-used));
                int left = c.costForTurn - used;
                AbstractDungeon.player.energy.use(left);
            } else {
                AbstractDungeon.player.energy.use(use);
            }
        }
//        @SpireInsertPatch(
//                locator = Locator.class
//        )
//        public static SpireReturn spendReserves(AbstractPlayer __instance, AbstractCard c) {
//            if (AuditionEnergy.count() > 0 && c.costForTurn > 0) {
//
//                // TODO noly cost double energy
////                if (c.cardID.equals(FingerOfDeath.ID)) {
////                    AbstractDungeon.actionManager.addToTop(new GainAuditionEnergyAction(-c.costForTurn));
////                    return SpireReturn.Return();
////                }
//
//                int delta = c.costForTurn - EnergyPanel.getCurrentEnergy();
//                if (delta > 0) {
//                    AbstractDungeon.actionManager.addToTop(new GainAuditionEnergyAction(-delta));
//                }
//            }
//
//            return SpireReturn.Continue();
//        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(EnergyManager.class, "use");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(
            clz = AbstractCard.class,
            method = "hasEnoughEnergy"
    )
    public static class CardCostPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<?> bePlayable(AbstractCard __instance) {
            if (!AuditionEnergy.checkCanUseEnergy(__instance)) return SpireReturn.Continue();

            int found = AuditionEnergy.count();
            // TODO only cost double enengy
//            if (__instance.cardID.equals(FingerOfDeath.ID)) {
//                return found < __instance.costForTurn && !__instance.freeToPlay() && !__instance.ignoreEnergyOnUse ? SpireReturn.Return(false) : SpireReturn.Return(true);
//            }
            return found > 0 && EnergyPanel.totalCount + found >= __instance.costForTurn ? SpireReturn.Return(true) : SpireReturn.Continue();

        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(EnergyPanel.class, "totalCount");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }


    // ======================== render


    @SpirePatch(
            clz = EnergyPanel.class,
            method = "render"
    )
    public static class ShowTextForReserves {
        public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
            int found = AuditionEnergy.count();
            if (AbstractDungeon.player.chosenClass.equals(NanaCharacter.Enums.ShoujoKageki_Nana) || found > 0) {
                String toShow;
                if (found > 0) {
                    toShow = String.valueOf(found);
                } else {
                    toShow = "0";
                }

                AbstractDungeon.player.getEnergyNumFont().getData().setScale(1.0F);
                FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), toShow, __instance.current_x + DoubleEnergyOrb.X_OFFSET, __instance.current_y + DoubleEnergyOrb.Y_OFFSET, Color.WHITE.cpy());
            }

        }
    }


    @SpirePatch(
            clz = EnergyPanel.class,
            method = "renderVfx"
    )
    public static class FlashSecondOrbPatch {

        @SpirePrefixPatch
        public static void flashSecondOrb(EnergyPanel __instance, SpriteBatch sb, Texture ___gainEnergyImg, Color ___energyVfxColor, float ___energyVfxScale, float ___energyVfxAngle) {
            if (AbstractDungeon.player.chosenClass == NanaCharacter.Enums.ShoujoKageki_Nana && DoubleEnergyOrb.secondVfxTimer > 0.0F) {
                sb.setBlendFunction(770, 1);
                sb.setColor(DoubleEnergyOrb.secondEnergyVfxColor);
                sb.draw(___gainEnergyImg, __instance.current_x + DoubleEnergyOrb.X_OFFSET - 128.0F, __instance.current_y + DoubleEnergyOrb.Y_OFFSET - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, DoubleEnergyOrb.secondEnergyVfxScale * DoubleEnergyOrb.SECOND_ORB_IMG_SCALE / DoubleEnergyOrb.PRIMARY_ORB_IMG_SCALE, DoubleEnergyOrb.secondEnergyVfxScale * DoubleEnergyOrb.SECOND_ORB_IMG_SCALE / DoubleEnergyOrb.PRIMARY_ORB_IMG_SCALE, DoubleEnergyOrb.secondEnergyVfxAngle - 50.0F, 0, 0, 256, 256, true, false);
                sb.draw(___gainEnergyImg, __instance.current_x + DoubleEnergyOrb.X_OFFSET - 128.0F, __instance.current_y + DoubleEnergyOrb.Y_OFFSET - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, DoubleEnergyOrb.secondEnergyVfxScale * DoubleEnergyOrb.SECOND_ORB_IMG_SCALE / DoubleEnergyOrb.PRIMARY_ORB_IMG_SCALE, DoubleEnergyOrb.secondEnergyVfxScale * DoubleEnergyOrb.SECOND_ORB_IMG_SCALE / DoubleEnergyOrb.PRIMARY_ORB_IMG_SCALE, -DoubleEnergyOrb.secondEnergyVfxAngle, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }

        }
    }


    // ======================== color


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    public static class SpecialColor {
        private static final Color SPENDING_RESERVES_COLOR = new Color(1.0F, 0.8F, 0.5F, 1.0F);

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"costColor"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef Color[] costColor) {
            if (__instance.costForTurn > EnergyPanel.totalCount && __instance.costForTurn <= EnergyPanel.totalCount + AuditionEnergy.count() && !__instance.freeToPlay()) {
                costColor[0] = SPENDING_RESERVES_COLOR.cpy();
            }

        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "transparency");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    // ======================== xCost


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class ReservesWithXCosts {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (c.cost == -1) {
                int effect = c.energyOnUse;
                int count = AuditionEnergy.count();
                if (count > 0) {
                    effect += count;
                    if (!c.freeToPlayOnce) {
                        AbstractDungeon.actionManager.addToTop(new GainAuditionEnergyAction(-count));
                    }
                }

                c.energyOnUse = effect;
            }

        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

}
