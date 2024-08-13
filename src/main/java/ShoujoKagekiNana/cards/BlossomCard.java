package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.token.TokenCardField;
import ShoujoKagekiCore.util.Utils2;
import ShoujoKagekiNana.blossom.BlossomField;
import ShoujoKagekiNana.blossom.BlossomFieldPatch;
import ShoujoKagekiNana.charactor.NanaCharacter;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class BlossomCard extends BaseCard {
    public BlossomCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, rarity, target);
        BlossomField.set(this);
    }

    public BlossomCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, cost, type, rarity, target);
        BlossomField.set(this);
    }

    @Override
    public void triggerOnGlowCheck() {
        if (BlossomFieldPatch.canTriggerBlossom(this)) {
            this.glowColor = AbstractCard.GREEN_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}
