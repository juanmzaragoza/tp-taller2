package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.R;

public class Reactions {
    private static final String I_LIKE_REACTION = "LIKE";
    private static final String I_NOTLIKE_REACTION = "NOTLIKE";
    private static final String I_ENJOY_REACTION = "ENJOY";
    private static final String I_GETBORED_REACTION = "GETBORED";

    @SerializedName(I_LIKE_REACTION)
    private ReactionResume like = new ReactionResume();
    @SerializedName(I_NOTLIKE_REACTION)
    private ReactionResume notLike = new ReactionResume();
    @SerializedName(I_ENJOY_REACTION)
    private ReactionResume enjoy = new ReactionResume();
    @SerializedName(I_GETBORED_REACTION)
    private ReactionResume bored = new ReactionResume();

    public ReactionResume getLike() {
        return like;
    }

    public void setLike(ReactionResume like) {
        this.like = like;
    }

    public ReactionResume getNotLike() {
        return notLike;
    }

    public void setNotLike(ReactionResume notLike) {
        this.notLike = notLike;
    }

    public ReactionResume getEnjoy() {
        return enjoy;
    }

    public void setEnjoy(ReactionResume enjoy) {
        this.enjoy = enjoy;
    }

    public ReactionResume getBored() {
        return bored;
    }

    public void setBored(ReactionResume bored) {
        this.bored = bored;
    }

    // TODO unfuck, maybe set id's programatically?
    // Fuck encapsulation and single responsibility
    public List<ReactionResume> obtainReactions() {
        List<ReactionResume> reactionResumes = new ArrayList<>();
        addReactionButton(reactionResumes, like, I_LIKE_REACTION, R.id.likeButton, R.id.likeButtonCount);
        addReactionButton(reactionResumes, notLike, I_NOTLIKE_REACTION, R.id.dontLikeButton, R.id.dontLikeButtonCount);
        addReactionButton(reactionResumes, enjoy, I_ENJOY_REACTION, R.id.enjoyButton, R.id.enjoyButtonCount);
        addReactionButton(reactionResumes, bored, I_GETBORED_REACTION, R.id.getBoredButton, R.id.getBoredButtonCount);
        return reactionResumes;
    }

    private void addReactionButton(List<ReactionResume> reactionResumes, ReactionResume reactionResume,
                                   String reactionType, int reactionViewId, int countViewId) {
        reactionResume.setReactionType(reactionType);
        reactionResume.setViewId(reactionViewId);
        reactionResume.setCountViewId(countViewId);
        reactionResumes.add(reactionResume);
    }
}
