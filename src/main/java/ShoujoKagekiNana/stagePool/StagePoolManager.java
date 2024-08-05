package ShoujoKagekiNana.stagePool;

import ShoujoKagekiCore.token.TokenCardField;
import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.util.Util;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StagePoolManager {
    public static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final LinkedList<AbstractCard> cardQueueInBattle = new LinkedList<>();

    public static int stage_remove_count = 0;

    public static Random rng;

    public static void onStartGame() {
    }

    public static void loadCardPool(SaveFile saveFile) {
        cardPool.clear();
        AbstractDungeon.player.getCardPool(cardPool);
        for (AbstractCard card : cardPool) {
            TokenCardField.isToken.set(card, false);
        }
        Log.logger.info("load card pool count = {}", cardPool.size());
        initializeCardPools();
    }

    public static void initializeCardPools() {
        AbstractDungeon.commonCardPool.clear();
        AbstractDungeon.uncommonCardPool.clear();
        AbstractDungeon.rareCardPool.clear();
        AbstractDungeon.srcCommonCardPool.clear();
        AbstractDungeon.srcUncommonCardPool.clear();
        AbstractDungeon.srcRareCardPool.clear();

        for (AbstractCard card : cardPool) {
            switch (card.rarity) {
                case COMMON:
                    AbstractDungeon.commonCardPool.addToTop(card);
                    AbstractDungeon.srcCommonCardPool.addToBottom(card);
                    break;
                case UNCOMMON:
                    AbstractDungeon.uncommonCardPool.addToTop(card);
                    AbstractDungeon.srcUncommonCardPool.addToBottom(card);
                    break;
                case RARE:
                    AbstractDungeon.rareCardPool.addToTop(card);
                    AbstractDungeon.srcRareCardPool.addToBottom(card);
                    break;
            }
        }
        Log.logger.info("initializeCardPools count = {}", cardPool.size());
    }

    public static void onBattleStart() {
        refreshCardQueue();
    }

    public static void removeCard(AbstractCard remove) {
        Util.removeFirst(cardPool, c -> c.cardID.equals(remove.cardID));
        Util.removeFirst(cardQueueInBattle, c -> c.cardID.equals(remove.cardID));
        initializeCardPools();
        stage_remove_count++;
    }

    public static ArrayList<AbstractCard> popCard(int number) {
        ArrayList<AbstractCard> result = new ArrayList<>();
        while (result.size() < number) {
            if (!isContainerHasAnyMoreCard(cardQueueInBattle, result)) {
                refreshCardQueue();
                if (!isContainerHasAnyMoreCard(cardQueueInBattle, result)) {
                    break; // TODO add curse
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
