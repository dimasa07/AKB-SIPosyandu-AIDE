package com.akb.siposyandu.adapters;

import android.support.v7.widget.RecyclerView;
import java.util.List;
import com.akb.siposyandu.models.*;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import com.akb.siposyandu.R;
import java.text.NumberFormat;
import java.util.Locale;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.content.Context;
import android.util.Log;
import com.akb.siposyandu.constants.*;
import com.akb.siposyandu.fragments.*;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import android.os.Environment;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.*;
import org.json.*;
import android.app.*;
import android.content.*;

public class AdapterKegiatan extends RecyclerView.Adapter<AdapterKegiatan.ViewHolderData> 
implements View.OnClickListener,View.OnLongClickListener{
	private List<Kegiatan> dataList;
	KegiatanFragment fragment;

	public AdapterKegiatan(KegiatanFragment fragment, List<Kegiatan> dataList){
		this.dataList = dataList;
		this.fragment = fragment;
		Log.d(getClass().toString(), "Kegiatan list: " + dataList.size());
	}

	@Override
	public AdapterKegiatan.ViewHolderData onCreateViewHolder(ViewGroup p1, int p2){
		LayoutInflater inflater = LayoutInflater.from(p1.getContext());
		View view = inflater.inflate(R.layout.card_kegiatan, p1, false);
		view.setOnClickListener(this);
		view.setOnLongClickListener(this);
		return new ViewHolderData(view);
	}

	@Override
	public void onBindViewHolder(AdapterKegiatan.ViewHolderData holder, int position){
		holder.txtNama.setText("Nama : " + dataList.get(position).getNama());
		holder.txtDeskripsi.setText("Deskripsi : " + dataList.get(position).getDeskripsi());
	}

	@Override
	public int getItemCount(){
		return (dataList != null) ?dataList.size() : 0;
	}

	@Override
	public void onClick(View p1){
		int itemPosition = fragment.recyclerView.getChildAdapterPosition(p1);
		Toast.makeText(
			fragment.activity.getApplicationContext(),
			dataList.get(itemPosition).getNama(), Toast.LENGTH_SHORT
		).show();
	}

	@Override
	public boolean onLongClick(View p1){
		final int itemPosition = fragment.recyclerView.getChildAdapterPosition(p1);

		AlertDialog.Builder alert = new AlertDialog.Builder(fragment.getActivity());
		alert.setTitle("Hapus Kegiatan");
		alert.setMessage("Yakin ingin hapus ?");
		alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2){
					AndroidNetworking.post(ConstantVariables.API + "hapus_kegiatan.php")
						.addBodyParameter("id", dataList.get(itemPosition).getIdKegiatan())
						.setPriority(Priority.MEDIUM)
						.build()
						.getAsJSONObject((new JSONObjectRequestListener(){
							@Override
							public void onResponse(JSONObject p1){
								try{
									String message = p1.getString("message");
									fragment.restoreData();
									Toast.makeText(fragment.activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
								}catch(JSONException e){
										Toast.makeText(fragment.activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();			
								}
							}

							@Override
							public void onError(ANError p1){
								Toast.makeText(fragment.activity.getApplicationContext(), p1.getMessage(), Toast.LENGTH_LONG).show();
							}
					}));
				}
			});
		alert.setNegativeButton(android.R.string.no, null);
		alert.show();


		return true;
	}

	class ViewHolderData extends RecyclerView.ViewHolder{

		private TextView txtNama;
		private TextView txtDeskripsi;
		private CardView card_kegiatan;

		ViewHolderData(View itemView){
			super(itemView);
			txtNama = itemView.findViewById(R.id.card_kegiatan_nama);
			txtDeskripsi = itemView.findViewById(R.id.card_kegiatan_deskripsi);
			card_kegiatan = itemView.findViewById(R.id.card_kegiatan);
		}
	}
}
