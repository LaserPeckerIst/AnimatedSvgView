/*
 * Copyright (C) 2016 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jaredrummler.android.animatedsvgview.demo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.android.widget.AnimatedSvgView;

public class MainActivity extends AppCompatActivity {

    /*package*/ AnimatedSvgView svgView;
    /*package*/ int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);

        svgView.postDelayed(new Runnable() {

            @Override
            public void run() {
                svgView.start();
            }
        }, 500);

        svgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (svgView.getState() == AnimatedSvgView.STATE_FINISHED) {
                    svgView.start();
                }
            }
        });

        svgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {

            @Override
            public void onStateChange(@AnimatedSvgView.State int state) {
                if (state == AnimatedSvgView.STATE_TRACE_STARTED) {
                    findViewById(R.id.btn_previous).setEnabled(false);
                    findViewById(R.id.btn_next).setEnabled(false);
                } else if (state == AnimatedSvgView.STATE_FINISHED) {
                    findViewById(R.id.btn_previous).setEnabled(index != -1);
                    findViewById(R.id.btn_next).setEnabled(true);
                    if (index == -1) index = 0; // first time
                }
            }
        });

        final TextPathAnimateView textPathAnimateView = (TextPathAnimateView) findViewById(R.id.text_path_animated_view);
        textPathAnimateView.setAnimateText("LaserPecker");
        textPathAnimateView.setTraceColor(Color.GREEN);
        textPathAnimateView.setFillColor(Color.BLUE);
        textPathAnimateView.setFillStart(1500);
        //textPathAnimateView.setFillTime(1_000);
        textPathAnimateView.start();
        textPathAnimateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textPathAnimateView.start();
            }
        });
    }

    public void onNext(View view) {
        if (++index >= SVG.values().length) index = 0;
        setSvg(SVG.values()[index]);
    }

    public void onPrevious(View view) {
        if (--index < 0) index = SVG.values().length - 1;
        setSvg(SVG.values()[index]);
    }

    public void onTest(View view) {
        Path[] paths = new Path[1];
        Path path = new Path();
        //path.addCircle(300, 300, 100, Path.Direction.CW);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(30);
        paint.setTextSize(60);
        String text = "LaserPecker";
        paint.getTextPath(text, 0, text.length(), 0, paint.descent() - paint.ascent(), path);

        paths[0] = path;
        svgView.setGlyphPaths(paths);
        svgView.rebuildGlyphData();
        svgView.start();
    }

    private void setSvg(SVG svg) {
        svgView.setGlyphStrings(svg.glyphs);
        svgView.setFillColors(svg.colors);
        svgView.setViewportSize(svg.width, svg.height);
        svgView.setTraceResidueColor(0x32000000);
        svgView.setTraceColors(svg.colors);
        svgView.rebuildGlyphData();
        svgView.start();
    }

}
