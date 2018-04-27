package rs.elfak.mosis.stele.myplaces.classes;

import android.util.Log;

import java.util.ArrayList;

public class MyPlacesData {

    private ArrayList<MyPlace> myPlaces;

    MyPlacesDBAdapter dbAdapter;

    public MyPlacesData() {
        myPlaces=new ArrayList<>();

        //Log.e("myplacesdata", "kreiranje......");

        dbAdapter = new MyPlacesDBAdapter(MyPlacesApplication.getContext());
        dbAdapter.open();
        this.myPlaces=dbAdapter.getAllEntries();
        dbAdapter.close();


    }

    private static class SingletonHolder{
        public static final MyPlacesData instance = new MyPlacesData();
    }

    public static MyPlacesData getInstance(){
        return SingletonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces() {
        return myPlaces;
    }

    public void addNewPlace(MyPlace place){
        myPlaces.add(place);
        dbAdapter.open();
        long ID = dbAdapter.insertEntry(place);
        dbAdapter.close();
        place.setId(ID);
    }

    public MyPlace getPlace(int index)
    {
        return myPlaces.get(index);
    }

    public void deletePlace(int index){
        MyPlace place = myPlaces.remove(index);
        dbAdapter.open();
        boolean success = dbAdapter.removeEntry(place.getId());
        dbAdapter.close();
    }

    public void updatePlace(MyPlace place)
    {
        dbAdapter.open();
        dbAdapter.updateEntry(place.getId(), place);
        dbAdapter.close();
    }

}
