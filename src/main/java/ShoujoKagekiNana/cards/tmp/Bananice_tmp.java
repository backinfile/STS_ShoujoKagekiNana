package ShoujoKagekiNana.cards.tmp;

import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.powers.BananicePower;
import ShoujoKagekiNana.powers.BananicePower_tmp;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

@AutoAdd.Ignore
public class Bananice_tmp extends BaseCard {
    public static final String ID = makeID(Bananice_tmp.class.getSimpleName());

    public Bananice_tmp() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BananicePower_tmp(magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}