package ShoujoKagekiNana.blossom;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.function.Consumer;

@SpirePatch(
        clz = AbstractCard.class,
        method = "<class>"
)
public class BlossomField {
    public static final int BlossomType_Normal = 0;
    public static final int BlossomType_Draw2 = 1; // blossom: draw 2

    public static SpireField<Boolean> blossom = new SpireField<>(() -> false); // is blossom card?
    public static SpireField<Boolean> blossomed = new SpireField<>(() -> false);

    public static SpireField<Consumer<AbstractCard>> blossomFunc = new SpireField<>(() -> null);
    public static SpireField<Boolean> blossomFuncChangeDescDefault = new SpireField<>(() -> false);


    public static void set(AbstractCard card, Consumer<AbstractCard> func, boolean changeDescription) {
        blossom.set(card, true);
        blossomFunc.set(card, func);
        blossomFuncChangeDescDefault.set(card, changeDescription);
    }

    public static void set(AbstractCard card, Consumer<AbstractCard> func) {
        set(card, func, true);
    }

    public static boolean isBlossomCard(AbstractCard card) {
        return blossom.get(card);
    }

    public static boolean isBlossomed(AbstractCard card) {
        return blossomed.get(card);
    }
}
