package breathe.inventerous.com.breathe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;




/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */









//
//          MAIN INTERFACE STARTS HERE
///
////
public class Actual_Interface extends AppCompatActivity {

    private Button btnForecast,btnSearchLoc,btnShopNow;
    private ViewGroup mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual__interface);
        mListView = (ViewGroup) findViewById(R.id.heatMaps);

        btnSearchLoc=(Button) findViewById(R.id.btnSearchLoc);
        btnForecast=(Button) findViewById(R.id.btnForecast);
        btnShopNow=(Button) findViewById(R.id.btnShop);

        btnForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to forecast activity
                Intent intentToForecast=new Intent(Actual_Interface.this,forecast.class);
                startActivity(intentToForecast);
            }
        });

        btnShopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to ShopNow activity
            }
        });


    }



}
