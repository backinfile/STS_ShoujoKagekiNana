package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class IKnown extends BaseCard {
    public static final String ID = ModPath.makeID(IKnown.class.getSimpleName());

    public IKnown() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.baseMagicNumber = this.magicNumber = 4;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StageDiscoveryAction(card -> {
            card.applyPowers();
            int b = Math.max(Math.max(card.damage, card.block), 0);
            if (upgraded) b += magicNumber;
            if (b > 0) {
                addToTop(new GainBlockAction(p, b));
            }
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
