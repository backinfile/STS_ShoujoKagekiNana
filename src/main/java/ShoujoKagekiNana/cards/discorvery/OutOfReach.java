package ShoujoKagekiNana.cards.discorvery;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.actions.StageRemoveAction;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OutOfReach extends BaseCard {
    public static final String ID = ModPath.makeID(OutOfReach.class.getSimpleName());

    public OutOfReach() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StageDiscoveryAction(card -> {
            addToTop(new StageRemoveAction(card.makeCopy()));
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBaseCost(0);
        }
    }
}
