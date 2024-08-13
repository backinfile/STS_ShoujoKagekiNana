package ShoujoKagekiNana.stagePool;

import ShoujoKagekiCore.token.TokenCardField;
import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.util.Util;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

import java.util.*;
import java.util.stream.Collectors;

public class StagePoolManager {
    public static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final LinkedList<AbstractCard> cardQueueInBattle = new LinkedList<>();

    public static int stage_remove_count = 0;

    public static Random rng;


    public static void afterLoadCardPool() {
        Log.logger.info("afterLoadCardPool");
        // make copy
        ArrayList<AbstractCard> tmp = new ArrayList<>(cardPool);
        cardPool.clear();
        for (AbstractCard card : tmp) {
            cardPool.add(card.makeStatEquivalentCopy());
        }
        // set token
        for (AbstractCard card : cardPool) {
            TokenCardField.isToken.set(card, false);
        }
        Log.logger.info("load card pool count = {}", cardPool.size());
    }

    public static void initDungeon() {
        Log.logger.info("initDungeon");
        if (rng == null) {
            StagePoolManager.rng = new Random(Settings.seed);
            Log.logger.info("init rng with initDungeon");
        }

        if (CardCrawlGame.saveFile == null) {
            Log.logger.info("init cardPool with initDungeon, saveFile == null");
            cardPool.clear();
            AbstractDungeon.player.getCardPool(cardPool);
            cardPool.sort((card, card2) -> {
                int c1 = card.rarity.compareTo(card2.rarity);
                if (c1 != 0) return c1;
                int c2 = card.type.compareTo(card2.type);
                if (c2 != 0) return c2;
                int c3 = card.cost - card2.cost;
                if (c3 != 0) return c3;
                return card.cardID.compareTo(card2.cardID);
            });
            afterLoadCardPool();
        }
    }

    public static void onBattleStart() {
        refreshCardQueue();
    }

    public static void removeCardFromStage(AbstractCard remove) {
        Util.removeFirst(cardPool, c -> c.cardID.equals(remove.cardID));
        Util.removeFirst(cardQueueInBattle, c -> c.cardID.equals(remove.cardID));
        stage_remove_count++;
    }

    public static ArrayList<AbstractCard> popCardForRemove(int number) {
        ArrayList<AbstractCard> cardPoolCopy = new ArrayList<>(cardPool);
        ArrayList<AbstractCard> result = new ArrayList<>();
        while (result.size() < number) {
            if (!isContainerHasAnyMoreCard(cardPoolCopy, result)) {
                refreshCardQueue();
                if (!isContainerHasAnyMoreCard(cardPoolCopy, result)) {
                    break;
                }
            }
            AbstractCard card = cardPoolCopy.get(AbstractDungeon.cardRng.random(cardPoolCopy.size() - 1));
            cardPoolCopy.remove(card);
            if (result.stream().noneMatch(c -> c.cardID.equals(card.cardID))) {
                result.add(card);
            }
        }
        ArrayList<AbstractCard> copy = new ArrayList<>();
        for (AbstractCard card : result) {
            copy.add(card.makeStatEquivalentCopy());
        }
        return copy;
    }


    public static ArrayList<AbstractCard> popCard(int number, AbstractCard.CardType cardType) {
        if (cardType == null) return popCard(number);
        ArrayList<AbstractCard> result = new ArrayList<>();
        LinkedList<AbstractCard> pool = new LinkedList<>();
        for (AbstractCard card : cardPool) {
            if (card.type == cardType && !card.hasTag(AbstractCard.CardTags.HEALING)) pool.add(card);
        }
        Collections.shuffle(pool, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
        while (result.size() < number) {
            AbstractCard card = pool.pop();
            if (result.stream().noneMatch(c -> c.cardID.equals(card.cardID))) {
                result.add(card);
            }
        }
        return result;
    }

    public static ArrayList<AbstractCard> popCard(int number) {
        ArrayList<AbstractCard> result = new ArrayList<>();
        while (result.size() < number) {
            if (!isContainerHasAnyMoreCard(cardQueueInBattle, result)) {
                refreshCardQueue();
                if (!isContainerHasAnyMoreCard(cardQueueInBattle, result)) {
                    break;
                }
            }
            AbstractCard card = cardQueueInBattle.pop();
            if (result.stream().noneMatch(c -> c.cardID.equals(card.cardID))) {
                result.add(card);
            }
        }
        ArrayList<AbstractCard> copy = new ArrayList<>();
        for (AbstractCard card : result) {
            copy.add(card.makeStatEquivalentCopy());
        }
        return copy;
    }

    private static void refreshCardQueue() {
        cardQueueInBattle.clear();
        for (AbstractCard card : cardPool) {
            if (card.hasTag(AbstractCard.CardTags.HEALING)) continue;
            cardQueueInBattle.add(card);
        }
        Log.logger.info("refreshCardQueue cardQueueInBattle = {}", cardQueueInBattle.size());
        Collections.shuffle(cardQueueInBattle, new java.util.Random(rng.randomLong()));
    }

    private static boolean isContainerHasAnyMoreCard(List<AbstractCard> container, List<AbstractCard> result) {
        return !container.stream().allMatch(c -> result.stream().anyMatch(r -> r.cardID.equals(c.cardID)));
    }

}
