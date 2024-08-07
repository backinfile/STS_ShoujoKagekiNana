package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.modifiers.TransparencyModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend02 extends BaseCard {
    public static final String ID = ModPath.makeID(Defend02.class.getSimpleName());
//    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Defend02() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        this.baseBlock = this.block = 6;
        this.magicNumber = this.baseMagicNumber = 3;
//        BlockModifierManager.addModifier(this, new InstantBlock());
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
//        this.addToBot(new GainCustomBlockAction(this, p, this.block));
        this.addToBot(new GainBlockAction(p, block));
        addToBot(new DrawCardAction(magicNumber, new InstantAction(() -> {
            for (AbstractCard card : DrawCardAction.drawnCards) {
                CardModifierManager.addModifier(card, new TransparencyModifier());
            }
        })));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
            initializeDescription();
        }
    }
}