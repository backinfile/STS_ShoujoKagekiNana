package ShoujoKagekiNana.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class InstantAction extends AbstractGameAction {

    private final Runnable runnable;

    public InstantAction(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void update() {
        runnable.run();
        isDone = true;
    }
}
