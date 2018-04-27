package rs.elfak.mosis.stele.myplaces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rs.elfak.mosis.stele.myplaces.classes.MyPlace;
import rs.elfak.mosis.stele.myplaces.classes.MyPlacesData;

public class ViewMyPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_place);

        int position = getIntent().getIntExtra("position", -1);

        if(position>=0)
        {
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            TextView name = findViewById(R.id.name_text);
            TextView desc = findViewById(R.id.description_text);
            TextView lat = findViewById(R.id.latitude_text);
            TextView lon = findViewById(R.id.longitude_text);

            name.setText(place.getName());
            desc.setText(place.getDescription());
            lat.setText(place.getLatitude());
            lon.setText(place.getLongitude());
        }

        Button btnOK = findViewById(R.id.view_ok_button);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
