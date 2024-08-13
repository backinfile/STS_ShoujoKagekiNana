package ShoujoKagekiNana.powers;


import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.blossom.BlossomField;
import ShoujoKagekiNana.modifiers.AddBlossomDraw2Modifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BananicePower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(BananicePower.class.getSimpleName());


    public BananicePower(int amount) {
        super(POWER_ID, PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, amount);
    }


    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();

        flash();
        addToBot(new InstantAction(() -> {
            ArrayList<AbstractCard> cards = AbstractDungeon.player.hand.group.stream().filter(c -> !BlossomField.isBlossomCard(c)).collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(cards, new Random(AbstractDungeon.cardRandomRng.randomLong()));
            for (AbstractCard card : cards.stream().limit(amount).collect(Collectors.toList())) {
                CardModifierManager.addModifier(card, new AddBlossomDraw2Modifier());
            }
        }));
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
