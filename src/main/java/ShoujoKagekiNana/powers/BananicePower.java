package ShoujoKagekiNana.powers;


import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BananicePower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(BananicePower.class.getSimpleName());


    public BananicePower() {
        super(POWER_ID, PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, -1);
    }

    @Override
    public void triggerOnBlossom(AbstractCard card) {
        flash();
        addToBot(new InstantAction(() -> {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hand.contains(card)) {
                p.hand.moveToDeck(card, false);
            }
        }));
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = DESCRIPTIONS[0];
    }
}
