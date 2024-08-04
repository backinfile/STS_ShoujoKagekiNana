package ShoujoKagekiNana.cards.starter;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class OutOfStory extends BaseCard {
    public static final String ID = ModPath.makeID(OutOfStory.class.getSimpleName());

    public OutOfStory() {
        super(ID, 0, CardType.SKILL, CardRarity.BASIC, CardTarget.NONE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StageDiscoveryAction());
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            exhaust = false;
            initializeDescription();
        }
    }
}
