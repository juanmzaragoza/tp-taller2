package tallerii.stories.controller;

import android.util.Log;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.network.apimodels.ServerError;

public abstract class DefaultCallback<T> implements Callback<T> {
    private StoriesAppActivity activity;
    private Gson gson = new Gson();

    public DefaultCallback(StoriesAppActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onResponse(response);
    }

    protected abstract void onResponse(Response<T> response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("Error", t.toString());
        activity.showMessage(t.toString());
    }

    public void manageErrors(Response<T> response) {
        assert response.errorBody() != null;
        try {
            ServerError error = gson.fromJson(response.errorBody().string(), ServerError.class);
            activity.showMessage(error.getMessage());
        } catch (Exception e) {
            activity.showMessage(e.getMessage());
        }
    }
}
