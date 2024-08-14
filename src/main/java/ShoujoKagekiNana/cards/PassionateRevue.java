package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.AudioManager;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PassionateRevue extends BaseCard {
    public static final String ID = ModPath.makeID(PassionateRevue.class.getSimpleName());

    public PassionateRevue() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        isInnate = true;
        this.magicNumber = this.baseMagicNumber = 3;
        DisposableVariable.setBaseValue(this, 9);
        this.singleCardAudioKey = AudioManager.PassionateRevue;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
