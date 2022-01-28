package com.phoenixro026.ftcfreightfrenzyscoreronline.recycleview;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.phoenixro026.ftcfreightfrenzyscoreronline.database.AppDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.database.Match;
import com.phoenixro026.ftcfreightfrenzyscoreronline.database.MatchDao;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class MatchRepository {

    private final MatchDao mMatchDao;
    private final LiveData<List<Match>> mAllMatches;
    private final LiveData<List<Match>> mAllMatchesDesc;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    MatchRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mMatchDao = db.matchDao();
        mAllMatches = mMatchDao.getAll();
        mAllMatchesDesc = mMatchDao.getAllDesc();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Match>> getAllMatches() {
        return mAllMatches;
    }

    LiveData<List<Match>> getAllMatchesDesc() {
        return mAllMatchesDesc;
    }

    void deleteByUserId(int userId) {
        AppDatabase.databaseWriteExecutor.execute(() -> mMatchDao.deleteByUserId(userId));
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Match match) {
        AppDatabase.databaseWriteExecutor.execute(() -> mMatchDao.insert(match));
    }

    void update(Match match) {
        AppDatabase.databaseWriteExecutor.execute(() -> mMatchDao.update(match));
    }
}
