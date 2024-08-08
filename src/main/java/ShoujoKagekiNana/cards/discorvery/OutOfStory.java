package ShoujoKagekiNana.cards.discorvery;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OutOfStory extends BaseCard {
    public static final String ID = ModPath.makeID(OutOfStory.class.getSimpleName());

    public OutOfStory() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            this.addToBot(new StageDiscoveryAction());
        } else {
            this.addToBot(new StageDiscoveryAction(c -> c.setCostForTurn(0)));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
