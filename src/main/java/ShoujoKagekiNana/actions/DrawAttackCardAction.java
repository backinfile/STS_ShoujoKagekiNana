package ShoujoKagekiNana.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;
import java.util.stream.Collectors;

public class DrawAttackCardAction extends AbstractGameAction {
    private final int number;
    private final boolean shuffle;

    public DrawAttackCardAction(int number, boolean shuffle) {
        this.number = number;
        this.shuffle = shuffle;
    }

    @Override
    public void update() {
        CardGroup drawPile = AbstractDungeon.player.drawPile;
        int count = (int) drawPile.group.stream().filter(c -> c.type == AbstractCard.CardType.ATTACK).count();
        int discardCount = (int) AbstractDungeon.player.discardPile.group.stream().filter(c -> c.type == AbstractCard.CardType.ATTACK).count();
        int toDraw = Math.min(count, number);

        int left = number - toDraw;
        if (left > 0 && shuffle && discardCount > 0) {
            addToTop(new DrawAttackCardAction(left, false));
            addToTop(new EmptyDeckShuffleAction());
        }

        if (toDraw > 0) {
            List<AbstractCard> attacks = drawPile.group.stream().filter(c -> c.type == AbstractCard.CardType.ATTACK).skip(count - toDraw).limit(toDraw).collect(Collectors.toList());
            attacks.forEach(drawPile::removeCard);
            attacks.forEach(drawPile::addToTop);
            addToTop(new DrawCardAction(toDraw));
        }
        isDone = true;
    }
}
