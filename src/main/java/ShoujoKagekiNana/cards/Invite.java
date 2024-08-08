package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.token.TokenCardField;
import ShoujoKagekiNana.actions.CopyHandCardToDeckAction;
import ShoujoKagekiNana.powers.HotBananaPower;
import ShoujoKagekiNana.powers.InvitePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Invite extends BaseCard {
    public static final String ID = makeID(Invite.class.getSimpleName());

    public Invite() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CopyHandCardToDeckAction(magicNumber, c -> TokenCardField.isToken.get(c)));
    }

    @Override
    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.hand.group.stream().filter(c -> c != this).anyMatch(c -> TokenCardField.isToken.get(c))) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
//            upgradeBaseCost(0);
            selfRetain = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}