package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Anirudh Swami on 21-07-2016 for the project PersonalAssistant.
 */
public class CostCustomAdapter extends ArrayAdapter<String> {

    public String[] cos,mons,yrs;
    public String rol;

    public CostCustomAdapter(Context context, String[] buds, String[] costs, String[] months, String[] years, String rool) {
        super(context, R.layout.costm_custom_row,buds);
        this.cos = costs;
        this.mons = months;
        this.yrs = years;
        this.rol = rool;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater aniInflater = LayoutInflater.from(getContext());
        View customView = aniInflater.inflate(R.layout.contact_custom_row, parent, false);
        final String bud = getItem(position);

        TextView buddy = (TextView) customView.findViewById(R.id.budm);
        TextView costy = (TextView) customView.findViewById(R.id.costm);
        TextView monthy = (TextView) customView.findViewById(R.id.monthm);
        TextView yeary = (TextView) customView.findViewById(R.id.yearm);

        buddy.setText(bud);
        costy.setText(cos[position]);
        monthy.setText(mons[position]);
        yeary.setText(yrs[position]);

        return customView;
        //return super.getView(position, convertView, parent);
    }
}
