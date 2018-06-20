package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Reactions {
    @SerializedName("LIKE")
    private Reaction like = new Reaction();
    @SerializedName("NOTLIKE")
    private Reaction notLike = new Reaction();
    @SerializedName("ENJOY")
    private Reaction enjoy = new Reaction();
    @SerializedName("GETBORED")
    private Reaction bored = new Reaction();

    public Reaction getLike() {
        return like;
    }

    public void setLike(Reaction like) {
        this.like = like;
    }

    public Reaction getNotLike() {
        return notLike;
    }

    public void setNotLike(Reaction notLike) {
        this.notLike = notLike;
    }

    public Reaction getEnjoy() {
        return enjoy;
    }

    public void setEnjoy(Reaction enjoy) {
        this.enjoy = enjoy;
    }

    public Reaction getBored() {
        return bored;
    }

    public void setBored(Reaction bored) {
        this.bored = bored;
    }

    public List<Reaction> obtainReactions() {
        List<Reaction> reactions = new ArrayList<>();
        like.setReactionType("LIKE");
        reactions.add(like);
        notLike.setReactionType("NOTLIKE");
        reactions.add(like);
        enjoy.setReactionType("ENJOY");
        reactions.add(like);
        bored.setReactionType("GETBORED");
        reactions.add(like);
        return reactions;
    }
}
