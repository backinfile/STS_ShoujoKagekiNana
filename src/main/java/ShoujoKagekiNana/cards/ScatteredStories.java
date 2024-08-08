package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.actions.StageCardSinglePowerUpAction;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ScatteredStories extends BaseCard {
    public static final String ID = ModPath.makeID(ScatteredStories.class.getSimpleName());

    public ScatteredStories() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        exhaust = true;
        this.magicNumber = this.baseMagicNumber = 3;
//        this.tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int cnt = this.magicNumber;
        this.addToBot(new StageDiscoveryAction(c -> {
            for (int i = 0; i < cnt; i++) {
                CardModifierManager.addModifier(c, new AddDamageModifier());
            }
//            addToTop(new StageCardSinglePowerUpAction(c, c2 -> {
//                for (int i = 0; i < cnt; i++) {
//                    CardModifierManager.addModifier(c2, new AddDamageModifier());
//                }
//            }));
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
            initializeDescription();
        }
    }
}
