package com.will.supercamera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    Camera mCamera;
    SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initCamera();
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.sv_camera);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setStartPreview(mCamera, holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                configCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void configCamera() {
        mCamera.stopPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        for (Camera.Size size :
                sizeList) {
            Log.e("size", "Height:" + size.height + "  Width:" + size.width);
        }
        Camera.Size size = parameters.getPreviewSize();
        Log.e("default size", "Height:" + size.height + "  Width:" + size.width);
        parameters.setPreviewSize(surfaceView.getHeight(),surfaceView.getWidth());
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Toast.makeText(MainActivity.this, success ? "focus success" : "focus failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCamera() {
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            mCamera = null;
        }
    }

    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
