package com.example.optimusnote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> /*implements View.OnClickListener*/ {

    private File[] allFiles;
    private TimeAgo timeAgo;
    private onItemListClick onItemListClick;
    //private Context rContext;
    private OnItemOptionListener onItemOptionListener = null;

    int i;
    AudioListAdapter(File[] allFiles,onItemListClick onItemListClick){
        this.allFiles = allFiles;
        this.onItemListClick = onItemListClick;

    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
        timeAgo = new TimeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, final int position) {
        holder.list_title.setText(allFiles[position].getName());
        holder.list_date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
        //menu

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showMenu(v,position);

            }
        });

        /*holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                Fragment fragment = new AudiolistFragment();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.audiolistFragment,fragment).addToBackStack(null).commit();
            }
        });*/


    }



    public interface OnItemOptionListener {
        void onItemOptionSelected(int menuId, File item);
    }





    private void showMenu(final View v, final int pos) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (onItemOptionListener != null && allFiles.length > pos) {
                    onItemOptionListener.onItemOptionSelected(item.getItemId(), allFiles[pos]);
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.file_menu, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menu_delete:
                        allFiles[pos].delete();
                        //notifyItemRemoved(pos);
                        //notifyItemRangeChanged(pos,allFiles.length);
                        Intent t = new Intent(v.getContext(),VoiceMainActivity.class);
                        v.getContext().startActivity(t);

                        

                        break;

                }
                return false;
            }
        });

    }

    public void setOnItemListClick(AudioListAdapter.onItemListClick onItemListClick) {
        this.onItemListClick = onItemListClick;
    }

    /*public void refresh(View view){
        AppCompatActivity activity =(AppCompatActivity) view.getContext();
        Fragment fragment = new AudiolistFragment();

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }*/

    //public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.menu_delete:


                break;
        }*/

   // }



    void setOnItemOptionListener(OnItemOptionListener onItemOptionListener) {
        this.onItemOptionListener = onItemOptionListener;
    }



    //Menu with delete



    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    /*public interface OnItemOptionListener {
        void onItemOptionSelected(int menuId, File item);
    }*/

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title;
        private TextView list_date;
        private ImageButton btnMore;


        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.list_image_view);
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            btnMore = itemView.findViewById(R.id.list_item_more);
            //btndel = itemView.findViewById(R.id.menu_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListClick.onClickListner(allFiles[getAdapterPosition()],getAdapterPosition());
        }


    }

    public interface clickOn{
        void click(View v,int position);
    }



    public interface onItemListClick{
        void onClickListner(File file,int position);

    }
}
