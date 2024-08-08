package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.ModPath;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;
import java.util.function.Consumer;

public class ExhaustHandCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModPath.makeID(ExhaustHandCardAction.class.getSimpleName()));
    public static final String[] TEXT = uiStrings.TEXT;
    private final Consumer<AbstractCard> consumer;

    public ExhaustHandCardAction(int amount) {
        this(amount, null);
    }

    public ExhaustHandCardAction(int amount, Consumer<AbstractCard> consumer) {
        this.consumer = consumer;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.player.hand.size() == 0) {
                this.isDone = true;
                return;
            }

            int cnt = Math.min(AbstractDungeon.player.hand.size(), amount);
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], cnt, true, true);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (consumer != null) consumer.accept(c);
                AbstractDungeon.player.hand.moveToExhaustPile(c);
            }
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }
}

