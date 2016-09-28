package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.coder.sample.R;
import com.com.pojo.AuditPoinDetailsPojo;

import java.util.ArrayList;

/**
 * Created by Ashitosh on 30-08-2015.
 */
public class AuditPointDetailsAdapter extends BaseAdapter {


    private ArrayList<AuditPoinDetailsPojo> list;
    private LayoutInflater inflater;
    private Context context;

    //update1.2

    public AuditPointDetailsAdapter(Context context, ArrayList<AuditPoinDetailsPojo> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row=view;
        AuditPoinDetailsPojo pojo = null;
        Holder holder=null;



        if (row == null) {
            row = inflater.inflate(R.layout.new_audit_detail_row, parent, false);

            holder=new Holder();

            holder.editText_remark = (EditText) row.findViewById(R.id.edit_text_audit_point_remark_row_new);
            holder.spinner_respones = (Spinner) row.findViewById(R.id.spinner_respones_row_new);
            holder.textView_audti_point = (TextView) row.findViewById(R.id.text_view_audit_point_row_new);
            row.setTag(holder);
        }else {
            holder = (Holder)row.getTag();
        }


        pojo=list.get(position);
        holder.textView_audti_point.setText(pojo.getAudit_point());
        holder.spinner_respones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


        /*holder.button_delete.setTag("" + position);

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt((String) v.getTag());

                list.remove(index);

                notifyDataSetChanged();
            }
        });*/





        return row;
    }

    static class Holder
    {
        /*TextView textView_audit_point, textView_respones, textView_remarks;
        Button button_delete;*/

    EditText editText_remark;
    Spinner spinner_respones;
    TextView textView_audti_point;

    }
}
