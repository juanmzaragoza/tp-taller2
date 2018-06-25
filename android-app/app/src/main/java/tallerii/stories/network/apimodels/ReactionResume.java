package tallerii.stories.network.apimodels;

public class ReactionResume {
    private String reactionType;
    private int viewId;
    private int countViewId;
    private Reaction react;
    private int count;
    private boolean isPressed;

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void setCountViewId(int countId) {
        this.countViewId = countId;
    }

    public int getCountViewId() {
        return countViewId;
    }

    public int getViewId() {
        return viewId;
    }

    public Reaction getReact() {
        return react;
    }

    public int getCount() {
        return isPressed ? count + 1 : count;
    }

    public void setReact(Reaction react) {
        this.react = react;
    }

    public boolean isPressed() {
        return isPressed || react != null;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
        if (!isPressed && react != null) {
            react = null;
            count--;
        }
    }
}
