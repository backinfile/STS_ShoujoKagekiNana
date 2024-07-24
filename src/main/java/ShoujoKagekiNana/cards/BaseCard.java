package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.charactor.NanaCharacter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class BaseCard extends AbstractDefaultCard {
    public CardStrings cardStrings;
    public String DESCRIPTION;
    public ArrayList<AbstractGameEffect> cardImageEffects = null;


    public BaseCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, NanaCharacter.Enums.CardColor_Nana, rarity, target);
        initNameAndDescription(id);
    }

    public BaseCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
        initNameAndDescription(id);
    }

    public BaseCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, ModPath.makeCardPath(id.split(":")[1] + ".png"), cost, languagePack.getCardStrings(id).DESCRIPTION, type, NanaCharacter.Enums.CardColor_Nana, rarity, target);
        initNameAndDescription(id);
    }

    public BaseCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, ModPath.makeCardPath(id.split(":")[1] + ".png"), cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
        initNameAndDescription(id);
    }

    protected void upgradeTimes() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }

    private void initNameAndDescription(String id) {
        cardStrings = languagePack.getCardStrings(id);
        DESCRIPTION = cardStrings.DESCRIPTION;
        this.name = cardStrings.NAME;
        this.rawDescription = DESCRIPTION;
        this.initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void update() {
        super.update();

        if (cardImageEffects != null) {
            Iterator<AbstractGameEffect> iterator = cardImageEffects.iterator();
            while (iterator.hasNext()) {
                AbstractGameEffect effect = iterator.next();
                effect.update();
                if (effect.isDone) {
                    iterator.remove();
                }
            }
        }
    }

    @SpireOverride
    protected void renderPortrait(SpriteBatch sb) {
        SpireSuper.call(sb);
        if (cardImageEffects != null) {
            Iterator<AbstractGameEffect> iterator = cardImageEffects.iterator();
            while (iterator.hasNext()) {
                AbstractGameEffect effect = iterator.next();
                effect.render(sb);
                if (effect.isDone) {
                    iterator.remove();
                }
            }
        }
    }

    public void addCardImageEffect(AbstractGameEffect effect) {
        if (!effect.isDone) {
            if (cardImageEffects == null) cardImageEffects = new ArrayList<>();
            cardImageEffects.add(effect);
        }
    }

    public void triggerOnTurnStartInBag() {

    }

    // 闪耀耗尽时
    public void triggerOnDisposed() {

    }

    // take from bag by any way
    public void triggerOnTakeFromBag() {

    }

    public void triggerOnGlobalMove() {

    }

    public void triggerOnTakeFromBagToHand() {

    }

    public void triggerOnPutInBag() {

    }

    public void triggerWhenMoveToDiscardPile() { // use this

    }

    public void triggerOnShuffleInfoDrawPile() {

    }

    public void triggerOnEndOfPlayerTurnInBag() {
    }

    public void triggerOnEndOfPlayerTurnInDrawPile() {
    }

    public void triggerOnEndOfPlayerTurnInDiscardPile() {
    }


    public void triggerOnAccretion() {

    }

    public void triggerOnBattleStart() {

    }
}
