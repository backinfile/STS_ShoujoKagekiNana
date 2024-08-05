package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class StagePowerUpModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(StagePowerUpModifier.class.getSimpleName());
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        AbstractList<AbstractCardModifier> modifiers = CardModifierManager.modifiers(card);
        long addDamage = modifiers.stream().filter(m -> m instanceof AddDamageModifier).count();
        long addBlock = modifiers.stream().filter(m -> m instanceof AddBlockModifier).count();
        List<TooltipInfo> result = new ArrayList<>();
        result.add(new TooltipInfo(uiStrings.TEXT[0], uiStrings.TEXT[1] + addDamage + uiStrings.TEXT[2] + addBlock + uiStrings.TEXT[3]));
        return result;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new StagePowerUpModifier();
    }
}
