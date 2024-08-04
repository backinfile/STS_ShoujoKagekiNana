package ShoujoKagekiNana.actions;

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

    @Override
    public void update() {
        List<AbstractCard> list = StagePoolManager.cardPool.stream().filter(predicate).collect(Collectors.toList());
        if (!list.isEmpty()) {
            AbstractCard card = list.get(AbstractDungeon.cardRng.random(list.size() - 1));
            consumer.accept(card);
        }
        isDone = true;
    }
}
