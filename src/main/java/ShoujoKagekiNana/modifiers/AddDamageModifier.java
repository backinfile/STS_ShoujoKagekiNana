package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class AddDamageModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(AddDamageModifier.class.getSimpleName());
//    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        CardModifierManager.modifiers(card);
        return damage + 1;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddDamageModifier();
    }
}
