package ShoujoKagekiNana.auditionEnergy;


import ShoujoKagekiNana.ModPath;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AuditionEnergy {

    public static final String ID = ModPath.makeID(AuditionEnergy.class.getSimpleName());
    private static int curValue = 0;

    public static void add(int amount) {
        curValue += amount;
        if (amount > 0) {
            DoubleEnergyOrb.secondVfxTimer = 2.0F;
        }

    }

    public static int count() {
        return curValue;
    }

    public static void reset() {
        curValue = 0;
    }

    public static boolean checkCanUseEnergy(AbstractCard card) {
        return card.type == AbstractCard.CardType.ATTACK;
    }
}
