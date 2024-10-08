package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.util.Utils2;
import ShoujoKagekiNana.cards.tmp.Invite;
import ShoujoKagekiNana.charactor.NanaCharacter;

public abstract class BaseCard extends ShoujoKagekiCore.base.BaseCard {

    public String singleCardAudioKey = null;

    public boolean enableGrayText = false;
    public int grayTextStartIndex = -1;
    public int grayTextEndIndex = -1;

    public BaseCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, NanaCharacter.Enums.CardColor_Nana, rarity, target);
        if (Utils2.inBattlePhase()) {
            enableGrayText = true;
        }
    }

    public BaseCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, cost, type, NanaCharacter.Enums.CardColor_Nana, rarity, target);
        if (Utils2.inBattlePhase()) {
            enableGrayText = true;
        }
    }


    @Override
    public void loadCardImage(String img) {
        try {
            super.loadCardImage(img);
        } catch (Exception e) {
            this.textureImg = makeCardPath(Invite.ID);
            super.loadCardImage(textureImg);
        }
    }
}
