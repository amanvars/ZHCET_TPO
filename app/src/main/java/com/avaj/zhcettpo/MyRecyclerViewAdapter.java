package com.avaj.zhcettpo;

/**
 * Created by aman_AV on 25-07-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avaj.zhcettpo.helper.SQLiteHandler;
import com.avaj.zhcettpo.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import static com.avaj.zhcettpo.R.layout.feed;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<HashMap<String,String>> mDataset;
    private static MyClickListener myClickListener;
    private Context mContext;
     private SQLiteHandler db;
     private SessionManager session ;
private String enrol;




    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView label;
        TextView dateTime,msg;
        ImageView  overflow;


        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView3);
            msg = (TextView) itemView.findViewById(R.id.textView2);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), View_feed.class);
                    v.getContext().startActivity(intent);
                    Toast.makeText(v.getContext(), "os version is:", Toast.LENGTH_SHORT).show();
                }
            });
*/
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(Context mContext,ArrayList<HashMap<String,String>> myDataset) {
        mDataset = myDataset;
        this.mContext = mContext;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(feed, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final HashMap<String,String> fd = mDataset.get(position);

        session= new SessionManager(mContext);
        db = new SQLiteHandler(mContext);

        enrol = session.session_enrol();

        if(session.isFBLoggedIn())
            enrol = session.session_uid();

        holder.label.setText(mDataset.get(position).get("feed_title"));
        holder.dateTime.setText(mDataset.get(position).get("feed_date"));
        holder.msg.setText(mDataset.get(position).get("feed_message"));

      /*  holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.overflow);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_feed);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.rem_feed:
                                //handle menu1 click



                                db.removeSingleFeed(fd.get("feed_rowid"),enrol);
                                // delete item from list
                                mDataset.remove(fd);

                                // adapter must be in global scope.
                                notifyDataSetChanged();

                               // deleteItem(position);

                                Snackbar.make(view, "Feed Deleted !!",
                                        Snackbar.LENGTH_LONG).show();
                                break;
                            default:
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                Intent intent = new Intent(v.getContext(), View_feed.class);
                intent.putExtra("rowid",mDataset.get(position).get("feed_rowid"));
                v.getContext().startActivity(intent);

               // Toast.makeText(v.getContext(),"" Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void addItem(HashMap<String,String> dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}