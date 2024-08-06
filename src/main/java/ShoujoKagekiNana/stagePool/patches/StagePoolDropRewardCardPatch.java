package ShoujoKagekiNana.stagePool.patches;

import ShoujoKagekiCore.base.BasePlayer;
import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.auditionEnergy.patches.AuditionEnergyPatch;
import ShoujoKagekiNana.cards.EmptyStage;
import ShoujoKagekiNana.cards.starter.Strike;
import ShoujoKagekiNana.charactor.NanaCharacter;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.Merchant;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class StagePoolDropRewardCardPatch {
//    @SpirePatch2(
//            clz = AbstractDungeon.class,
//            method = "getRewardCards"
//    )
//    public static class _Patch {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                @Override
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().equals(AbstractDungeon.class.getName()) && m.getMethodName().equals("getCard")) {
//                        m.replace(String.format("$_ = %s.getCard($1, retVal);", _Patch.class.getName()));
//                    }
//                }
//            };
//        }
//    }

    private static AbstractCard getCard(AbstractCard.CardRarity rarity, ArrayList<AbstractCard> retVal, boolean useRng) {
        ArrayList<AbstractCard> pool = null;
        switch (rarity) {
            case COMMON:
                pool = filteredPool(retVal, commonCardPool);
                break;
            case UNCOMMON:
                pool = filteredPool(retVal, uncommonCardPool);
                break;
            case RARE:
                pool = filteredPool(retVal, rareCardPool);
                break;
            case CURSE:
                pool = filteredPool(retVal, curseCardPool);
                break;
            default:
                return null;
        }
        if (pool.isEmpty()) {
            return new EmptyStage();
        }
        if (useRng) {
            return pool.get(AbstractDungeon.cardRng.random(pool.size() - 1));
        }
        return pool.get(MathUtils.random(pool.size() - 1));
    }

    private static ArrayList<AbstractCard> filteredPool(ArrayList<AbstractCard> retVal, CardGroup pool) {
        if (retVal == null || retVal.isEmpty()) return pool.group;
        ArrayList<AbstractCard> result = new ArrayList<>();
        for (AbstractCard card : pool.group) {
            if (retVal.stream().anyMatch(c -> c.cardID.equals(card.cardID))) {
                continue;
            }
            result.add(card);
        }
        return result;
    }


    // copy then change to stagePool
    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class _Patch2 {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<ArrayList<AbstractCard>> Insert(int ___numCards) {
            if (!(player instanceof NanaCharacter)) {
                return SpireReturn.Continue();
            }
            ArrayList<AbstractCard> result = new ArrayList<>();
            ArrayList<AbstractCard> resultTmp = new ArrayList<>();

            for (int i = 0; i < ___numCards; i++) {
                AbstractCard.CardRarity rarity = rollRarity();
                switch (rarity) {
                    case COMMON:
                        cardBlizzRandomizer -= cardBlizzGrowth;
                        if (cardBlizzRandomizer <= cardBlizzMaxOffset) {
                            cardBlizzRandomizer = cardBlizzMaxOffset;
                        }
                    case UNCOMMON:
                        break;
                    case RARE:
                        cardBlizzRandomizer = cardBlizzStartOffset;
                        break;
                    default:
                        Log.logger.info("WTF?");
                }

                if (player.hasRelic("PrismaticShard")) {
                    resultTmp.add(CardLibrary.getAnyColorCard(rarity));
                } else {
                    resultTmp.add(getCard(rarity, resultTmp, true));
                }
            }

            for (AbstractCard card : resultTmp) {
                result.add(card.makeStatEquivalentCopy()); // pass modifier
            }

            for (AbstractCard card : result) {
                float cardUpgradedChance = ReflectionHacks.getPrivateStatic(AbstractDungeon.class, "cardUpgradedChance");
                if (card.rarity != AbstractCard.CardRarity.RARE && cardRng.randomBoolean(cardUpgradedChance) && card.canUpgrade()) {
                    card.upgrade();
                } else {
                    for (AbstractRelic r : player.relics) {
                        r.onPreviewObtainCard(card);
                    }

                }
            }
            return SpireReturn.Return(result);
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher m = new Matcher.MethodCallMatcher(AbstractDungeon.class, "rollRarity");
            return LineFinder.findInOrder(ctBehavior, m);
        }
    }


    private static AbstractCard getCard(AbstractCard.CardRarity rarity, AbstractCard.CardType cardType, boolean useRng) {
        ArrayList<AbstractCard> pool = null;
        switch (rarity) {
            case COMMON:
                pool = filteredPool(cardType, commonCardPool);
                break;
            case UNCOMMON:
                pool = filteredPool(cardType, uncommonCardPool);
                break;
            case RARE:
                pool = filteredPool(cardType, rareCardPool);
                break;
            case CURSE:
                pool = filteredPool(cardType, curseCardPool);
                break;
            default:
                return null;
        }
        if (pool.isEmpty()) {
            return null;
        }
        if (useRng) {
            return pool.get(AbstractDungeon.cardRng.random(pool.size() - 1));
        }
        return pool.get(MathUtils.random(pool.size() - 1));
    }

    private static ArrayList<AbstractCard> filteredPool(AbstractCard.CardType cardType, CardGroup pool) {
        ArrayList<AbstractCard> result = new ArrayList<>();
        for (AbstractCard card : pool.group) {
            if (card.type != cardType) {
                continue;
            }
            result.add(card);
        }
        return result;
    }


    // copy from AbstractDungeon.getCardFromPool
    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "getCardFromPool"
    )
    public static class _Patch3 {
        public static SpireReturn<AbstractCard> Prefix(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean useRng) {
            AbstractCard retVal = getCard(rarity, type, useRng);

            switch (rarity) {
                case COMMON:
                    if (type == AbstractCard.CardType.POWER && retVal == null)
                        retVal = getCard(AbstractCard.CardRarity.UNCOMMON, type, useRng);
                case UNCOMMON:
                    if (type == AbstractCard.CardType.POWER && retVal == null)
                        retVal = getCard(AbstractCard.CardRarity.RARE, type, useRng);
                    break;
            }

            if (retVal != null) {
                return SpireReturn.Return(retVal);
            }
            Log.logger.info("ERROR: Could not find {} card of type: {} use", rarity, type);
            return SpireReturn.Return(new EmptyStage());
        }
    }


    // ignore curse for Merchant
    @SpirePatch2(
            clz = Merchant.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {float.class, float.class, int.class}
    )
    public static class _Patch4 {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(Objects.class.getName()) && m.getMethodName().equals("equals")) {
                        m.replace(String.format("$_ = %s._equals($1, $2);", _Patch4.class.getName()));
                    }
                }
            };
        }

        public static boolean _equals(Object a, Object b) {
            if (a.equals(EmptyStage.ID)) return false;
            return Objects.equals(a, b);
        }
    }
}
