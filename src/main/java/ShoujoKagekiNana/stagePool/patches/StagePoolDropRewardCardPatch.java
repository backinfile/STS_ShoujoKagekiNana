package ShoujoKagekiNana.stagePool.patches;

import ShoujoKagekiCore.base.BasePlayer;
import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.auditionEnergy.patches.AuditionEnergyPatch;
import ShoujoKagekiNana.cards.EmptyStage;
import ShoujoKagekiNana.cards.starter.Strike;
import ShoujoKagekiNana.charactor.NanaCharacter;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
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

    private static AbstractCard getCard(AbstractCard.CardRarity rarity, ArrayList<AbstractCard> retVal) {
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
        return pool.get(AbstractDungeon.cardRng.random(pool.size() - 1));
    }

    private static ArrayList<AbstractCard> filteredPool(ArrayList<AbstractCard> retVal, CardGroup pool) {
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
                    resultTmp.add(getCard(rarity, resultTmp));
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
}
