package com.hansung.android.medicine;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import java.io.FileNotFoundException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisActivity extends AppCompatActivity {
    private static final String TAG = RegisActivity.class.getName();

    // 카메라 불러오기 관련
    static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    // 앨범 불러오기 관련
    static final int REQUEST_IMAGE_PICK = 2;
    // 불러온 사진의 파일 경로
    private Uri filePath;

    // 카메라 통해 찍은 사진 관련 변수
    // 사진 이름 변수
    private String mPhotoFileName = null;
    // 사진 파일
    private File mPhotoFile;
    private Uri imageUri;

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItem;
    ArrayList<String> pills;

    EditText user_name;
    EditText user_height;
    EditText user_weight;
    EditText user_birth;

    EditText user_pill;
    Button add;
    Button save_user;

    RadioButton male;
    RadioButton female;


    Button upload_btn;
    TextView btn_result;

    int count = 0;
    String gender;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);





        user_name = (EditText) findViewById(R.id.username);
        user_birth = (EditText)findViewById(R.id.user_birth);
        user_height = (EditText) findViewById(R.id.user_height);
        user_weight = (EditText) findViewById(R.id.user_weight);

        user_pill = (EditText) findViewById(R.id.user_pill);


        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        btn_result = (TextView)findViewById((R.id.btn_result));

        Button upload = (Button) findViewById(R.id.upload_btn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그 뜨게함
                makeDialog();
            }
        });



        add = (Button) findViewById(R.id.add);
        save_user = (Button)findViewById(R.id.submit_user);

        listItem = new ArrayList<String>();
        final ArrayList<String> arrayList = new ArrayList<String>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user_pill.getText().toString().length()> 0) {
                    listItem.add(user_pill.getText().toString());
                    adapter.notifyDataSetChanged();
                    user_pill.setText("");
                    count += 1;

                    Toast.makeText(RegisActivity.this, "추가 완료.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"약 이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                pills = listItem;



            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem){

            @Override

            public View getView(int position, View convertView, ViewGroup parent)

            {

                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.BLUE);

                return view;

            }

        };

        listView = (ListView) findViewById(R.id.pill_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Toast.makeText(getApplicationContext(),listItem.get(i).toString() + "의 복용 시간을 설정합니다",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), SetActivity.class);
                //startActivity(intent);
                Intent intent = new Intent(RegisActivity.this, SetActivity.class);
                intent.putExtra("user_pill", listItem.get(i).toString());
                intent.putExtra("user_name", user_name.getText().toString());
                startActivity(intent);
            }
           /* public void onItemDoubleClick(AdapterView<?> adapterView, View view, int i, long l){

                Toast.makeText(getApplicationContext(),listItem.get(i).toString() + "삭제",Toast.LENGTH_SHORT).show();
                listItem.remove(i);
                adapter.notifyDataSetChanged();

            }

            */
        });

        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(user_name.getText().toString().length() > 0 && user_birth.getText().toString().length() > 0 && user_height.getText().toString().length() > 0
                    && user_weight.getText().toString().length() > 0 && count > 0 ){
                    count++;
                    Toast.makeText(getApplicationContext(), user_name.getText().toString() +"님 프로필 등록 완료",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisActivity.this, MainActivity.class);
                    //intent.putExtra("user_name", user_name.getText().toString());
                    //intent.putExtra("user_birth", user_birth.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "사용자 정보를 모두 입력해주세요.",Toast.LENGTH_SHORT).show();
                }

                if(male.isChecked() == true){
                    gender = "남성";
                }
                else{
                    gender = "여성";
                }
                FirebasePostRegis regis = new FirebasePostRegis(user_birth.getText().toString(), gender,
                        user_height.getText().toString(),user_name.getText().toString(), user_weight.getText().toString(), pills.toString());


                //    weight w = new weight(user_weight.getText().toString());

                // 앞 child는 맨위꺼 그밑에 child가 밑으로 싸임 rgrg
                // 하나쓰면 왜.... 여러개 user만드는거는 더 봐야할듯

                //이렇게 하나써주면 setValue에있는 값들 +싹뜸

               // we we = new we(user_weight.getText().toString());
                 databaseReference.child("users").child(user_name.getText().toString()).setValue(regis);





            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // camera 사용 code 이고 result_ok일시 작동
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            // 찍은 사진 mPhotoFile을 bitmap으로 decodeFile
            Bitmap cameraphoto = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

            // 찍은 사진 rotate
            // Bitmap cameraRotated = rotateBitmap(cameraphoto, orientation);
            // imageview 사진 뜨게함
            //image.setImageBitmap(cameraRotated);


            // imageUri (찍은사진)이 있으면
            if (imageUri != null) {
                /*
                 * firebase기반으로 한 cloud storage -> 사용자가 찍은 image를 cloud storage로 넘김
                 */

                // image를 cloud로 넘길때 이름
                String filename = user_name.getText().toString() + ".jpg";

                // image를 넘길 폴더
                String foldername = user_name.getText().toString() + "/";

                // reference 생성
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://pill-dispenser-d4bec.appspot.com").child(foldername + filename);

                storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    // coloud storage에 image 업로드 성공시
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("Firebase cloud storage", "Upload Success!");
                        btn_result.setText("이미지 업로드 성공");
                    }
                })
                        //실패시
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firebase cloud storage", "Upload Fail!");
                                btn_result.setText("이미지 업로드 실패");
                            }
                        })
                        //진행중
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("Firebase cloud storage", "Loading!");
                                btn_result.setText("이미지 업로딩");
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {


            Log.e("error1", "a");

            filePath = data.getData();

            // 사진의 절대 경로명
        //    Uri mPhotoUri = Uri.parse(getRealPathFromURI(filePath));

          //  ExifInterface exif = null;

            // inpuStream이라서 try / catch문 없애면 안됨
            try {

                int batchNum = 0;
                InputStream buf = getContentResolver().openInputStream(filePath);
             //   Bitmap albumphoto = BitmapFactory.decodeStream(buf);
                buf.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (filePath != null) {
                    Log.e("error1", "b");
                    /*
                     * firebase기반으로 한 cloud storage -> 사용자가 찍은 image를 cloud storage로 넘김
                     */

                    // image를 cloud로 넘길때 이름
                    String filename = user_name.getText().toString() + ".jpg";

                    // image를 넘길 폴더
                    String foldername = user_name.getText().toString() + "/";

                    // reference 생성
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://pill-dispenser-d4bec.appspot.com").child(foldername + filename);


                    storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        // coloud storage에 image 업로드 성공시
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.e("Firebase cloud storage", "Upload Success!");
                            btn_result.setText("이미지 업로드 성공");
                        }
                    })
                            //실패시
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firebase cloud storage", "Upload Fail!");
                                    btn_result.setText("이미지 업로드 실패");
                                }
                            })
                            //진행중
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("Firebase cloud storage", "Loading!");
                                    btn_result.setText("이미지 업로딩");
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                }

                //   }/
            }
        }/*catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();*/


   // }



    private void makeDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진 업로드");
        builder.setMessage("업로드 할 방법을 선택해주세요.");
        builder.setNeutralButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.setPositiveButton("앨범 선택",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // 모든게 다 허용되어있으면 --> 앨범 오픈
                        if(hasAlbumPermission()){
                            openAlbum();
                        } else{
                            // 허용안된거 있으면 권한 요청
                            requestAlbumPermission();
                        }
                    }
                });
        builder.setNegativeButton("사진 촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 모든게 다 허용되어있으면 --> 카메라 오픈
                        if(hasCameraPermission()){
                            openCamera();
                        } else {
                            // 허용안된거 있으면 권한 요청
                            requestCameraPermission();
                        }
                    }
                });
        builder.show();
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx); cursor.close();
        }
        return result;
    }


    // 사진이 회전되어있으면 돌려주는 bitmap 함수
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
    // 카메라 열어서 사진찍는 함수
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                imageUri = FileProvider.getUriForFile(this, "com.hansung.android.medicine.fileprovider", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }


    // 앨범 열어서 사진 pick하는 함수
    private void openAlbum(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(albumIntent, REQUEST_IMAGE_PICK);
    }


    // 카메라 허용 권한이 있는지 check 함수
    private boolean hasCameraPermission() {
        //버전이 M 이상이면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // manifest camera 권한 있는지 확인
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    // 앨범 권한 허용이 있는지 check 함수
    private boolean hasAlbumPermission() {
        //버전이 M 이상이면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // manifest 외부저장소 권한 있는지 확인
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    // 카메리 허용 권한 물어보는 함수
    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 카메라 허용 권한 요청 ok면 log함수 나옴
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Log.e("Camera", "Camera Permission Required");
            }
            // 허용 안되어있으면
            // 카메라 사용 권한 요청
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    // 앨범 허용 권한 물어보는 함수
    private void requestAlbumPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 외부저장소 허용 권한 요청 ok면 log함수 나옴
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("External Storage", "Storage Permission Required");
            }
            // 허용 안되어있으면
            // 외부저장소 사용 권한 요청
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestcode가 카메라 허용 코드이면
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // permission 다 허용되면
            if (hasAllPermissions(grantResults)) {
                // camera 열기
                openCamera();
            } else {
                requestCameraPermission();
            }
        }
        // requestcode가 앨범선택 허용 코드이면
        else if(requestCode == REQUEST_IMAGE_PICK){
            if (hasAllPermissions(grantResults)) {
                openAlbum();
            } else {
                requestAlbumPermission();
            }
        }
    }

    // permission이 다 허용 됐을시 true 반환
    //@param grantResults the permission grant results
    //@return true if all the reqested permission has been granted
    // otherwise returns false
    private boolean hasAllPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }



    // 현재 날짜 시간 String 함수
    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



