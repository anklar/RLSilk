package c.geonet.rlsilk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class StatActivity extends AppCompatActivity {
    private RequestQueue req;
    private TextView text;
    private String apiKey = "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        req = Volley.newRequestQueue(this.getApplicationContext());
        text = findViewById(R.id.text);

    }
    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick(R.id.button)
    public void ButtonClick(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,"https://api.rocketleaguestats.com/v1/data/platforms",null,(response)->{
            text.setText(response.toString());
        }, error->{
            android.util.Log.e("tstEx",error.getStackTrace().toString());
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU");
                return params;
            }
        };
        req.add(request);
    }

}
