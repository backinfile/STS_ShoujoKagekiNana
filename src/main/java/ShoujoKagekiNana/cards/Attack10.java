package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.blossom.BlossomField;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Attack10 extends BaseCard {
    public static final String ID = makeID(Attack10.class.getSimpleName());

    public Attack10() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 5;
//        DisposableVariable.setBaseValue(this, 9);
        BlossomField.set(this, null);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.calculateCardDamage(m);

        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (BlossomField.isBlossomed(this)) {
            addToBot(new DamageAction(m, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    public void calculateCardDamage(AbstractMonster mo) {

        int saveBaseDamage = this.baseDamage;
        this.baseDamage = this.baseMagicNumber;
        super.calculateCardDamage(mo);
        boolean saveMagicNumberModified = this.isDamageModified;
        int saveMagicNumber = this.damage;


        this.baseDamage = saveBaseDamage;
        super.calculateCardDamage(mo);

        this.magicNumber = saveMagicNumber;
        this.isMagicNumberModified = saveMagicNumberModified;
    }

    public void applyPowers() {

        int saveBaseDamage = this.baseDamage;
        this.baseDamage = this.baseMagicNumber;
        super.applyPowers();
        boolean saveMagicNumberModified = this.isDamageModified;
        int saveMagicNumber = this.damage;


        this.baseDamage = saveBaseDamage;
        super.applyPowers();

        this.magicNumber = saveMagicNumber;
        this.isMagicNumberModified = saveMagicNumberModified;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
//            upgradeMagicNumber(4);
            initializeDescription();
        }
    }
}