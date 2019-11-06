package com.ahnsafety.ex71cameratest3video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView vv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv= findViewById(R.id.vv);
        btn= findViewById(R.id.btn);

        //Video는 용량문제로 무조건 파일로 저장하는 방식을 사용함.
        //그래서 반드시 외부저장소에 대한 퍼미션필요
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 10:
                if( grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "카메라 기능 사용 가능", Toast.LENGTH_SHORT).show();
                    btn.setEnabled(true);
                }else{
                    Toast.makeText(this, "카메라 기능 제한", Toast.LENGTH_SHORT).show();
                    btn.setEnabled(false);
                }
                break;
        }
    }

    public void clickBtn(View view) {
        //비디오 촬영모드로 카메라앱 실행
        Intent intent= new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 100:
                if(resultCode==RESULT_OK){
                    //촬영된 비디오의 파일경로 얻어오기
                    Uri uri= data.getData();
                    vv.setVideoURI(uri);

                    //비디오뷰를 클릭했을 때 컨트롤 바가 아래에서 올라오도록.
                    MediaController mediaController= new MediaController(this);
                    vv.setMediaController(mediaController);

                    //비디오 실행하기
                    //비디오는 용량이 크므로 재생준비에 시간이 걸림
                    //재생준비완료 리스너를 이용하길 권장
                    vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            vv.start();
                        }
                    });
                }
                break;
        }
    }
}
