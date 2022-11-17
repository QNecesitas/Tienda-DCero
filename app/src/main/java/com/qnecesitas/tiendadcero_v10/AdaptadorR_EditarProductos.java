package com.qnecesitas.tiendadcero_v10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class AdaptadorR_EditarProductos extends RecyclerView.Adapter<AdaptadorR_EditarProductos.EditarProductosViewHolder> {
    private Context context;
    private ArrayList<Modelo_EditarProductos> al_EditarProductos;
    private RecyclerTouchListener listener;


    public interface RecyclerTouchListener{
        void onClickItem(View v, int position);
    }

    public AdaptadorR_EditarProductos(Context context, ArrayList<Modelo_EditarProductos> modelo_EditarProductos){
        this.context=context;
        this.al_EditarProductos = modelo_EditarProductos;
    }


    @Override
    public EditarProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_editar_producto,null);
        return new EditarProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditarProductosViewHolder holder, int position){
        Modelo_EditarProductos modelo = al_EditarProductos.get(position);

        Glide.with(context)
                .load(Utiles.DIRECWEB_IMG+modelo.getNombre()+".jpg")
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imagenview);

        holder.nombre.setText(modelo.getNombre());
        holder.precio.setText(modelo.getPrecio()+" "+context.getString(R.string.Productos));

    }

    public void setClickListener(RecyclerTouchListener listener){
        this.listener=listener;
    }

    @Override
    public int getItemCount(){
        return al_EditarProductos.size();
    }


    class EditarProductosViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView precio;
        ImageView imagenview;
        ImageView visibilidad;

        public EditarProductosViewHolder(final View itemView){
            super(itemView);

            nombre=(TextView)itemView.findViewById(R.id.TV_nombre_del_producto);
            precio=(TextView)itemView.findViewById(R.id.TV_precio);
            imagenview=(ImageView)itemView.findViewById(R.id.IV_producto);
            visibilidad=(ImageView)itemView.findViewById(R.id.IV_visibilidad);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null) listener.onClickItem(itemView,getAdapterPosition());
                }
            });


        }

    }
}
