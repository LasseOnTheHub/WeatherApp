package com.grp8.weatherapp.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Data.IDataRepository;

/*
 * Created by Thomas on 15-Jan-17.
 */
public class UserManager
{
    private static UserManager instance;

    private static final String KEY_USER_ID = "user-id";

    private SharedPreferences prefs;

    private int     userID    = 0;
    private boolean validated = false;

    private UserManager(Context context)
    {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static UserManager getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new UserManager(context);
        }

        return instance;
    }

    public int getUserID()
    {
        return this.userID;
    }

    public boolean isValid()
    {
        if(validated)
        {
            return true;
        }

        try
        {
            int id = this.prefs.getInt(KEY_USER_ID, 0);

            if(id == 0)
            {
                return false;
            }

            userID    = id;
            validated = true;

            return true;
        }
        catch(ClassCastException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void resume(Context context)
    {
        int id = this.getUserID();

        DataRepositoryFactory.build(context).setUser(id);

        this.userID    = id;
        this.validated = true;
    }

    public boolean login(Context context, int id)
    {
        IDataRepository repository = DataRepositoryFactory.build(context);

        if(repository.authorize(id))
        {
            SharedPreferences.Editor editor = this.prefs.edit();

            editor.putInt(KEY_USER_ID, id);
            editor.apply();

            this.userID    = id;
            this.validated = true;

            repository.setUser(id);

            return true;
        }

        return false;
    }

    public void logout()
    {
        this.prefs.edit().remove(KEY_USER_ID).apply();

        this.userID    = 0;
        this.validated = false;
    }

}
