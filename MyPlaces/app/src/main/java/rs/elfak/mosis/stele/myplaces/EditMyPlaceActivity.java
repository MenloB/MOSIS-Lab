package rs.elfak.mosis.stele.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rs.elfak.mosis.stele.myplaces.classes.MyPlace;
import rs.elfak.mosis.stele.myplaces.classes.MyPlacesData;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener{

    boolean editMode = true;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);

        final Button finish = findViewById(R.id.edit_finished_button);
        finish.setOnClickListener(this);
        Button cancel = findViewById(R.id.edit_cancel_button);
        cancel.setOnClickListener(this);


        int position = getIntent().getIntExtra("position", -1);

        if (position < 0)
            editMode = false;

        this.position=position;

        EditText nameEdit = findViewById(R.id.name_edit);
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finish.setEnabled(s.length() > 0);
            }
        });

        Button getLocationButton = findViewById(R.id.location_button_edit);
        getLocationButton.setOnClickListener(this);

        if (!editMode) {
            finish.setEnabled(false);
            finish.setText("Add");
        } else {
            finish.setText("Save");
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            nameEdit.setText(place.getName());
            EditText desc = findViewById(R.id.description_edit);
            desc.setText(place.getDescription());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(resultCode == Activity.RESULT_OK)
            {
                String lon = data.getExtras().getString("lon");
                Log.e("LONGITUDE:", lon);
                EditText lonText = findViewById(R.id.long_edit);
                lonText.setText(lon);
                String lat = data.getExtras().getString("lat");
                EditText latText = findViewById(R.id.lat_edit);
                latText.setText(lat);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edit_finished_button:
                EditText etName = (EditText) findViewById(R.id.name_edit);
                EditText etDesc = findViewById(R.id.description_edit);
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();
                EditText etLat = (EditText) findViewById(R.id.lat_edit);
                EditText etLong = findViewById(R.id.long_edit);
                String lat = etLat.getText().toString();
                String longt = etLong.getText().toString();
                if(!editMode) {
                    MyPlace place = new MyPlace(name, desc);
                    place.setLatitude(lat);
                    place.setLongitude(longt);
                    MyPlacesData.getInstance().addNewPlace(place);
                }
                else
                {
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    place.setName(name);
                    place.setDescription(desc);
                    place.setLatitude(lat);
                    place.setLongitude(longt);
                    MyPlacesData.getInstance().updatePlace(place);
                }
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.edit_cancel_button:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.location_button_edit:
                Intent i = new Intent(this, MyPlacesMapsActivity.class);
                i.putExtra("state", MyPlacesMapsActivity.SELECT_COORDINATES);
                startActivityForResult(i, 1);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



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


            case R.id.show_map_item:
                Toast.makeText(this,"Show Map!",Toast.LENGTH_SHORT).show();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }
}
