package ShoujoKagekiNana.relics;

import ShoujoKagekiNana.ModPath;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ShineRelic extends BaseRelic {
    public static final String RAW_ID = ShineRelic.class.getSimpleName();
    public static final String ID = ModPath.makeID(RAW_ID);

    public ShineRelic() {
        super(ID, RAW_ID, RelicTier.STARTER, LandingSound.FLAT);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        super.onUseCard(targetCard, useCardAction);
    }
}
