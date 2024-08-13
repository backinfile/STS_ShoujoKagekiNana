package ShoujoKagekiNana.modifiers;

import ShoujoKagekiCore.actions.TrueWaitAction;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.cards.discorvery.Repeat;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.List;

@AbstractCardModifier.SaveIgnore
public class RepeatCardModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(RepeatCardModifier.class.getSimpleName());
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public AbstractCard repeatCard = null;

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    public RepeatCardModifier with(AbstractCard repeatCard) {
        this.repeatCard = repeatCard;
        return this;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> result = new ArrayList<>();
        String description;
        if (repeatCard == null) {
            description = uiStrings.TEXT[1] + uiStrings.TEXT[2] + uiStrings.TEXT[3];
        } else {
            description = uiStrings.TEXT[1] + repeatCard.name + uiStrings.TEXT[3];
        }
        result.add(new TooltipInfo(uiStrings.TEXT[0], description));
        return result;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        super.atEndOfTurn(card, group);
        if (AbstractDungeon.player.hand.contains(card) && repeatCard != null) {
            addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            addToBot(new MakeTempCardInHandAction(repeatCard));
            addToBot(new TrueWaitAction(Settings.ACTION_DUR_FAST));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RepeatCardModifier();
    }
}
