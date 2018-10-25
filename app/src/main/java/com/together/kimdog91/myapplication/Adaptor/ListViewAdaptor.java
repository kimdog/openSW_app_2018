package com.together.kimdog91.myapplication.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.together.kimdog91.myapplication.ListVO.ListVO;
import com.together.kimdog91.myapplication.MapsActivity;
import com.together.kimdog91.myapplication.MoimActivity;
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

        TextView mid = (TextView) convertView.findViewById(R.id.mid);
        TextView lon = (TextView) convertView.findViewById(R.id.lon);
        TextView lat = (TextView) convertView.findViewById(R.id.lat);
        TextView imageUrl = (TextView) convertView.findViewById(R.id.img_url);
        ImageView image = (ImageView) convertView.findViewById(R.id.img);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);

        ListVO listViewItem = listVO.get(position);

        final int _mid = listViewItem.getMid();
        final String _lon = listViewItem.getLon();
        final String _lat = listViewItem.getLat();
        final String _title = listViewItem.getTitle();

        // 아이템 내 각 위젯에 데이터 반영
        mid.setText(String.valueOf(listViewItem.getMid()));
        lon.setText(listViewItem.getLon());
        lat.setText(listViewItem.getLat());
        imageUrl.setText(listViewItem.getImgUrl());
        image.setImageBitmap(listViewItem.getImg());
        title.setText(listViewItem.getTitle());
        content.setText(listViewItem.getContent());


        // 리스트 뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, _mid + " : " + _title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MapsActivity.class);

                intent.putExtra("mid", _mid);
                intent.putExtra("title", _title);
                intent.putExtra("mlon", _lon);
                intent.putExtra("mlat", _lat);

                context.startActivity(intent);
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
    public void addVO(int mid, String lon, String lat, String imgUrl, Bitmap img, String title, String content) {
        ListVO item = new ListVO();

        item.setMid(mid);
        item.setLon(lon);
        item.setLat(lat);
        item.setImgUrl(imgUrl);
        item.setImg(img);
        item.setTitle(title);
        item.setContent(content);

        listVO.add(item);

    }

}
