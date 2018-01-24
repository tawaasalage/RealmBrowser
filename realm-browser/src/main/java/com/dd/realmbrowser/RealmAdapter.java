package com.dd.realmbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dd.realmbrowser.model.RealmPreferences;
import com.dd.realmbrowser.utils.MagicUtils;
import io.realm.RealmObject;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

class RealmAdapter extends RecyclerView.Adapter<RealmAdapter.ViewHolder> {

    public interface Listener {
        void onRowItemClicked(@NonNull RealmObject realmObject, @NonNull Field field);
    }

    private AbstractList<? extends RealmObject> mRealmObjects;
    private Context mContext;
    private List<Field> mFieldList;
    private Listener mListener;
    private RealmPreferences mRealmPreferences;

    public RealmAdapter(@NonNull Context context, @NonNull AbstractList<? extends RealmObject> realmObjects,
                        @NonNull List<Field> fieldList, @NonNull Listener listener) {
        mRealmPreferences = new RealmPreferences(context);
        mContext = context;
        mRealmObjects = realmObjects;
        mFieldList = fieldList;
        mListener = listener;
    }

    public void setFieldList(List<Field> fieldList) {
        mFieldList = fieldList;
    }

    @Override
    public RealmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realm_browser, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mRealmObjects.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.rb_grey));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.rb_white));
        }

        if (mFieldList.isEmpty()) {
            holder.txtIndex.setText(null);
            holder.txtColumn1.setText(null);
            holder.txtColumn2.setText(null);
            holder.txtColumn3.setText(null);
            holder.txtColumn4.setText(null);
        } else {
            holder.txtIndex.setText(String.valueOf(position));

            RealmObject realmObject = mRealmObjects.get(position);
            //initRowWeight(holder);
            //initRowTextWrapping(holder);

            if (mFieldList.size() == 1) {
                initRowText(holder.txtColumn1, realmObject, mFieldList.get(0));
                holder.txtColumn2.setText(null);
                holder.txtColumn3.setText(null);
            } else if (mFieldList.size() == 2) {
                initRowText(holder.txtColumn1, realmObject, mFieldList.get(0));
                initRowText(holder.txtColumn2, realmObject, mFieldList.get(1));
                holder.txtColumn3.setText(null);
            }  else if (mFieldList.size() == 3) {
                initRowText(holder.txtColumn1, realmObject, mFieldList.get(0));
                initRowText(holder.txtColumn2, realmObject, mFieldList.get(1));
                initRowText(holder.txtColumn3, realmObject, mFieldList.get(2));
            }
            else if (mFieldList.size() == 4) {
                initRowText(holder.txtColumn1, realmObject, mFieldList.get(0));
                initRowText(holder.txtColumn2, realmObject, mFieldList.get(1));
                initRowText(holder.txtColumn3, realmObject, mFieldList.get(2));
                initRowText(holder.txtColumn4, realmObject, mFieldList.get(3));
            }


        }
    }

    private void initRowText(TextView txtColumn, RealmObject realmObject, Field field) {
        if (MagicUtils.isParameterizedField(field)) {
            txtColumn.setText(MagicUtils.createParameterizedName(field));
            txtColumn.setOnClickListener(createClickListener(realmObject, field));
        } else {
            String methodName = MagicUtils.createMethodName(field);
            txtColumn.setText(MagicUtils.invokeMethod(realmObject, methodName));
            txtColumn.setOnClickListener(mEmptyClickListener);
        }
    }

    private View.OnClickListener mEmptyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener createClickListener(@NonNull final RealmObject realmObject, @NonNull final Field field) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRowItemClicked(realmObject, field);
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIndex;
        public TextView txtColumn1;
        public TextView txtColumn2;
        public TextView txtColumn3;
        public TextView txtColumn4;


        public ViewHolder(View v) {
            super(v);
            txtIndex = (TextView) v.findViewById(R.id.txtIndex);
            txtColumn1 = (TextView) v.findViewById(R.id.txtColumn1);
            txtColumn2 = (TextView) v.findViewById(R.id.txtColumn2);
            txtColumn3 = (TextView) v.findViewById(R.id.txtColumn3);
            txtColumn4  = (TextView) v.findViewById(R.id.txtColumn4);
        }
    }

}
