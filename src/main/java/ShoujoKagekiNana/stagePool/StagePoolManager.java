package ShoujoKagekiNana.stagePool;

import ShoujoKagekiNana.Log;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class StagePoolManager {
    private static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final LinkedList<AbstractCard> cardQueueInBattle = new LinkedList<>();

    public static Random rng;

    public static void onStartGame() {
    }

    public static void loadCardPool(SaveFile saveFile) {
        cardPool.clear();
        AbstractDungeon.player.getCardPool(cardPool);
        Log.logger.info("load card pool count = {}", cardPool.size());
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

    public static void removeCard(AbstractCard card) {

    }

    public static ArrayList<AbstractCard> popCard(int number) {
        ArrayList<AbstractCard> result = new ArrayList<>();

        while (result.size() < number) {
            if (cardQueueInBattle.isEmpty()) {
                refreshCardQueue();
            }
            AbstractCard card = cardQueueInBattle.pop();
            if (result.stream().noneMatch(c -> c.cardID.equals(card.cardID))) {
                result.add(card);
            }
        }
        return result;
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
}
