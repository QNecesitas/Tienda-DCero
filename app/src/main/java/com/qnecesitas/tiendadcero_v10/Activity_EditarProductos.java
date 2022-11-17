package com.qnecesitas.tiendadcero_v10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class Activity_EditarProductos extends AppCompatActivity {

    //Recycler
    private RecyclerView recycler;


    //Internet
    private ProgressDialog progressDialogAdding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_productos);

        //Toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.activity_personalizar_menu_appBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


    }

    public void click_FABadd(View view) {
        liAddElement();
    }


    //Agregar elemento al menu
    public void personalizar_menu_click_fab(View view) {
        if(array_menus.size()<100) {
            liAgregarElemento();
        }else{
            showAlertDialogToMuch();
        }
    }
    private void liAddElement(){
        LayoutInflater layoutInflater = LayoutInflater.from(Activity_EditarProductos.this);
        View view = layoutInflater.inflate(R.layout.li_editproduct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_EditarProductos.this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //Declaraciones
        ImageView imageView=(ImageView) view.findViewById(R.id.Image_edit_product);
        EditText et_nombre=(EditText)view.findViewById(R.id.ET_Nombre);
        EditText et_precio=(EditText)view.findViewById(R.id.ET_Precio);
        EditText et_desc=(EditText) view.findViewById(R.id.ET_Desc);
        Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
        Button btn_add=(Button)view.findViewById(R.id.btn_add);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utiles.Internet.isOnline(Activity_EditarProductos.this, true)) {
                    if (!et_nombre.getText().toString().trim().isEmpty()) {
                        if(!et_precio.getText().toString().trim().isEmpty()) {
                            if (!isRepetidoElNombre(et_nombre.getText().toString())) {
                                alertDialog.dismiss();
                                if (imageView.getDrawable() != null) {
                                    progressDialogAdding = ProgressDialog.show(Activity_EditarProductos.this, getString(R.string.Agregando_producto), getString(R.string.Espere), false, false);
                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    uploadImageAdd(bitmap, et_nombre.getText().toString(),et_precio.getText().toString());
                                } else {
                                    progressDialogAdding = ProgressDialog.show(Activity_Personalizar_Menu.this, getString(R.string.actualizando_info), getString(R.string.por_favor_espere), false, false);
                                    uploadInfoAdd(et_nombre.getText().toString(),et_precio.getText().toString());
                                }
                            } else {
                                et_nombre.setError(getString(R.string.existe_ya_producto_con_este_nombre));
                            }
                        }else{
                            et_precio.setError(getString(R.string.campo_vacio));
                        }
                    } else {
                        et_nombre.setError(getString(R.string.campo_vacio));
                    }
                }
            }
        });


        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void uploadImageAdd(Bitmap bitmap, String nombreCategoria, String precio){

        Bitmap BitRec = Bitmap.createScaledBitmap(bitmap, ANCHO_DE_FOTO_A_SUBIR, ALTO_DE_FOTO_A_SUBIR, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitRec.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utiles.DIRECWEB_IMG + "_SubirImagen.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                uploadInfoAdd(nombreCategoria, precio);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogSubiendo.dismiss();
                showAlertDialogNoInternet();
                //alert dialog de error
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = ConvertImage;
                String nombre = "Menu_"+nombreCategoria;

                Map<String, String> params = new Hashtable<String, String>();
                params.put("imagen", imagen);
                params.put("nombre", nombre);

                return params;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);
    }
    private void uploadInfoAdd(String nombre, String precio){
        RequestQueue requestQAll = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.GET, Utiles.DIRECWEB_ARCHIVOSPHP+"ColocarMenu.php?Producto="+nombre+"&Precio="+precio+"&Categoria="+categoriaSeleccionada+"&Subcategoria="+subcategoriaSeleccionada , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialogSubiendo.dismiss();
                array_menus.add(new Pila_Menu(nombre,Double.parseDouble(precio), 1));
                actualizarRecyclerParaMenu();
            }
        },

                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialogSubiendo.dismiss();
                        showAlertDialogNoInternet();
                    }
                });
        requestQAll.add(stringRequest);
    }
    public void showAlertDialogToMuch() {
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(Activity_Personalizar_Menu.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.demasiados_elementos);
        builder.setMessage(R.string.excedido_numero_maximo_elementos);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }

    //Auxiliares
    private void escogerimagenGaleria() {

        if (Utiles.Permisos.siHayPermisoDeAlmacenamiento(getApplicationContext())) {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, INTENT_RESULT_GALERIA);

        } else {
            Utiles.Permisos.pedirPermisoDeAlmacenamiento(Activity_Personalizar_Menu.this, PERMISO_GALERIA);
        }

    }
    private void recortarImagen(Uri uri1, Uri uri2) {
        try {
            UCrop.of(uri1, uri2)
                    .withAspectRatio(3, 3)
                    .withMaxResultSize(ANCHO_DE_FOTO_A_SUBIR, ALTO_DE_FOTO_A_SUBIR)
                    .start(Activity_Personalizar_Menu.this);
        } catch (Exception e) {
            Toast.makeText(Activity_Personalizar_Menu.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isRepetidoElNombre(String nombre){
        boolean repetido=false;
        for (int x=0;x<array_menus.size();x++){
            if(array_menus.get(x).getNombre().equalsIgnoreCase(nombre)){
                repetido=true;
            }
        }
        return repetido;
    }

}