package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.List;

public class InstantModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(InstantModifier.class.getSimpleName());
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    private long endTime = 0;

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public void onUpdate(AbstractCard card) {
        if (System.currentTimeMillis() > endTime) {
            endTime = System.currentTimeMillis() + 500;
            card.targetTransparency = 0.8f;
        }
    }

    @Override
    public void onOtherCardPlayed(AbstractCard card, AbstractCard otherCard, CardGroup group) {
        if (otherCard != card && CardModifierManager.hasModifier(otherCard, ID)) {
            addToTop(new InstantAction(() -> {
                CardModifierManager.removeModifiersById(card, ID, true);
                if (AbstractDungeon.player.hand.contains(card)) {
                    AbstractDungeon.player.hand.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                }
            }));
        }
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.unfadeOut();
    }

//    @Override
//    public String modifyDescription(String rawDescription, AbstractCard card) {
//        if (rawDescription.equals("")) {
//            return uiStrings.TEXT[2];
//        }
//        return uiStrings.TEXT[2] + uiStrings.TEXT[3] + rawDescription;
//    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> result = new ArrayList<>();
        result.add(new TooltipInfo(uiStrings.TEXT[0], uiStrings.TEXT[1]));
        return result;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new InstantModifier();
    }
}
