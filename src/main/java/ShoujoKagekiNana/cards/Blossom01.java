package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.blossom.BlossomField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Blossom01 extends BlossomCard {
    public static final String ID = makeID(Blossom01.class.getSimpleName());

    public Blossom01() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.baseBlock = 5;
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (BlossomField.isBlossomed(this)) {
            addToBot(new GainBlockAction(p, p, block));
        }
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void triggerBlossom() {
        super.triggerBlossom();
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
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