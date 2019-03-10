package com.project.letsmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FindUserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton searchButton;
    private EditText searchInputText;
    //    private RecyclerView searchResultList;
//    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference allUsersDatabaseRef;
    private List<User> userList;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mToolbar = (Toolbar) findViewById(R.id.find_user_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Leet's Meet");
        listView = (ListView) findViewById(R.id.listview);
        userList = new ArrayList<>();

//        searchResultList = (RecyclerView) findViewById(R.id.search_result_list);
//        searchResultList.setHasFixedSize(true);
//        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchInputText = (EditText) findViewById(R.id.search_box);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String searchText = searchInputText.getText().toString();
                searchUsers(searchText);
            }
        });
    }

    private void searchUsers(String searchText) {
        Query query = allUsersDatabaseRef.orderByChild("firstName").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        User user1 = data.getValue(User.class);
                        if(user1 != null ){
                            User user = new User();
                            user.setEmail(user1.getEmail());
                            user.setFirstName(user1.getFirstName());
                            user.setLastName(user1.getLastName());
                            user.setImage(user1.getImage());
                            userList.add(user);
                        }

                    }
                    UserAdapter adapter = new UserAdapter(FindUserActivity.this, R.layout.adapter_view_layout, userList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedEmail = ((TextView)view.findViewById(R.id.all_users_email)).getText().toString();
                            killActivity(selectedEmail);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            public void killActivity(String email) {
                Intent data = new Intent();
                data.putExtra("myData1", email);
                //data.putExtra("myData2", latLng.longitude);
// Activity finished ok, return the data
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}