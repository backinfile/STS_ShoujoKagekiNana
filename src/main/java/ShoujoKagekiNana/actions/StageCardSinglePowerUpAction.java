package ShoujoKagekiNana.actions;

import ShoujoKagekiCore.util.Utils2;
import ShoujoKagekiNana.cards.Defend03;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StageCardSinglePowerUpAction extends AbstractGameAction {
    private final Predicate<AbstractCard> predicate;
    private final Consumer<AbstractCard> consumer;

    public StageCardSinglePowerUpAction(Predicate<AbstractCard> predicate, Consumer<AbstractCard> consumer) {
        this.predicate = predicate;
        this.consumer = consumer;
    }

    public void doInstance() {
        update();
    }

    @Override
    public void update() {
        List<AbstractCard> list = StagePoolManager.cardPool.stream().filter(predicate).collect(Collectors.toList());
        if (!list.isEmpty()) {
            AbstractCard card = list.get(AbstractDungeon.cardRng.random(list.size() - 1));
            consumer.accept(card);
        }

        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof Defend03) {
                consumer.accept(card);
            }
        }
        for (AbstractCard card : Utils2.getAllCardsInCombat(AbstractDungeon.player)) {
            if (card instanceof Defend03) {
                consumer.accept(card);
            }
        }

        isDone = true;
    }
}
