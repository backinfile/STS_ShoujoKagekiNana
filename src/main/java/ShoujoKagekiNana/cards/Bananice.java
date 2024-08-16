package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.powers.BananicePower;
import ShoujoKagekiNana.powers.Party99Power;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

public class Bananice extends BaseCard {
    public static final String ID = makeID(Bananice.class.getSimpleName());

    public Bananice() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BananicePower()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }
}