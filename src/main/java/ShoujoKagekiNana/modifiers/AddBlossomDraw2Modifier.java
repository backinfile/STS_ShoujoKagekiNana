package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.blossom.BlossomField;
import ShoujoKagekiNana.blossom.BlossomFieldPatch;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class AddBlossomDraw2Modifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(AddBlossomDraw2Modifier.class.getSimpleName());
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        BlossomField.set(card, AbstractCard::initializeDescription, false);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        if (BlossomField.isBlossomed(card)) {
            addToBot(new DrawCardAction(2));
        }
    }

    //    @Override
//    public Color getGlow(AbstractCard card) {
//        if (BlossomFieldPatch.canTriggerBlossom(card)) {
//            return new Color(0.0F, 1.0F, 0.0F, 0.25F);
//        }
//        return super.getGlow(card);
//    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (BlossomField.isBlossomed(card)) {
            return uiStrings.TEXT[1] + rawDescription + uiStrings.TEXT[2];
        }
        return rawDescription + uiStrings.TEXT[0];
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !BlossomField.isBlossomCard(card) && !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddBlossomDraw2Modifier();
    }
}
