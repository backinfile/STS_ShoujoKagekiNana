package ShoujoKagekiNana.cards.discorvery;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IKnown extends BaseCard {
    public static final String ID = ModPath.makeID(IKnown.class.getSimpleName());

    public IKnown() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.baseMagicNumber = this.magicNumber = 4;
        exhaust = true;
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
