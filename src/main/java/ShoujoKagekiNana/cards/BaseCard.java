package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.charactor.NanaCharacter;

public abstract class BaseCard extends ShoujoKagekiCore.base.BaseCard {

    public BaseCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, NanaCharacter.Enums.CardColor_Nana, rarity, target);
    }

    public BaseCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, cost, type, NanaCharacter.Enums.CardColor_Nana, rarity, target);
    }
}
