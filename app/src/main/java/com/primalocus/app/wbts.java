package com.primalocus.app;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.google.gson.Gson;
import com.primalocus.app.loginPOJO.loginBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class wbts extends Fragment {

    private static final String TAG = "asdsa";
    Spinner landusage,under_construction, warehouse, construction, firenoc, safety, ventilation, insulation, leveler, agreement, flooring , floor;

    List<String> lan, pos, und, war, con, pli, fir, saf, ven, ins, lev, agr, flo , flo1;
    Button submit, add , add1 , add2;

    Spinner renttype , commontype , plottype , plinth , coveredtype , availabletype;

    EditText posession, flooringtext , plinthtext  ,coveredunit  ,availableunit;

    TextView postitle , under_constructiontitle , constructiontitle , pricetitle , coveredtitle , minimumtitle , tenantnametitle , agreementtitle;
    TextView availabletitle , partitiontitle , renttitle , securitytitle , commontitle , eavestitle , center_heighttitle;
    TextView opening_dockstitle , plinthtitle , plantitle , firenoctitle , safetytitle , ventilationtitle , insulationtitle , levelertitle , dockleverernumbertitle;
    RelativeLayout firenoclayout , safetylayout , ventilationlayout , insulationlayout, levelerlayout ,agreementhide;
    RelativeLayout constructionlayout , warehouselayout , under_constructionlayout;
    LinearLayout price , plothide  ,coveredhide  ,availablehide;
    String lann, unde, ware, cond, fire, safe, vent, insu, leve, aggr, floo , floo1 , plin;
    EditText min, max, plot, covered, available, rent, security, common, eaves, center_height, opening_docks, tenantname , dockleverernumber , minimum , land , plotunit , brands;
    EditText fwh, large, mobile, secondary, owned, email, caretaker, caretakerphone, emailcaretaker, remarks;
    RecyclerView images;
    GridLayoutManager manager;
    RadioGroup plan, partition, tenant;
    int itemcount = 0;

    File f1;
    Uri uri;

    File f2;
    Uri uri2;

    //String avai , type , date , pid , lat , lng , ds , st , ci;

    //EditText location , address;

    List<MultipartBody.Part> list;

    ImageView image1;

    List<Uri> ulist;

    ImageAdapter adapter;
    ContactAdapter adapter222;

    List<contactBean> lll;

    RecyclerView contacts;

    TextView imagecount;


    Warehouse wcontext;

    void setData(Warehouse wcontext)
    {
        this.wcontext = wcontext;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wbts , container , false);

        list = new ArrayList<>();
        ulist = new ArrayList<>();
        lll = new ArrayList<>();
        pli = new ArrayList<>();

        lan = new ArrayList<>();
        pos = new ArrayList<>();
        und = new ArrayList<>();
        war = new ArrayList<>();
        con = new ArrayList<>();
        fir = new ArrayList<>();
        saf = new ArrayList<>();
        ven = new ArrayList<>();
        ins = new ArrayList<>();
        lev = new ArrayList<>();
        agr = new ArrayList<>();
        flo = new ArrayList<>();
        flo1 = new ArrayList<>();

        contacts = view.findViewById(R.id.contacts);
        coveredtype = view.findViewById(R.id.coveredtype);
        coveredunit = view.findViewById(R.id.coveredunit);
        coveredhide = view.findViewById(R.id.coveredhide);
        availabletype = view.findViewById(R.id.availabletype);
        availableunit = view.findViewById(R.id.availableunit);
        availablehide = view.findViewById(R.id.availablehide);
        brands = view.findViewById(R.id.brands);
        agreementtitle = view.findViewById(R.id.agreementtitle);
        flooringtext = view.findViewById(R.id.flooringtext);
        plothide = view.findViewById(R.id.plothide);
        plottype = view.findViewById(R.id.plottype);
        plotunit = view.findViewById(R.id.plotunit);
        floor = view.findViewById(R.id.floor);
        agreementhide = view.findViewById(R.id.agreementhide);
        minimumtitle = view.findViewById(R.id.minimumtitle);
        minimum = view.findViewById(R.id.minimum);
        add1 = view.findViewById(R.id.add1);
        add2 = view.findViewById(R.id.add2);
        image1 = view.findViewById(R.id.image1);

        postitle = view.findViewById(R.id.postitle);
        land = view.findViewById(R.id.land);
        renttype = view.findViewById(R.id.renttype);
        commontype = view.findViewById(R.id.commontype);
        tenantnametitle = view.findViewById(R.id.tenantnametitle);
        imagecount = view.findViewById(R.id.imagecount);

        landusage = view.findViewById(R.id.landusage);
        add = view.findViewById(R.id.add);
        dockleverernumber = view.findViewById(R.id.dockleverernumber);
        dockleverernumbertitle = view.findViewById(R.id.dockleverernumbertitle);

        min = view.findViewById(R.id.min);
        max = view.findViewById(R.id.max);
        posession = view.findViewById(R.id.posession);
        plinthtext = view.findViewById(R.id.plinthtext);
        under_construction = view.findViewById(R.id.under_construction);
        warehouse = view.findViewById(R.id.warehouse);
        covered = view.findViewById(R.id.covered);
        construction = view.findViewById(R.id.construction);
        rent = view.findViewById(R.id.min);
        security = view.findViewById(R.id.security);
        common = view.findViewById(R.id.common);
        plinth = view.findViewById(R.id.plinth);
        firenoc = view.findViewById(R.id.firenoc);
        tenantname = view.findViewById(R.id.tenantname);

        safety = view.findViewById(R.id.safety);
        ventilation = view.findViewById(R.id.ventilation);
        insulation = view.findViewById(R.id.insulation);
        leveler = view.findViewById(R.id.leveler);
        agreement = view.findViewById(R.id.agreement);
        flooring = view.findViewById(R.id.flooring);
        mobile = view.findViewById(R.id.mobile);
        secondary = view.findViewById(R.id.secondary);
        owned = view.findViewById(R.id.owned);
        email = view.findViewById(R.id.email);
        caretaker = view.findViewById(R.id.caretaker);
        caretakerphone = view.findViewById(R.id.caretakerphone);
        emailcaretaker = view.findViewById(R.id.emailcaretaker);
        remarks = view.findViewById(R.id.remarks);

        under_constructiontitle = view.findViewById(R.id.under_constructiontitle);
        constructiontitle = view.findViewById(R.id.constructiontitle);
        pricetitle = view.findViewById(R.id.pricetitle);
        coveredtitle = view.findViewById(R.id.coveredtitle);
        availabletitle = view.findViewById(R.id.availabletitle);
        partitiontitle = view.findViewById(R.id.partitiontitle);
        renttitle = view.findViewById(R.id.renttitle);
        securitytitle = view.findViewById(R.id.securitytitle);
        commontitle = view.findViewById(R.id.commontitle);
        eavestitle = view.findViewById(R.id.eavestitle);
        center_heighttitle = view.findViewById(R.id.center_heighttitle);
        opening_dockstitle = view.findViewById(R.id.opening_dockstitle);
        plinthtitle = view.findViewById(R.id.plinthtitle);
        plantitle = view.findViewById(R.id.plantitle);
        firenoctitle = view.findViewById(R.id.firenoctitle);
        safetytitle = view.findViewById(R.id.safetytitle);
        ventilationtitle = view.findViewById(R.id.ventilationtitle);
        insulationtitle = view.findViewById(R.id.insulationtitle);
        levelertitle = view.findViewById(R.id.levelertitle);

        firenoclayout = view.findViewById(R.id.firenoclayout);
        safetylayout = view.findViewById(R.id.safetylayout);
        ventilationlayout = view.findViewById(R.id.ventilationlayout);
        insulationlayout = view.findViewById(R.id.insulationlayout);
        levelerlayout = view.findViewById(R.id.levelerlayout);
        price = view.findViewById(R.id.price);
        constructionlayout = view.findViewById(R.id.constructionlayout);
        warehouselayout = view.findViewById(R.id.warehouselayout);
        under_constructionlayout = view.findViewById(R.id.under_constructionlayout);
        images = view.findViewById(R.id.images);
        plot = view.findViewById(R.id.plot);
        partition = view.findViewById(R.id.partition);
        tenant = view.findViewById(R.id.tenant);
        available = view.findViewById(R.id.available);
        eaves = view.findViewById(R.id.eaves);
        center_height = view.findViewById(R.id.center_height);
        opening_docks = view.findViewById(R.id.opening_docks);
        fwh = view.findViewById(R.id.fwh);
        large = view.findViewById(R.id.large);
        plan = view.findViewById(R.id.plan);

        submit = view.findViewById(R.id.button);

        partition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.yes) {
                    minimum.setVisibility(View.VISIBLE);
                    minimumtitle.setVisibility(View.VISIBLE);
                    minimum.setText("");
                } else {
                    minimum.setVisibility(View.GONE);
                    minimumtitle.setVisibility(View.GONE);
                    minimum.setText("-");
                }

            }
        });

        tenant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.occupied) {
                    tenantnametitle.setVisibility(View.VISIBLE);
                    tenantname.setVisibility(View.VISIBLE);
                    tenantname.setText("");
                } else {
                    tenantnametitle.setVisibility(View.GONE);
                    tenantname.setVisibility(View.GONE);
                    tenantname.setText("-");
                }

            }
        });

        adapter = new ImageAdapter(getActivity(), list);
        manager = new GridLayoutManager(getContext(), 3);
        images.setAdapter(adapter);
        images.setLayoutManager(manager);


        adapter222 = new ContactAdapter(getActivity() , lll);
        GridLayoutManager manager1 = new GridLayoutManager(getContext() , 1);
        contacts.setAdapter(adapter222);
        contacts.setLayoutManager(manager1);

        lan.add("Warehousing");
        lan.add("Industrial");
        lan.add("Agricultural");
        lan.add("Lal Dora");
        lan.add("Others");

        pli.add("Ground");
        pli.add("1 ft.");
        pli.add("2 Ft.");
        pli.add("4 ft.");
        pli.add("Others");

        pos.add("-0 to 2 months");
        pos.add("-2 to 4 months");
        pos.add("-4 to 6 months");
        pos.add(">6 months");

        und.add("-0 to 2 months");
        und.add("-2 to 4 months");
        und.add("-4 to 6 months");
        und.add(">6 months");

        war.add("stand-alone");
        war.add("campus/cluster");

        con.add("PEB Shed");
        con.add("Old Shed (Asbestos)");
        con.add("RCC");
        //con.add("RCC & Old Shed Combined");
        //con.add("RCC + PEB");


        fir.add("Available");
        fir.add("Not Available");
        fir.add("Agreed To Procure");

        saf.add("Fire Sprinkler + Hydrant");
        saf.add("Only Fire Hydrant");
        saf.add("Only Fire Sprinkler");
        saf.add("Not Available");

        ven.add("Ridge");
        ven.add("Turbo");
        ven.add("Not Available");

        ins.add("Cladding");
        ins.add("Roof");
        ins.add("Both Cladding and Roof");
        ins.add("Not Available");

        lev.add("Yes");
        lev.add("Not Available");

        agr.add("Yes");
        agr.add("Not Available");

        flo.add("Industrial");
        flo.add("VDF");
        flo.add("FM2");
        flo.add("Kota");
        flo.add("Others");

        flo1.add("Basement");
        flo1.add("1st");
        flo1.add("2nd");
        flo1.add("3rd");
        flo1.add("4th");

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, lan);
        landusage.setAdapter(adapter5);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, und);
        under_construction.setAdapter(adapter7);

        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, war);
        warehouse.setAdapter(adapter8);

        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, con);
        construction.setAdapter(adapter9);


        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, fir);
        firenoc.setAdapter(adapter11);

        ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, saf);
        safety.setAdapter(adapter12);

        ArrayAdapter<String> adapter13 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, ven);
        ventilation.setAdapter(adapter13);

        ArrayAdapter<String> adapter14 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, ins);
        insulation.setAdapter(adapter14);

        ArrayAdapter<String> adapter15 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, lev);
        leveler.setAdapter(adapter15);

        ArrayAdapter<String> adapter16 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, agr);
        agreement.setAdapter(adapter16);

        ArrayAdapter<String> adapter17 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, flo);
        flooring.setAdapter(adapter17);

        ArrayAdapter<String> adapter18 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, flo1);
        floor.setAdapter(adapter18);

        ArrayAdapter<String> adapter19 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, pli);
        plinth.setAdapter(adapter19);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


                            f1 = new File(file);
                            try {
                                f1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()), BuildConfig.APPLICATION_ID + ".provider", f1);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);
                            dialog.dismiss();
                        } else if (items[item].equals("Choose from Gallery")) {


                            Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
                            intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 30);
                            startActivityForResult(intent, 2);

                            /*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                            dialog.dismiss();*/



                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });


        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


                            f2 = new File(file);
                            try {
                                f2.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri2 = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()), BuildConfig.APPLICATION_ID + ".provider", f2);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 3);
                            dialog.dismiss();
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 4);
                            dialog.dismiss();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });


        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_contact_dialog);
                dialog.show();

                Spinner role = dialog.findViewById(R.id.role);
                final EditText name = dialog.findViewById(R.id.name);
                final EditText mobile = dialog.findViewById(R.id.mobile);
                final EditText landline = dialog.findViewById(R.id.landline);
                final EditText email = dialog.findViewById(R.id.email);
                Button addd = dialog.findViewById(R.id.button);

                final String[] rol = new String[1];

                final List<String> ll = new ArrayList<>();

                ll.add("Landlord");
                ll.add("Caretaker/ Security Guard");
                ll.add("Leasing Manager");
                ll.add("Others");


                ArrayAdapter<String> adapter21 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, ll);
                role.setAdapter(adapter21);

                role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        rol[0] = ll.get(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                addd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String n = name.getText().toString();

                        if (n.length() > 0)
                        {

                            contactBean item = new contactBean();
                            item.setRole(rol[0]);
                            item.setName(n);
                            item.setMobile(mobile.getText().toString());
                            item.setLandline(landline.getText().toString());
                            item.setEmail(email.getText().toString());

                            adapter222.addData(item);

                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String lo = wcontext.location.getText().toString();
                final String ad = wcontext.address.getText().toString();
                String mi = min.getText().toString();
                final String ma = max.getText().toString();
                String pl = plot.getText().toString();
                String co = covered.getText().toString();
                String av = available.getText().toString();
                final String re = rent.getText().toString();
                final String se = security.getText().toString();
                String com = common.getText().toString();
                final String ea = eaves.getText().toString();
                final String ce = center_height.getText().toString();
                final String op = opening_docks.getText().toString();
                final String tn = tenantname.getText().toString();
                final String fw = fwh.getText().toString();
                final String la = large.getText().toString();
                final String mo = mobile.getText().toString();
                final String sec = secondary.getText().toString();
                final String ow = owned.getText().toString();
                final String em = email.getText().toString();
                final String car = caretaker.getText().toString();
                final String cph = caretakerphone.getText().toString();
                final String cem = emailcaretaker.getText().toString();
                final String rem = remarks.getText().toString();
                final String poss = posession.getText().toString();
                final String bra = brands.getText().toString();


                if (wcontext.avai.equals("Built to Suit (BTS)"))
                {
                    mi = re;
                }
                else
                {
                    mi = re + " " + renttype.getSelectedItem().toString();
                }




                if (plottype.getSelectedItem().toString().equals("Others"))
                {
                    pl = pl + " " + plotunit.getText().toString();
                }
                else
                {
                    pl = pl + " " + plottype.getSelectedItem().toString();
                }

                if (coveredtype.getSelectedItem().toString().equals("Others"))
                {
                    co = co + " " + coveredunit.getText().toString();
                }
                else
                {
                    co = co + " " + coveredtype.getSelectedItem().toString();
                }

                if (availabletype.getSelectedItem().toString().equals("Others"))
                {
                    av = av + " " + availableunit.getText().toString();
                }
                else
                {
                    av = av + " " + availabletype.getSelectedItem().toString();
                }


                com = com + " " + commontype.getSelectedItem().toString();

                if (!leve.equals("No"))
                {
                    leve = dockleverernumber.getText().toString();
                }

                if (lann.equals("Others"))
                {
                    lann = land.getText().toString();
                }

                if (plin.equals("Others"))
                {
                    plin = plinthtext.getText().toString();
                }

                if (floo.equals("Others"))
                {
                    floo = flooringtext.getText().toString();
                }


                final String par;
                String ten;
                final String pla;


                RadioButton tb = tenant.findViewById(tenant.getCheckedRadioButtonId());
                ten = tb.getText().toString();

                if (ten.equals("Occupied"))
                {
                    ten = tenantname.getText().toString();
                }

                RadioButton tb1 = partition.findViewById(partition.getCheckedRadioButtonId());
                par = tb1.getText().toString();

                RadioButton tb2 = plan.findViewById(plan.getCheckedRadioButtonId());
                pla = tb2.getText().toString();

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.submit_popup);
                dialog.show();

                ImageButton ok = dialog.findViewById(R.id.imageButton3);
                ImageButton cancel = dialog.findViewById(R.id.imageButton4);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                final String finalMi = mi;
                final String finalCom = com;
                final String finalTen = ten;
                final String finalPl = pl;
                final String finalCo = co;
                final String finalAv = av;
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        List<contactBean> reqlist = adapter222.getList();

                        Gson gson = new Gson();
                        String json = gson.toJson(reqlist);

                        Log.d("reqlist", json);

                        MultipartBody.Part body2 = null;

                        try {

                            RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f2);
                            body2 = MultipartBody.Part.createFormData("featured", f2.getName(), reqFile1);


                            //adapter.addData(body2, uri2);


                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        wcontext.progress.setVisibility(View.VISIBLE);

                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                        CookieHandler cookieHandler = new CookieManager();
                        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor)
                                .cookieJar(new JavaNetCookieJar(cookieHandler))
                                .connectTimeout(120, TimeUnit.SECONDS)
                                .writeTimeout(120, TimeUnit.SECONDS)
                                .readTimeout(120, TimeUnit.SECONDS)
                                .build();

                        Bean b = (Bean) getContext().getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(client)
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        Log.d("images" , String.valueOf(adapter.getList().size()));

                        Log.d("count" , String.valueOf(itemcount));

                        Call<loginBean> call = cr.add_warehouse(
                                SharePreferenceUtils.getInstance().getString("id"),
                                wcontext.type,
                                wcontext.date,
                                wcontext.pid + wcontext.code,
                                String.valueOf(wcontext.lat),
                                String.valueOf(wcontext.lng),
                                wcontext.ds,
                                wcontext.st,
                                wcontext.ci,
                                lo,
                                ad,
                                wcontext.avai,
                                poss,
                                unde,
                                ware,
                                cond,
                                finalMi,
                                ma,
                                finalPl,
                                finalCo,
                                finalAv,
                                par,
                                minimum.getText().toString(),
                                re,
                                se,
                                finalCom,
                                ea,
                                ce,
                                op,
                                plin,
                                pla,
                                fire,
                                safe,
                                vent,
                                insu,
                                leve,
                                finalTen,
                                tn,
                                lann,
                                aggr,
                                floo,
                                fw,
                                la,
                                mo,
                                sec,
                                ow,
                                em,
                                car,
                                cph,
                                cem,
                                rem,
                                json,
                                String.valueOf(itemcount),
                                floo1,
                                bra,
                                body2,
                                adapter.getList()
                        );

                        call.enqueue(new Callback<loginBean>() {
                            @Override
                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    Intent intent = new Intent(getContext(), Survey.class);
                                    startActivity(intent);
                                    getActivity().finishAffinity();
                                }

                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                wcontext.progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<loginBean> call, Throwable t) {

                                t.printStackTrace();
                                wcontext.progress.setVisibility(View.GONE);

                            }
                        });

                    }
                });





            }
        });

        landusage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position == 4)
                {
                    lann = "Others";
                    land.setVisibility(View.VISIBLE);
                }
                else
                {
                    lann = lan.get(position);
                    land.setVisibility(View.GONE);
                }


                if (position == 0)
                {
                    agreementtitle.setVisibility(View.GONE);
                    agreementhide.setVisibility(View.GONE);
                }
                else if(position == 1)
                {
                    agreementtitle.setVisibility(View.GONE);
                    agreementhide.setVisibility(View.GONE);
                }
                else
                {
                    agreementtitle.setVisibility(View.VISIBLE);
                    agreementhide.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        flooring.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position == 4)
                {
                    floo = "Others";
                    flooringtext.setVisibility(View.VISIBLE);
                }
                else
                {
                    floo = lan.get(position);
                    flooringtext.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        plinth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position == 4)
                {
                    plin = "Others";
                    plinthtext.setVisibility(View.VISIBLE);
                }
                else
                {
                    plin = pli.get(position);
                    plinthtext.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        under_construction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                unde = und.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        plottype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2)
                {
                    plothide.setVisibility(View.VISIBLE);
                }
                else
                {
                    plothide.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        coveredtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2)
                {
                    coveredhide.setVisibility(View.VISIBLE);
                }
                else
                {
                    coveredhide.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        availabletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2)
                {
                    availablehide.setVisibility(View.VISIBLE);
                }
                else
                {
                    availablehide.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        warehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ware = war.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        construction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cond = con.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        firenoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fire = fir.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        safety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                safe = saf.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ventilation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                vent = ven.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        insulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                insu = ins.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        leveler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1)
                {
                    leve = lev.get(position);

                    dockleverernumber.setVisibility(View.GONE);
                    dockleverernumbertitle.setVisibility(View.GONE);

                }
                else
                {
                    leve = "";
                    dockleverernumber.setVisibility(View.VISIBLE);
                    dockleverernumbertitle.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        agreement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                aggr = agr.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                floo1 = flo1.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*posession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.date_dialog);
                dialog.show();


                final DatePicker picker = dialog.findViewById(R.id.date);
                Button ok = dialog.findViewById(R.id.ok);

                long now = System.currentTimeMillis() - 1000;
                picker.setMinDate(now);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int year = picker.getYear();
                        int month = picker.getMonth();
                        int day = picker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd, YYYY");
                        String strDate = format.format(calendar.getTime());

                        posession.setText(strDate);

                        dialog.dismiss();

                    }
                });

            }
        });
