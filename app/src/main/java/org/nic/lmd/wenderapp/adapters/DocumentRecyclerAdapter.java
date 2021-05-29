package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.uploadFileUtility.PdfOpenHelper;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;


/**
 * Created by chandan on 30.09.2020
 */

public class DocumentRecyclerAdapter extends RecyclerView.Adapter<DocumentRecyclerAdapter.MyViewHolder> {

    private JSONArray jsonArray;
    Activity activity;
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_head_doc,text_doc_name;
        public MyViewHolder(View view) {
            super(view);
            text_head_doc =  view.findViewById(R.id.text_head_doc);
            text_doc_name =  view.findViewById(R.id.text_doc_name);

        }
    }


    public DocumentRecyclerAdapter(JSONArray jsonArray, Activity activity) {
        this.jsonArray = jsonArray;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            JSONObject obj=jsonArray.getJSONObject(position);
            if (position==0) {
                holder.text_head_doc.setVisibility(View.VISIBLE);
                holder.text_head_doc.setText("Uploaded Documents :-");
            }else{
                holder.text_head_doc.setVisibility(View.GONE);
            }
            /*String[] tokens=obj.getString("docUrl").split("/");
            holder.text_doc_name.setText(""+tokens[tokens.length-1]);
            holder.text_doc_name.setText(Html.fromHtml("<a href=http://192.168.1.20:65001/app/vendor"+obj.getString("docUrl")+tokens[tokens.length-1]+"</a>"));
            */
            String[] tokens=obj.getString("docUrl").split("/");
            SpannableString content = new SpannableString(obj.getString("documentId")+" "+tokens[tokens.length-1]);
            content.setSpan(new UnderlineSpan(), 0,content.length(), 0);
            holder.text_doc_name.setText(content);
            holder.text_doc_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //activity.startActivity(new Intent(activity, LoadUrlActivity.class).putExtra("url","http://192.168.1.20:65001/app/vendor"+obj.docUrl));
                    try {
                        PdfOpenHelper.openPdfFromUrl(Urls_this_pro.baseURL + obj.getString("docUrl"), activity);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            setAnimation(holder.itemView, position);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
                     Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);
                     viewToAnimate.startAnimation(animation);
                     lastPosition = position;
        }
    }
}