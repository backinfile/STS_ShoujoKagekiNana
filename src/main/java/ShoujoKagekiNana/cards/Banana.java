package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.blossom.BlossomField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Banana extends BaseCard {
    public static final String ID = makeID(Banana.class.getSimpleName());

    public Banana() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.baseBlock = 5;
        this.magicNumber = this.baseMagicNumber = 3;
        BlossomField.set(this, null);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (BlossomField.isBlossomed(this)) {
            addToBot(new GainBlockAction(p, p, block));
        }
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
//            upgradeBlock(2);
            initializeDescription();
        }
    }
}