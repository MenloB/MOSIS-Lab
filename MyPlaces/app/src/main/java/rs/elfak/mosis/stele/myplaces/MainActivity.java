package rs.elfak.mosis.stele.myplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    static int NEW_PLACE=333;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_item:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                Toast.makeText(this,"About!",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.my_places_list_item:
                Toast.makeText(this,"My places!",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this,MyPlacesList.class);
                startActivity(intent1);
                return true;

            case R.id.new_place_item:
                Toast.makeText(this,"New place!",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(intent2,NEW_PLACE);
                return true;

            case R.id.show_map_item:
                Intent i = new Intent(this, MyPlacesMapsActivity.class);
                startActivity(i);
                Toast.makeText(this,"Show Map!",Toast.LENGTH_SHORT).show();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==NEW_PLACE)
            Toast.makeText(this,"New place added",Toast.LENGTH_SHORT).show();
    }
}
