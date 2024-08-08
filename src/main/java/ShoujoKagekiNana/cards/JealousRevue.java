package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.modifiers.InstantModifier;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

public class JealousRevue extends BaseCard {
    public static final String ID = makeID(JealousRevue.class.getSimpleName());

    public JealousRevue() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 4;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new InstantAction(() -> {
            int toDraw = BaseMod.MAX_HAND_SIZE - p.hand.size();
            if (toDraw > 0) {
                addToTop(new DrawCardAction(toDraw, new InstantAction(() -> {
                    for (AbstractCard card : DrawCardAction.drawnCards) {
                        CardModifierManager.addModifier(card, new InstantModifier());
                    }
                })));
            }
        }));
//        addToBot(new DrawCardAction(this.magicNumber, new InstantAction(() -> {
//            AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
//            for (AbstractCard c : DrawCardAction.drawnCards) {
//                if (!DisposableVariable.isDisposableCard(c)) {
//                    AbstractDungeon.player.hand.moveToDiscardPile(c);
//                    c.triggerOnManualDiscard();
//                    GameActionManager.incrementDiscard(false);
//                }
//            }
//        })));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            exhaust = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}