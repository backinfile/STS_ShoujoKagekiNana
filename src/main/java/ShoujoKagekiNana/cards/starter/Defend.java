package ShoujoKagekiNana.cards.starter;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend extends BaseCard {
    public static final String ID = ModPath.makeID(Defend.class.getSimpleName());

    public Defend() {
        super(ID, 1, CardType.SKILL, AbstractCard.CardRarity.BASIC, CardTarget.NONE);
        this.baseBlock = 5;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
        }
    }
}
