package ShoujoKagekiNana.patches;

import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.auditionEnergy.AuditionEnergy;
import ShoujoKagekiNana.auditionEnergy.patches.AuditionEnergyPatch;
import ShoujoKagekiNana.cards.BaseCard;
import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CNCardTextColors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class TextColorPatch {

    //
//    @SpirePatch2(
//            clz = CNCardTextColors.class,
//            method = "Insert"
//    )
//    public static class _Patch {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                private boolean replaced = false;
//
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (!replaced && m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("length")) {
//                        m.replace("$_ = $proceed($$) / 2;");
//                        replaced = true;
//                    }
//
//                }
//            };
//        }
//    }
//    @SpirePatch2(
//            clz = AbstractCard.class,
//            method = "initializeDescriptionCN"
//    )
//    public static class _Patch {
//        @SpireInsertPatch(locator = Locator.class)
//        public static void Insert(AbstractCard __instance, @ByRef String[] ___word, @ByRef float[] ___currentWidth, @ByRef int[] ___numLines) {
//            if (___word[0].startsWith("$")) {
//                String wordTrim = ___word[0].substring(1);
//                GlyphLayout gl = ReflectionHacks.getPrivateStatic(AbstractCard.class, "gl");
//                float CN_DESC_BOX_WIDTH = ReflectionHacks.getPrivateStatic(AbstractCard.class, "CN_DESC_BOX_WIDTH");
//                StringBuilder sbuilder = ReflectionHacks.getPrivateStatic(AbstractCard.class, "sbuilder");
//                gl.setText(FontHelper.cardDescFont_N, wordTrim);
//                sbuilder.append(wordTrim);
//                ___currentWidth[0] += gl.width;
//                ___word[0] = "";
//            }
//        }
//
//        public static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctBehavior) throws Exception {
//                Matcher m = new Matcher.MethodCallMatcher(String.class, "trim");
//                int[] allInOrder = LineFinder.findAllInOrder(ctBehavior, m);
//                return new int[]{allInOrder[allInOrder.length - 1]};
//            }
//        }
//    }
//
    @SpirePatch2(
            clz = AbstractCard.class,
            method = "renderDescriptionCN"
    )
    public static class _Patch2 {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getLineNumber() != 2451) return;
                    if (f.getClassName().equals(AbstractCard.class.getName()) && f.getFieldName().equals("textColor")) {
                        f.replace(String.format("$_ = %s._replaceColor(this, i, $proceed());", _Patch2.class.getName()));
//                        Log.logger.info("============ replaced");
                    }
                }
            };
        }

        public static Color _replaceColor(AbstractCard card, int index, Color textColor) {
            if (!(card instanceof BaseCard)) return textColor;
            if (!((BaseCard) card).enableGrayText) return textColor;
            int grayTextStartIndex = ((BaseCard) card).grayTextStartIndex;
            if (grayTextStartIndex < 0) return textColor;
            int grayTextEndIndex = ((BaseCard) card).grayTextEndIndex;
            if (grayTextStartIndex <= index && index <= grayTextEndIndex) {
                return Color.GRAY;
            }
//            Log.logger.info("========= index={}", index);
            return textColor;
        }
    }


//    @SpirePatch2(
//            clz = AbstractCard.class,
//            method = "renderDescriptionCN"
//    )
//    public static class _Patch3 {
//        @SpireInsertPatch(locator = Locator.class)
//        public static void Insert(AbstractCard __instance, int ___var9, Color ___textColor) {
//            if (__instance instanceof BaseCard) {
//                int grayTextStartIndex = ((BaseCard) __instance).grayTextStartIndex;
//                int grayTextEndIndex = ((BaseCard) __instance).grayTextEndIndex;
//                if (grayTextStartIndex >= 0 && grayTextStartIndex <= ___var9 && ___var9 <= grayTextEndIndex) {
//                    ___textColor.r = Settings.CREAM_COLOR.b;
//                    ___textColor.g = Settings.CREAM_COLOR.b;
//                    ___textColor.b = Settings.CREAM_COLOR.b;
//                }
//            }
//        }
//
//        public static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctBehavior) throws Exception {
//                Matcher m = new Matcher.FieldAccessMatcher(GlyphLayout.class, "width");
//                int[] allInOrder = LineFinder.findAllInOrder(ctBehavior, m);
//                return new int[]{allInOrder[allInOrder.length - 1]};
//            }
//        }
//    }
}