*/
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {


            ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {

                String ypath = images.get(i).path;
                assert ypath != null;
                f1 = new File(ypath);

                File file = null;
                try {
                    file = new Compressor(getContext()).compressToFile(f1);

                    uri = Uri.fromFile(file);

                } catch (IOException e) {
                    e.printStackTrace();
                }



                Log.d("path", ypath);
                Log.d("uri", String.valueOf(uri));

                MultipartBody.Part body = null;

                try {

                    RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData("file[]", file.getName(), reqFile1);


                    adapter.addData(body, uri);


                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }







        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            Log.d("uri", String.valueOf(uri));

            MultipartBody.Part body = null;


            try {

                File file = new Compressor(getContext()).compressToFile(f1);

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("file[]", file.getName(), reqFile1);

                Uri uri1 = Uri.fromFile(file);

                adapter.addData(body, uri1);

            } catch (Exception e1) {
                e1.printStackTrace();
            }



        }


        if (requestCode == 4 && resultCode == RESULT_OK && null != data) {
            uri2 = data.getData();

            Log.d("uri1", String.valueOf(uri2));

            String ypath = getPath(getContext(), uri2);
            assert ypath != null;

            File file = null;
            file = new File(ypath);

            try {
                f2 = new Compressor(getContext()).compressToFile(file);

                uri2 = Uri.fromFile(f2);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("path1", ypath);

            image1.setImageURI(uri2);



        } else if (requestCode == 3 && resultCode == RESULT_OK) {

            Log.d("uri1", String.valueOf(uri2));

            try {

                File file = new Compressor(getContext()).compressToFile(f2);

                f2 = file;

                uri2 = Uri.fromFile(f2);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            image1.setImageURI(uri2);


        }


    }

    private static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }


    private static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        Context context;
        List<MultipartBody.Part> list = new ArrayList<>();
        List<Uri> ulist = new ArrayList<>();

        ImageAdapter(Context context, List<MultipartBody.Part> list) {
            this.context = context;
            this.list = list;
        }

        void addData(MultipartBody.Part item, Uri uri) {
            list.add(item);
            ulist.add(uri);
            notifyDataSetChanged();
            imagecount.setText("Property Images (" + getItemCount() + ")");
            itemcount = getItemCount();
        }

        void removeData(int pos) {
            list.remove(pos);
            ulist.remove(pos);
            notifyDataSetChanged();
            imagecount.setText("Property Images (" + getItemCount() + ")");
            itemcount = getItemCount();
        }

        List<MultipartBody.Part> getList() {
            return list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            Uri item = ulist.get(position);

            holder.image.setImageURI(item);

            holder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            ImageButton close;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                close = itemView.findViewById(R.id.close);
            }
        }
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        Context context;
        List<contactBean> list = new ArrayList<>();

        ContactAdapter(Context context, List<contactBean> list) {
            this.context = context;
            this.list = list;
        }

        void addData(contactBean item) {
            list.add(item);
            notifyDataSetChanged();
        }

        void removeData(int pos) {
            list.remove(pos);
            notifyDataSetChanged();
        }

        List<contactBean> getList() {
            return list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.contact_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            contactBean item = list.get(position);

            holder.role.setText(item.getRole());
            holder.name.setText(item.getName());
            holder.mobile.setText(item.getMobile());
            holder.landline.setText(item.getLandline());
            holder.email.setText(item.getEmail());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView role , name , mobile , landline , email;
            ImageButton delete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                role = itemView.findViewById(R.id.textView10);
                name = itemView.findViewById(R.id.textView15);
                mobile = itemView.findViewById(R.id.textView16);
                landline = itemView.findViewById(R.id.textView17);
                email = itemView.findViewById(R.id.textView18);
                delete = itemView.findViewById(R.id.imageButton5);
            }
        }
    }

}
