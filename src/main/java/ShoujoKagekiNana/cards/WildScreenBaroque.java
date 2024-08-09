package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.actions.DamageCallbackAction;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.actions.StageCardSinglePowerUpAction;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static ShoujoKagekiNana.ModPath.makeID;


public class WildScreenBaroque extends BaseCard {
    public static final String ID = makeID(WildScreenBaroque.class.getSimpleName());

    public WildScreenBaroque() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 8;
//        DisposableVariable.setBaseValue(this, 3);
        this.tags.add(CardTags.HEALING);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractMonster target = m;
        addToBot(new DamageCallbackAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, hp -> {
            if ((target.isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower(MinionPower.POWER_ID)) {
                StageCardSinglePowerUpAction action = new StageCardSinglePowerUpAction(AbstractCard::canUpgrade, c -> {
                    c.upgrade();
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                    addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                });
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    action.doInstance();
                } else {
                    addToTop(action);
                }
            }
        }));
         }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
            initializeDescription();
        }
    }
}