package ShoujoKagekiNana.cards.discorvery;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class Exist extends BaseCard {
    public static final String ID = ModPath.makeID(Exist.class.getSimpleName());

    public Exist() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
//        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StageDiscoveryAction(card -> {
            if (card.costForTurn == -1) {
                addToTop(new GainEnergyAction(EnergyPanel.getCurrentEnergy()));
            } else if (card.costForTurn > 0) {
                addToTop(new GainEnergyAction(card.costForTurn));
            }
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBaseCost(0);
        }
    }
}
