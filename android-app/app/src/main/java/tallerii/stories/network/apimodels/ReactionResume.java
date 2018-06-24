package tallerii.stories.network.apimodels;

public class ReactionResume {
    private String reactionType;
    private int viewId;
    private int countId;
    private Reaction react;
    private int count;

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public int getCountId() {
        return countId;
    }

    public int getViewId() {
        return viewId;
    }

    public Reaction getReact() {
        return react;
    }

    public int getCount() {
        return count;
    }

    public void setReact(Reaction react) {
        this.react = react;
    }
}
