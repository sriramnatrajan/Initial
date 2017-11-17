package com.one.downloader.whatsappstatusdownloader.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.nispok.snackbar.SnackbarManager;
import com.one.downloader.whatsappstatusdownloader.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


/**
 * Created by Ace Programmer Rbk<rodney@swiftpot.com> on 06-May-17
 * 8:24 AM
 */
public class RecyclerViewMediaAdapter extends RecyclerView.Adapter<RecyclerViewMediaAdapter.FileHolder> {

    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/legacy/WSDownloader/";
    private ArrayList<File> filesList;
    private Activity activity;
    Uri video;

    public RecyclerViewMediaAdapter(ArrayList<File> filesList, Activity activity) {
        this.filesList = filesList;
        this.activity = activity;
        setHasStableIds(true);
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_media_row_item, parent, false);
        return new FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final FileHolder holder, int position) {
        final File currentFile = filesList.get(position);

        holder.pic_imgShare.setOnClickListener(this.shareImageMedia(currentFile));
         holder.pic_imgDownload.setOnClickListener(this.downloadMediaItem(currentFile));

        holder.vid_imgdownlaod.setOnClickListener(this.downloadMediaItem(currentFile));
        holder.vid_imageShare.setOnClickListener(this.shareVideoMedia(currentFile));


        if (currentFile.getAbsolutePath().endsWith(".mp4")) {
            holder.cardViewImageMedia.setVisibility(View.GONE);
            holder.cardViewVideoMedia.setVisibility(View.VISIBLE);
            video = Uri.parse(currentFile.getAbsolutePath());
            holder.videoViewVideoMedia.setVideoURI(video);
            holder.videoViewVideoMedia.seekTo(100);
            holder.videoViewVideoMedia.pause();


            holder.imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.fromFile(currentFile);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setDataAndType(uri, "video/*");
                    activity.startActivity(i);
                }
            });

        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.imageViewImageMedia.setImageBitmap(myBitmap);
        }

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }


    public View.OnClickListener shareImageMedia(final File sourceFile) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Runnable() {

                    @Override
                    public void run() {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        sharingIntent.setType("image/*");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(sourceFile));
                        activity.startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
                    }
                }.run();
            }
        };
    }
    public View.OnClickListener shareVideoMedia(final File sourceFile) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Runnable() {

                    @Override
                    public void run() {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        sharingIntent.setType("video/*");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(sourceFile));
                        activity.startActivity(Intent.createChooser(sharingIntent, "Share Video Using"));
                    }
                }.run();
            }
        };
    }
    public View.OnClickListener downloadMediaItem(final File sourceFile) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Runnable() {

                    @Override
                    public void run() {
                        try {

                            Log.d("Here", "Storage click:  " + DIRECTORY_TO_SAVE_MEDIA_NOW);

                            copyFile(sourceFile, new File(Environment.getExternalStorageDirectory() + DIRECTORY_TO_SAVE_MEDIA_NOW, sourceFile.getName()));
                            SnackbarManager.show(com.nispok.snackbar.Snackbar.with(activity).text("Saved").
                                    actionLabel("Open"));

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("RecyclerV", "onClick: Error:" + e.getMessage());
                            SnackbarManager.show(com.nispok.snackbar.Snackbar.with(activity).text("Failed to download"));

                        }
                    }
                }.run();
            }
        };
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mc;
        ImageView imageViewImageMedia;
        VideoView videoViewVideoMedia;
        CardView cardViewVideoMedia;
        CardView cardViewImageMedia;
        ImageView imgPlay, imgPause,pic_imgShare,pic_imgDownload,vid_imageShare,vid_imgdownlaod;
       // FloatingActionButton buttonVideoDownload, buttonImageDownload;

        public FileHolder(View itemView) {
            super(itemView);
            mc = itemView.getContext();
            imageViewImageMedia = (ImageView) itemView.findViewById(R.id.imageViewImageMedia);
            videoViewVideoMedia = (VideoView) itemView.findViewById(R.id.videoViewVideoMedia);
            cardViewVideoMedia = (CardView) itemView.findViewById(R.id.cardViewVideoMedia);
            cardViewImageMedia = (CardView) itemView.findViewById(R.id.cardViewImageMedia);

            pic_imgShare = (ImageView) itemView.findViewById(R.id.send_pic);
            pic_imgDownload = (ImageView) itemView.findViewById(R.id.download_pic);
            vid_imageShare=(ImageView)itemView.findViewById(R.id.send_vid);
            vid_imgdownlaod=(ImageView)itemView.findViewById(R.id.download_vid);
            imgPlay = (ImageView) itemView.findViewById(R.id.play_btn);
            imgPause = (ImageView) itemView.findViewById(R.id.pause_btn);
            //imgPlay.setOnClickListener(this);
            //imgPause.setOnClickListener(this);
            cardViewVideoMedia.setOnClickListener(this);
            cardViewVideoMedia.setOnClickListener(this);
            //buttonImageDownload.setBackgroundColor(R.color.md_green_400);
        }

        @Override
        public void onClick(View view) {

        }

        /*@Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play_btn:

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    break;
                default:
                    break;
            }
        }*/


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*@Override
    public int getItemViewType(int position) {
        return position;
    }*/
}
