package ShoujoKagekiNana.cards.discorvery;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.modifiers.RepeatCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Repeat extends BaseCard {
    public static final String ID = ModPath.makeID(Repeat.class.getSimpleName());

    public Repeat() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard copy = Repeat.this.makeStatEquivalentCopy();
        this.addToBot(new StageDiscoveryAction(c -> {
            CardModifierManager.addModifier(c, new RepeatCardModifier().with(copy.makeStatEquivalentCopy()));
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            selfRetain = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
