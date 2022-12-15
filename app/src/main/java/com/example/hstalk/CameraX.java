package com.example.hstalk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CameraX extends AppCompatActivity implements ImageAnalysis.Analyzer{
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    private ImageCapture imageCapture;
    Camera camera;
    ImageView chuphinh,reload,layhinh;
    ImageView switch_camera;
    CameraControl cameraControl;
    CameraInfo cameraInfo;
    SeekBar zoom;
    LinearLayout viewchup;
    TextView noidung,countdown,ketqua;
    Boolean check = false;
    String uri;
    ImageLabeler labeler;
    PoseDetector poseDetector;
    TextToSpeech speak;
    ImageProxy get;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_x);
        // Releases model resources if no longer used.
        noidung = findViewById(R.id.noidung);
        countdown = findViewById(R.id.countdown);
        ketqua = findViewById(R.id.ketqua);
        //Pose
        PoseDetectorOptions options =
                new PoseDetectorOptions.Builder()
                        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                        .build();

        poseDetector = PoseDetection.getClient(options);

        //Image
        LocalModel localModel = new LocalModel.Builder()
                .setAssetFilePath("model.tflite")
                .build();
        CustomImageLabelerOptions customImageLabelerOptions =
                new CustomImageLabelerOptions.Builder(localModel)
                        .setConfidenceThreshold(0.7f)
                        .setMaxResultCount(1)

                        .build();
        labeler = ImageLabeling.getClient(customImageLabelerOptions);
        xinquyen_camera();
        //ánh xạ các các thành phần
        previewView = findViewById(R.id.previewView);
        chuphinh = findViewById(R.id.chuphinh);
        zoom = findViewById(R.id.zoom);
        switch_camera = findViewById(R.id.switch_camera);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height_screen = displayMetrics.heightPixels;
        int width_screen = displayMetrics.widthPixels;
        int height_new = (width_screen/3)*4;
        previewView.setLayoutParams(new ConstraintLayout.LayoutParams(width_screen,height_new));
        start(check);
        zoom.setMax(100);
        zoom.setProgress(0);

        chuphinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //chuphinh();
                speak.speak(ketqua.getText().toString(),TextToSpeech.QUEUE_FLUSH,null, UUID.randomUUID().toString());

            }
        });


        switch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check)
                {
                    check = false;
                    start(check);
                }else {
                    check = true;
                    start(check);
                }
            }
        });
        speak = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                {
                    speak.setLanguage(Locale.US);
                }
            }
        });
    }
    public void chuphinh()
    {
        //layhinh.setEnabled(false);
        //Tạo nơi mà ảnh sẽ lưu, ở đây tôi chọn vùng cache để lưu
        File z = new File(getCacheDir(),"images");
        z.mkdirs();
        File luu = new File(z, "admin.jpg");
        ImageCapture.OutputFileOptions outputFileOptions = null;
        if(!check)
        {
            ImageCapture.Metadata meta = new  ImageCapture.Metadata();
            meta.setReversedHorizontal(true);
            outputFileOptions = new ImageCapture.OutputFileOptions.Builder(luu).setMetadata(meta).build();

        }else {
            outputFileOptions = new ImageCapture.OutputFileOptions.Builder(luu).build();
        }
        //Chụp ảnh
        //ContextCompat.getMainExecutor(MainActivity.this) là luồng xử lý ảnh ở dây tôi dùng trên luồng chính
        //takePicture có 2 phương thức, nếu bạn không truyền biến outputFileoption (Khởi tạo nơi lưu) thì mặt định nó sẽ lưu ở file cache =]]
        //Ừ thì cái outputFileOptions cũng lưu ở file cache

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(CameraX.this), new ImageCapture.OnImageSavedCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            //sau khi chụp thành công các sự kiện xảy ra, như kiểu nếu thành công bạn sẽ làm gì
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                //lấy uri nơi lưu hình


                try {
                    //sao khi chụp thành công tôi dùng khung hình lại  tạo hiệu ứng lấy ảnh
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    cameraProvider.unbindAll();
//                    viewchup.setVisibility(View.VISIBLE);
                    chuphinh.setVisibility(View.GONE);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Toast.makeText(CameraX.this, "Lưu hình thành công"  , Toast.LENGTH_SHORT).show();
//                layhinh.setEnabled(true);
                uri = luu.toString();
                InputImage image;
                try {
                    image = InputImage.fromFilePath(CameraX.this, Uri.fromFile(luu));
                    labeler.process(image)
                            .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                                @Override
                                public void onSuccess(List<ImageLabel> imageLabels) {
                                    for(ImageLabel im : imageLabels)
                                    {
                                        Log.e("nganh_custom",im.getText() + "   " + im.getConfidence() + " " + im.getIndex());

                                    }

                                }
                            });
                }catch (Exception e)
                {

                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });
    }
    public void xinquyen_camera()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},7979);
        }else {

        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7979)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                xinquyen_camera();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void start(boolean check_laycamera)
    {
        //Ở đây chúng ta sẽ ràng buộc luồng xử lý vào vòng đời camera, và cameraX sẽ tự hiểu vòng đòi của chính thứ chứa nó ở đây là vòng đời hàm main
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                //Hàm có thể coi như là setview từ camera lên previewView
                bindPreview(cameraProvider,check_laycamera);
            } catch (ExecutionException | InterruptedException e) {
            }
        }, ContextCompat.getMainExecutor(this));
    }
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.R)
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider, boolean check_laycamera) {
        //Hủy mọi liên kết vòng đời của cameraX trước khi liên kết lại
        cameraProvider.unbindAll();
        //Build ImageCapture
        imageCapture = new ImageCapture.Builder()
                .setMaxResolution(new Size(1080,1920))//tỉ lệ hình ảnh
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)//tốc độ luồng xử lý và chất lượng hiình ảnh
                .build();
        //Phân tích và xử lý hình ảnh nếu muốn dùng thì có thể đọc thêm doc của google về cameraX
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        //Build preview hình ảnh
        Preview preview = new Preview.Builder()
                .build();
        CameraSelector cameraSelector = null;
        if(check_laycamera){
            cameraSelector= new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();
        }else {
            cameraSelector= new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build();
        }


        //kết nối Preview vào PreviewView
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(CameraX.this), this);




        //cameraProvider.bindToLifecycle sẽ trả về một đối tượng camera và đồng thời sẽ liên kết vòng đợi camera để xử dụng
        //ở đây họ dùng thuật ngữ use_case, nhưng bạn có trẻ truyền đối số theo mẫu dưới là đc
        camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector,preview,imageCapture,imageAnalysis);

        //Ở camera chúng ta sẽ có 2 đối tượng để xử lý ảnh đầu ra và vào đồng thời các tính năng mặc định như set độ phơi sáng, bật đèn flash, zoom ra zoom vào
        cameraControl = camera.getCameraControl();
        cameraInfo = camera.getCameraInfo();


        zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // ở đây tôi dùng seekbar để zoom ra zoom vào camera, hàm setLinearZoom nhận giá trị  từ 0-1
                cameraControl.setLinearZoom(i/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    String chuoi = "";
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        get = imageProxy;
        @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
        if(mediaImage != null)
        {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            labeler.process(image).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                        @Override
                        public void onSuccess(List<ImageLabel> imageLabels) {
                            time(time_yes);
                            time_yes = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(imageLabels.size() > 0)
                                    {
                                        for(ImageLabel im : imageLabels) {
                                            chuoi = chuoi + im.getText();
                                            noidung.setText(im.getText());
                                            ketqua.setText(chuoi);
                                            Log.e("nganhlon_custom", chuoi);
                                            break;
                                        }
                                    }else {
                                        noidung.setText("Không tìm thấy");
                                    }
                                    time_yes = true;
                                    imageProxy.close();
                                }
                            }, 3000);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("nganhlon_custom","loi roi   " + e.toString());
                            imageProxy.close();
                            noidung.setText("");
                        }
                    });


        }
    }
    Boolean time_yes = false;
    int z = 4;
    public void time(boolean yes)
    {
        if(yes)
        {
            z=4;
        }
        CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                z = z- 1;
                countdown.setText(z+"s");
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }
}