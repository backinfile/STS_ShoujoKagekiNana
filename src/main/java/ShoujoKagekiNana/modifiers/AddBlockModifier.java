package ShoujoKagekiNana.modifiers;

import ShoujoKagekiNana.ModPath;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AddBlockModifier extends AbstractCardModifier {
    public static final String ID = ModPath.makeID(AddBlockModifier.class.getSimpleName());
//    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        CardModifierManager.addModifier(card, new StagePowerUpModifier());
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.baseBlock > 0;
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        return block + 1;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddBlockModifier();
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        super.onRender(card, sb);
    }
}
