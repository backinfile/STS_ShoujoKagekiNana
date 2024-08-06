package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.List;

public class TransparencyModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(TransparencyModifier.class.getSimpleName());
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    @Override
    public void onUpdate(AbstractCard card) {
        card.targetTransparency = 0.8f;
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

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> result = new ArrayList<>();
        result.add(new TooltipInfo(uiStrings.TEXT[0], uiStrings.TEXT[1]));
        return result;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TransparencyModifier();
    }
}
