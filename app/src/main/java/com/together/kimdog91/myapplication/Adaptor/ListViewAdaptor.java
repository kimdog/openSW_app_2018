package com.together.kimdog91.myapplication.Adaptor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.together.kimdog91.myapplication.ListVO.ListVO;
import com.together.kimdog91.myapplication.R;

import java.util.ArrayList;

public class ListViewAdaptor extends BaseAdapter {

    private ArrayList<ListVO> listVO = new ArrayList<ListVO>();

    public ListViewAdaptor() {

    }

    @Override
    public int getCount() {
        return listVO.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // position : ListView의 위치, 첫번째 0
        final int pos = position;
        final Context context = parent.getContext();

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.img);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);

        ListVO listViewItem = listVO.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        image.setImageDrawable(listViewItem.getImg());
        title.setText(listViewItem.getTitle());
        content.setText(listViewItem.getContent());

        // 리스트 뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, (pos+1)+"번째 데이터", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listVO.get(position);
    }

    // 데이터값 넣어줌
    public void addVO(Drawable icon, String title, String content) {
        ListVO item = new ListVO();

        item.setImg(icon);
        item.setTitle(title);
        item.setContent(content);

        listVO.add(item);

    }

}
