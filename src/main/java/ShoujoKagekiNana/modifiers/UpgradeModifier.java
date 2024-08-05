package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class UpgradeModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(UpgradeModifier.class.getSimpleName());
//    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    @Override
    public AbstractCardModifier makeCopy() {
        return new UpgradeModifier();
    }
}
