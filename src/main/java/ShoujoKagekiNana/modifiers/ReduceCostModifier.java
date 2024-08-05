package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import basemod.abstracts.AbstractCardModifier;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ReduceCostModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(ReduceCostModifier.class.getSimpleName());
//    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        card.updateCost(-1);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ReduceCostModifier();
    }

}
