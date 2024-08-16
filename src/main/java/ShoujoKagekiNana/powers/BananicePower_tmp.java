package ShoujoKagekiNana.powers;


import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.blossom.BlossomField;
import ShoujoKagekiNana.modifiers.AddBlossomDraw2Modifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class BananicePower_tmp extends BasePower {
    public static final String POWER_ID = ModPath.makeID(BananicePower_tmp.class.getSimpleName());


    public BananicePower_tmp(int amount) {
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
