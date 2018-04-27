package rs.elfak.mosis.stele.myplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import rs.elfak.mosis.stele.myplaces.classes.MyPlace;
import rs.elfak.mosis.stele.myplaces.classes.MyPlacesData;

public class MyPlacesList extends AppCompatActivity implements View.OnCreateContextMenuListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);


        ListView listView = findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyPlace place = (MyPlace) parent.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), place.getName() + " selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyPlacesList.this,ViewMyPlaceActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                MyPlace place = MyPlacesData.getInstance().getPlace(info.position);
                menu.setHeaderTitle(place.getName());
                menu.add(0,1,1,"View place");
                menu.add(0,2,2,"Edit place");
                menu.add(0,3,3,"Delete place");

            }
        });


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId())
        {
            case 1:
                intent=new Intent(this,ViewMyPlaceActivity.class);
                intent.putExtra("position", info.position);
                startActivity(intent);
                break;
            case 2:
                intent=new Intent(this,EditMyPlaceActivity.class);
                intent.putExtra("position", info.position);
                startActivityForResult(intent, 1);
                break;
            case 3:
                MyPlacesData.getInstance().deletePlace(info.position);
                setList();
                break;
        }

        return super.onContextItemSelected(item);

    }

    private void setList()
    {
        ListView myPlacesList = findViewById(R.id.list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_places_list, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.about_item:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.new_place_item:
                Toast.makeText(this, "New place!", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(intent2, MainActivity.NEW_PLACE);
                return true;

            case R.id.show_map_item:
                Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show();
                return true;

            default:

                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MainActivity.NEW_PLACE && resultCode==RESULT_OK)
        {
            ListView listView = findViewById(R.id.list);
            listView.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));

        }
    }
}
