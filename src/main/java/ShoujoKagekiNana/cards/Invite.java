package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.powers.HotBananaPower;
import ShoujoKagekiNana.powers.InvitePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Invite extends BaseCard {
    public static final String ID = makeID(Invite.class.getSimpleName());

    public Invite() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new InvitePower(magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
//            initializeDescription();
        }
    }
}