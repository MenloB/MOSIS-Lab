package rs.elfak.mosis.stele.myplaces.classes;

import android.app.Application;
import android.content.Context;

public class MyPlacesApplication extends Application {
    private static MyPlacesApplication instance;

    public MyPlacesApplication()
    {
        instance=this;
    }

    public static Context getContext()
    {
        return instance;
    }
}
