package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.stances.RevueStance;
import ShoujoKagekiNana.stances.patches.StancePatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;

public class CreatureSwitchStanceAction extends AbstractGameAction {

    private final AbstractCreature creature;
    private final String id;
    private AbstractStance newStance;

    public CreatureSwitchStanceAction(AbstractCreature creature, String stanceId) {
        this.creature = creature;
        this.newStance = null;
        this.duration = Settings.ACTION_DUR_FAST;
        this.id = stanceId;
    }

    public CreatureSwitchStanceAction(AbstractCreature creature, AbstractStance newStance) {
        this(creature, newStance.ID);
        this.newStance = newStance;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
//            if (creature.hasPower("CannotChangeStancePower")) {
//                this.isDone = true;
//                return;
//            }
            if (creature.isDeadOrEscaped()) {
                StancePatches.Field.stance.set(creature, new NeutralStance());
                isDone = true;
                return;
            }

            AbstractStance oldStance = StancePatches.Field.stance.get(creature);
            if (!oldStance.ID.equals(this.id)) {
                if (this.newStance == null) {
                    this.newStance = AbstractStance.getStanceFromName(this.id);
                    StancePatches.Field2.owner.set(this.newStance, creature);
                }
            } else {
                this.newStance = new NeutralStance();
            }
            for (AbstractPower p : creature.powers)
                p.onChangeStance(oldStance, this.newStance);

            oldStance.onExitStance();
            StancePatches.Field.stance.set(creature, this.newStance);
            this.newStance.onEnterStance();

//            AbstractDungeon.player.switchedStance();
//            AbstractDungeon.player.onStanceChange(this.id);
        }

//        AbstractDungeon.onModifyPower();
        if (Settings.FAST_MODE) {
            this.isDone = true;
            return;
        }
        this.tickDuration();
    }

}